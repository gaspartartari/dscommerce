package com.learning.dscommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.DTO.OrderItemDTO;
import com.learning.dscommerce.DTO.PaymentDTO;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.DTO.ProductMinDTO;
import com.learning.dscommerce.DTO.UserDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.Payment;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.entities.User;

@Service
public class EntityMapperService {
    
    private final ModelMapper modelMapper;

    public EntityMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureProductMappings();
        configureUserMappings();
        configureOrderMappings();
        configurePaymentMappings();
        configureOrderItemMappings();
    }

    private void configureOrderItemMappings() {
        TypeMap<OrderItem, OrderItemDTO> orderItemTypeMap = modelMapper.createTypeMap(OrderItem.class, OrderItemDTO.class);
        orderItemTypeMap.addMapping(x -> x.getProduct().getName(), OrderItemDTO :: setName);
        orderItemTypeMap.addMapping(x -> x.getProduct().getImgUrl(), OrderItemDTO :: setImgUlr);
    }

    private void configurePaymentMappings() {
        TypeMap<Payment, PaymentDTO> paymentTypeMap = modelMapper.createTypeMap(Payment.class, PaymentDTO.class);
        paymentTypeMap.addMapping(x -> x.getMoment(), PaymentDTO :: setMoment);
    }

    private void configureOrderMappings() {
        TypeMap<Order, OrderDTO> orderTypeMap = modelMapper.createTypeMap(Order.class, OrderDTO.class);
        orderTypeMap.addMapping(x -> x.getMoment(), OrderDTO :: setMoment);
    }

    private void configureUserMappings() {
        TypeMap<User, UserDTO> userTypeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        userTypeMap.addMapping(x -> x.getAuthorities(), UserDTO :: addAuthorities);
    }

    private void configureProductMappings() {
        TypeMap<Product, ProductDTO> productTypeMap = modelMapper.createTypeMap(Product.class, ProductDTO.class);
        productTypeMap.addMapping(x -> x.getCategories(), ProductDTO :: addCategories);
    }

    public ProductMinDTO productToProductMinDto(Product product){
        return modelMapper.map(product, ProductMinDTO.class);
    }

    public ProductDTO productToProductDto(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    public UserDTO userToUserDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    public PaymentDTO paymentToPaymentDto(Payment payment){
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public OrderItemDTO orderItemToOrderItemDto(OrderItem orderItem){
        return modelMapper.map(orderItem, OrderItemDTO.class);
    }

    public OrderDTO orderToOrderDto(Order order){
        return modelMapper.map(order, OrderDTO.class);
    }

    public CategoryDTO categoryToCategoryDTO(Category categorie){
        return modelMapper.map(categorie, CategoryDTO.class);
    }
}
