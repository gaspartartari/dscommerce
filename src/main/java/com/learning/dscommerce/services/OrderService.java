package com.learning.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.repositories.OrderRepository;
import com.learning.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order result = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        OrderDTO dto = new OrderDTO(result);
        return dto;
    }
}
