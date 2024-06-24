package com.project.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
public class ItemRequestDTO {

    @NotNull
    private String itemsName;

    @NotNull
    private String itemsCode;

    @NotNull
    private Integer stock;

    @NotNull
    private Integer price;

    @NotNull
    private Integer isAvailable;
}
