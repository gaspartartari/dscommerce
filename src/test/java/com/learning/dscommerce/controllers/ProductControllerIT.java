package com.learning.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.dscommerce.TokenUtil;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.test.ProductFactory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String clientToken, adminToken, invalidToken;
    private String adminUsername, adminPassword, clientUsername, clientPassword;
    private Product product;
    private ProductDTO productDTO;
    private Long dependenId, existingId, nonExistingId;

    @BeforeEach
    void setup() throws Exception {
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "abcdr"; // generates invalid token
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO(product);
        dependenId = 3L;
        existingId = 2L;
        nonExistingId = 100L;

    }

    // Deleção de produto retorna 401 quando não logado como admin ou cliente
    @Test
    public void deleteByIdShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());

    }

    // Deleção de produto retorna 403 quando logado como cliente
    @Test
    public void deleteByIdShouldReturnForbiddenWhenClientLogged() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());

    }

    // Deleção de produto retorna 400 para produto dependente quando logado como admin
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteByIdShouldReturnBadRequestWhenDependentProductAndAdminLogged() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", dependenId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());

    }

    //Deleção de produto retorna 404 para produto inexistente quando logado como admin
    @Test
    public void deleteByIdShouldReturnNotFoundWhenNonExsistingProductAndAdminLogged() throws Exception {
       
        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    // Deleção de produto deleta produto existente quando logado como admin
    @Test
    public void deleteByIdShouldDeleteProductWhenAdminlogged() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

    }

    // Inserção de produto retorna 401 quando não logado como admin ou cliente
    @Test
    public void insertShouldReturnUnauthorizedWhenClientOrUserNotLogged() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + invalidToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    // Inserção de produto retorna 403 quando logado como cliente
    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {


        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + clientToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());

    }

    // Inserção de produto retorna 422 e mensagens customizadas com dados inválidos
    // quando logado como admin e não tiver categoria associada
    @Test
    public void insertShouldReturnUnprocessableEntityAndCustomErrorsWhenProductCategoryNotInformedAndAdminLogged() throws Exception {

        ProductDTO invalidProductDTO = ProductFactory.createProductDTO();
        invalidProductDTO.addCategories(null);

        String json = objectMapper.writeValueAsString(invalidProductDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("categories"));
        result.andExpect(jsonPath("$.errors[0].message").value("There must be at least one category"));

    }

    // Inserção de produto retorna 422 e mensagens customizadas com dados inválidos
    // quando logado como admin e campo price for zero
    @Test
    public void insertShouldReturnUnprocessableEntityAndCustomErrorsWhenProductPriceIsZeroAndAdminLogged() throws Exception {

        ProductDTO invalidProductDTO = ProductFactory.createProductDTO();
        invalidProductDTO.setPrice(0.0);

        String json = objectMapper.writeValueAsString(invalidProductDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("price"));
        result.andExpect(jsonPath("$.errors[0].message").value("Product price must be a positive value"));

    }

    // Inserção de produto retorna 422 e mensagens customizadas com dados inválidos
    // quando logado como admin e campo price for negativo
    @Test
    public void insertShouldReturnUnprocessableEntityAndCustomErrorsWhenNegativeProductPriceAndAdminLogged() throws Exception {

        ProductDTO invalidProductDTO = ProductFactory.createProductDTO();
        invalidProductDTO.setPrice(-20.0);

        String json = objectMapper.writeValueAsString(invalidProductDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("price"));

    }

    // Inserção de produto retorna 422 e mensagens customizadas
    // com dados inválidos quando logado como admin e campo description for inválido
    @Test
    public void insertShouldReturnUnprocessableEntityAndCustomErrorsWhenInvalidProductDescriptionAndAdminLogged() throws Exception {

        ProductDTO invalidProductDTO = ProductFactory.createProductDTO();
        invalidProductDTO.setDescription(" ");

        String json = objectMapper.writeValueAsString(invalidProductDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("description"));

    }

    // Inserção de produto retorna 422 e mensagens customizadas com
    // dados inválidos quando logado como admin e campo name for inválido
    @Test
    public void insertShouldReturnUnprocessableEntityAndCustomErrorsWhenInvaliProductNameAndAdminLogged() throws Exception {

        ProductDTO invalidProductDTO = ProductFactory.createProductDTO();
        invalidProductDTO.setName(" ");

        String json = objectMapper.writeValueAsString(invalidProductDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));

    }

    // Inserção de produto insere produto com dados válidos quando logado como admin
    @Test
    public void insertShouldReturnProductDTOWhenAdminLogged() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
    }

    @Test
    public void findAllShouldReturnPageProductMinDTO() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(25));
        result.andExpect(jsonPath("$.content[0].id").value(1));
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.content[0].price").value(90.5));
        result.andExpect(jsonPath("$.content[0].imgUrl").value(
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
    }

    @Test
    public void findAllShouldReturnPageProductMinDTOWhenFilteredByProductName() throws Exception {
        String productName = "Macbook";

        ResultActions result = mockMvc.perform(
                get("/products?name={productName}", productName)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(1));
        result.andExpect(jsonPath("$.content[0].id").value(3));
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[0].price").value(1250.0));
        result.andExpect(jsonPath("$.content[0].imgUrl").value(
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
    }
}
