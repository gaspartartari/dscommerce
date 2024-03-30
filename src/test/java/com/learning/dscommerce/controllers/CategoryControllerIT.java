package com.learning.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.learning.dscommerce.services.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerIT {
    
    @InjectMocks
    private CategoryController categoryController;
    
    @Mock
    private CategoryService categoryService;

    @Autowired  
    private MockMvc mockMvc;


    @BeforeEach
    void setup() throws Exception{

    }

    @Test
    public void findAllShouldReturnListOfCategoryDTO() throws Exception{

        ResultActions result = mockMvc.perform(get("/categories")
                    .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }
    
}
