package com.learning.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.DTO.ProductMinDTO;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.repositories.ProductRepository;
import com.learning.dscommerce.services.exceptions.DatabaseException;
import com.learning.dscommerce.services.exceptions.ResourceNotFoundException;
import com.learning.dscommerce.test.ProductFactory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EntityMapperService entityMapperService;

    private Product product;
    private List<Product> list;
    private ProductDTO productDTO;
    private ProductMinDTO productMinDTO;
    private Long existingId, nonExistingId, constreinedId;
    private Pageable pageable;
    private PageImpl page;
    
    

    @BeforeEach
    void setup() throws Exception{
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO();
        productMinDTO = ProductFactory.createProductMinDTO();
        list = new ArrayList<>();
        list.add(product);
        existingId = 1L;
        nonExistingId = 100L;
        constreinedId = 1000L;
        pageable = PageRequest.of(0, 12);
        page = new PageImpl<>(List.of(product));


        Mockito.when(entityMapperService.productToProductDto(product)).thenReturn(productDTO);
        Mockito.when(entityMapperService.productToProductMinDto(product)).thenReturn(productMinDTO);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(productRepository.searchByName(any(), (Pageable)any())).thenReturn(page);
        Mockito.when(productRepository.save((Product)any())).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(constreinedId)).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(constreinedId);

    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsConstrained(){

        Assertions.assertThrows(DatabaseException.class, () ->{
            productService.deleteById(constreinedId);
        });

        Mockito.verify(productRepository).existsById(constreinedId);
        Mockito.verify(productRepository).deleteById(constreinedId);
    }


    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            productService.deleteById(nonExistingId);
        });

        Mockito.verify(productRepository).existsById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExsitsAndNotConstrained(){

        productService.deleteById(existingId);

        Mockito.verify(productRepository).existsById(existingId);
        Mockito.verify(productRepository).deleteById(existingId);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            ProductDTO result = productService.update(nonExistingId, productDTO);
        });

        Mockito.verify(productRepository).getReferenceById(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), productDTO.getId());
        Assertions.assertEquals(result.getPrice(), productDTO.getPrice());
    }

    @Test
    public void insertShoulThrowDatabaseExceptionWhenObjectIsNull(){
        
        Assertions.assertThrows(DatabaseException.class, () -> {
            ProductDTO result = productService.insert(null);
        });

    }

    @Test
    public void insertShouldReturnProductDTO(){

        ProductDTO result = productService.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), productDTO.getId());
        Assertions.assertEquals(result.getName(), productDTO.getName());
    }

    @Test
    public void findAllShouldReturnPageOfProductMinDTO(){

        Page<ProductMinDTO> result = productService.findAll("Mac", pageable);

        Assertions.assertEquals(result.getContent().get(0).getId(), 1L);
        Assertions.assertEquals(result.getContent().get(0).getName(), "MacBook");
        Mockito.verify(productRepository).searchByName("Mac", pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = productService.findById(existingId);
        
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getPrice(), product.getPrice());
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = productService.findById(nonExistingId);
        });
        
        Mockito.verify(productRepository).findById(nonExistingId);
    }
}
