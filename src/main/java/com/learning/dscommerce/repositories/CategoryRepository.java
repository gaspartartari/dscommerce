package com.learning.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
