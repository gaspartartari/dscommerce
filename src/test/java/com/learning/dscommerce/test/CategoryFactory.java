package com.learning.dscommerce.test;

import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.entities.Category;

public class CategoryFactory {
    
    public static Category createCategory(){
        Category category = new Category(1L, "Books");
        return category;
    }

    public static Category createCategory(Long id, String name){
        Category category = new Category(id, name);
        return category;
    }

    public static CategoryDTO createCategoryDto(){
        Category category = createCategory();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
