package com.learning.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.repositories.OrderItemRepository;
import com.learning.dscommerce.repositories.OrderRepository;
import com.learning.dscommerce.repositories.ProductRepository;
import com.learning.dscommerce.repositories.UserRepository;
import com.learning.dscommerce.services.exceptions.ForbiddenException;
import com.learning.dscommerce.services.exceptions.ResourceNotFoundException;
import com.learning.dscommerce.test.OrderFactory;
import com.learning.dscommerce.test.ProductFactory;
import com.learning.dscommerce.test.UserFactory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
    
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EntityMapperService entityMapperService;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    private User userAdmin, userClient;
    private Order order;
    private OrderDTO orderDTO;
    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Product product;
    private ProductDTO productDTO;
    private OrderItem orderItem;

    
    @BeforeEach
    void setup () throws Exception {
        userAdmin = UserFactory.createUserAdmin();
        userClient = UserFactory.createUserClient();
        order = OrderFactory.createOrder(userAdmin);
        orderDTO = OrderFactory.createOrderDTO(order);
        existingOrderId = 1L;
        nonExistingOrderId = 2L;
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO(product);
        existingProductId = 1L;
        nonExistingProductId = 2L;
        orderItem = new OrderItem(product, order, 10, 20.0);

        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
        Mockito.when(entityMapperService.orderToOrderDto(order)).thenReturn(orderDTO);
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(anySet())).thenReturn(List.of(orderItem));
    }

    @Test
    public void insertShouldThrowEntityNotFoundExceptionWhenProductIdDoesNotExist(){
        Mockito.when(userService.authenticated()).thenReturn(userClient);
        orderDTO.getItems().get(0).setProductId(nonExistingProductId);
        Assertions.assertThrows(EntityNotFoundException.class, ()->{
            orderService.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){
        Mockito.when(userService.authenticated()).thenThrow(UsernameNotFoundException.class);
        order.setClient(new User());
        OrderDTO orderDTO = OrderFactory.createOrderDTO(order);
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            orderService.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenUserClientAuthenticated(){
        Mockito.when(userService.authenticated()).thenReturn(userClient);
        Order clientOrder = OrderFactory.createOrder(userClient);
        OrderDTO clientOrderDTO = OrderFactory.createOrderDTO(clientOrder);
        Mockito.when(entityMapperService.orderToOrderDto(clientOrder)).thenReturn(clientOrderDTO);
        OrderDTO result = orderService.insert(clientOrderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getClient(), clientOrderDTO.getClient());
        Mockito.verify(productRepository).getReferenceById(existingProductId);
        Mockito.verify(orderRepository).save(any());
        Mockito.verify(orderItemRepository).saveAll(Set.of(orderItem));
    }

    @Test
    public void insertShouldReturnOrderDTOWhenUserAdminAuthenticated() throws Exception{
       
        Mockito.when(userService.authenticated()).thenReturn(userAdmin);
        OrderDTO result = orderService.insert(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getClient(), orderDTO.getClient());
        Mockito.verify(productRepository).getReferenceById(existingProductId);
        Mockito.verify(orderRepository).save(any());
        Mockito.verify(orderItemRepository).saveAll(Set.of(orderItem));

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExsitsAndAdminLogged(){
    
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExsitSelfClientLogged(){
    
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenOrdeIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            orderService.findById(nonExistingOrderId);
        });
    }


    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenOtherClientLogged(){

        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () ->{
            orderService.findById(existingOrderId);
        });
    }
}
