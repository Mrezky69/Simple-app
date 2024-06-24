package com.project.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_price")
    private Long totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customersId;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items itemsId;

    @Column(name = "quantity")
    private Integer quantity;

}
