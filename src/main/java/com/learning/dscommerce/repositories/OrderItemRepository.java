package com.learning.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
    
}
