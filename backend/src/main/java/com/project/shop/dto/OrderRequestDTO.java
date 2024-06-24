package com.project.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {
    @NotNull
    private String orderCode;

    @NotNull
    private Long customerId;

    @NotNull
    private Long itemId;

    private Long itemIdLama;
    private Integer quantityLama;

    @Min(value = 1)
    private Integer quantity;
}
