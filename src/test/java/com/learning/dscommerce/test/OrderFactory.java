package com.learning.dscommerce.test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.DTO.ClientDTO;
import com.learning.dscommerce.DTO.OrderDTO;
import com.learning.dscommerce.DTO.OrderItemDTO;
import com.learning.dscommerce.DTO.PaymentDTO;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.Payment;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.entities.enums.OrderStatus;

public class OrderFactory {



    public static Order createOrder(User user) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user, new Payment());
        Product Product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(Product, order, 1, 500.0);
        order.getItems().add(orderItem);
        return order;
    }

    public static OrderDTO createOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(order.getClient().getId());
        clientDTO.setName(order.getClient().getName());
        orderDTO.setClient(clientDTO);
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(order.getProducts().get(0).getDescription());
        productDTO.setId(order.getProducts().get(0).getId());
        productDTO.setImgUrl(order.getProducts().get(0).getImgUrl());
        productDTO.setName(order.getProducts().get(0).getName());
        productDTO.setPrice(order.getProducts().get(0).getPrice());
        List<CategoryDTO> categoriesDTO = new ArrayList<>();
        for(Category cat : order.getProducts().get(0).getCategories()){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(cat.getId());
            categoryDTO.setName(cat.getName());
            categoriesDTO.add(categoryDTO);
        }
        productDTO.addCategories(categoriesDTO);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setImgUlr(productDTO.getImgUrl());
        orderItemDTO.setName(productDTO.getName());
        orderItemDTO.setPrice(productDTO.getPrice());
        orderItemDTO.setProductId(productDTO.getId());
        orderItemDTO.setQuantity(10);
        orderDTO.setItems(List.of(orderItemDTO));
        orderDTO.setId(order.getId());
        orderDTO.setMoment(order.getMoment());
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(order.getPayment().getId());
        paymentDTO.setMoment(order.getPayment().getMoment());
        orderDTO.setPayment(paymentDTO);
        orderDTO.setStatus(order.getStatus());

        return orderDTO;
    }

    public static OrderDTO createOrderDTO(Order order, User user) {
        OrderDTO orderDTO = new OrderDTO();
        
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(user.getId());
        clientDTO.setName(user.getName());
        orderDTO.setClient(clientDTO);
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(order.getProducts().get(0).getDescription());
        productDTO.setId(order.getProducts().get(0).getId());
        productDTO.setImgUrl(order.getProducts().get(0).getImgUrl());
        productDTO.setName(order.getProducts().get(0).getName());
        productDTO.setPrice(order.getProducts().get(0).getPrice());
        List<CategoryDTO> categoriesDTO = new ArrayList<>();
        for(Category cat : order.getProducts().get(0).getCategories()){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(cat.getId());
            categoryDTO.setName(cat.getName());
            categoriesDTO.add(categoryDTO);
        }
        productDTO.addCategories(categoriesDTO);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setImgUlr(productDTO.getImgUrl());
        orderItemDTO.setName(productDTO.getName());
        orderItemDTO.setPrice(productDTO.getPrice());
        orderItemDTO.setProductId(productDTO.getId());
        orderItemDTO.setQuantity(10);
        orderDTO.setItems(List.of(orderItemDTO));
        orderDTO.setId(order.getId());
        orderDTO.setMoment(order.getMoment());
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(order.getPayment().getId());
        paymentDTO.setMoment(order.getPayment().getMoment());
        orderDTO.setPayment(paymentDTO);
        orderDTO.setStatus(order.getStatus());

        return orderDTO;
    }

}
