package com.project.shop.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String orderCode;
    private Date orderDate;
    private Long totalPrice;
    private CustomerReponseDTO customer;
    private ItemResponseDTO item;
    private Integer quantity;

    public String getCustomerName() {
        return customer.getName();
    }

    public String getItemName() {
        return item.getItemsName();
    }
}
