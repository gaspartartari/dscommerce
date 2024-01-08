package com.learning.dscommerce.DTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.learning.dscommerce.entities.Order;
import com.learning.dscommerce.entities.OrderItem;
import com.learning.dscommerce.entities.enums.OrderStatus;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus status;

    private ClientDTO client;
    private PaymentDTO payment;

    @NotEmpty(message = "There must be at least one item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Long id, Instant moment, OrderStatus status) {
        this.id = id;
        this.moment = moment;
        this.status = status;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMomment();
        status = entity.getStatus();
        client = new ClientDTO(entity.getClient());
        payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
        for (OrderItem item : entity.getItems()){
            items.add(new OrderItemDTO(item));
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public Double getTotal(){
        Double sum = 0.0;
        for(OrderItemDTO item : items){
            sum += item.getSubTotal();
        }
        return sum;
    }  
}
