package com.learning.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.DTO.OrderItemDTO;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.entities.enums.OrderStatus;
import com.learning.dscommerce.repositories.OrderItemRepository;
import com.learning.dscommerce.repositories.OrderRepository;
import com.learning.dscommerce.repositories.ProductRepository;
import com.learning.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EntityMapperService entityMapperService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order result = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        authService.validateSelfOrAdmin(result.getClient().getId());
        return entityMapperService.orderToOrderDto(result);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setClient(userService.authenticated());
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        
        for(OrderItemDTO itemDTO : dto.getItems()){
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(product, order, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return entityMapperService.orderToOrderDto(order);
    }
}
