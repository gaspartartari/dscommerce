package com.learning.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
