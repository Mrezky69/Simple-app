package com.project.shop.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDTO {

    private Long id;
    private String itemsName;
    private String itemsCode;
    private Integer stock;
    private Integer price;
    private Integer isAvailable;
    private Date lastReStock;

}
