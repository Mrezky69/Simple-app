package com.project.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "customer_code")
    private String code;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "last_order_date")
    private Date lastOrder;

    @Column(name = "pic")
    private String pic;

}
