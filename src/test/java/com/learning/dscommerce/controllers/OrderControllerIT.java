package com.learning.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.dscommerce.TokenUtil;
import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.DTO.OrderItemDTO;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.services.OrderService;
import com.learning.dscommerce.test.OrderFactory;
import com.learning.dscommerce.test.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private OrderDTO orderDTO;
    private Long existingId, nonExistingId;
    private String adminUsername, clientUsername;
    private String adminPassword, clientPassword;
    private String clientToken, adminToken, invalidToken;

    @BeforeEach
    void setup() throws Exception {

        order = OrderFactory.createOrder(UserFactory.createUserAdmin());
        orderDTO = OrderFactory.createOrderDTO(order);
        existingId = 1L;
        nonExistingId = 10L;
        adminUsername = "alex@gmail.com";
        clientUsername = "maria@gmail.com";
        adminPassword = "123456";
        clientPassword = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "avba"; // generates invalid token
    }

    // Inserção de pedido insere pedido com dados válidos quando logado como cliente
    @Test
    public void insertShouldReturnOrderDTOWhenValidDataAndClientLogged() throws Exception {
        Long totalOrderNumber = 3L;
        String json = objectMapper.writeValueAsString(orderDTO);

        ResultActions result = mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + clientToken)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(totalOrderNumber + 1));
    }

    // Inserção de pedido retorna 422 e mensagem customizadas
    // com dados inválidos quando logado como cliente (ter pelo menos um item)
    @Test
    public void insertShouldReturnsUnprocessableEntityWhenHasNoItems() throws Exception {
        List<OrderItemDTO> list = new ArrayList<>();
        orderDTO.setItems(list);
        String json = objectMapper.writeValueAsString(orderDTO);

        ResultActions result = mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + clientToken)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnprocessableEntity());

    }

    // Inserção de pedido retorna 403 quando logado como admin
    @Test
    public void insertShouldReturnForbiddenWhenAdminLogged() throws Exception {

        String json = objectMapper.writeValueAsString(orderDTO);

        ResultActions result = mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isForbidden());
    }

    // Inserção de pedido retorna 401 quando não logado como admin ou cliente
    @Test
    public void insertShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {

        String json = objectMapper.writeValueAsString(orderDTO);

        ResultActions result = mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + invalidToken)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }

    // Busca de pedido por id retorna 404 para pedido inexistente quando logado como
    // cliente
    @Test
    public void findByIdShouldReturnNotFoundWhenNonExistingIdAndClientLogged() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}", nonExistingId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    // Busca de pedido por id retorna 401 quando não logado como admin ou cliente
    @Test
    public void findByIdShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}", nonExistingId)
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    // Busca de pedido por id retorna 404 para pedido inexistente quando logado como
    // admin
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}", nonExistingId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    // Busca de pedido por id retorna 403 quando pedido não pertence ao usuário (com
    // perfil de cliente)
    @Test
    public void findByIdReturnOrderDTOWhenIdExistsAndOtherClient() throws Exception {
        Long otherClientId = 2L;
        ResultActions result = mockMvc.perform(get("/orders/{id}", otherClientId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());

    }

    // Busca de pedido por id retorna pedido existente quando logado como cliente e
    // o pedido pertence ao usuário
    @Test
    public void findByIdReturnOrderDTOWhenIdExistsAndSelfClient() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}", existingId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1));
        result.andExpect(jsonPath("$.status").value("PAID"));
    }

    // Busca de pedido por id retorna pedido existente quando logado como admin
    @Test
    public void findByIdReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}", existingId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1));
        result.andExpect(jsonPath("$.status").value("PAID"));
    }

}
