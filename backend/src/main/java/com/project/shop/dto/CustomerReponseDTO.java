package com.project.shop.dto;

import jakarta.persistence.Lob;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReponseDTO {
    private Long id;
    private String name;
    private String address;
    private String code;
    private String phone;
    private Integer isActive;
    private Date lastOrder;

    @Lob
    private String pic;
}
