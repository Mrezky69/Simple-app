package com.project.shop.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String code;

    @NotNull
    @Pattern(regexp = "\\d{10,13}", message = "Phone harus berisi angka dengan panjang 10 hingga 13 karakter.")
    private String phone;

    @NotNull
    private Integer isActive;

    @Lob
    private String pic;
}
