package com.learning.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
