package com.duoc.storeapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {
    @Size(min = 3, max = 50, message = "Nombre debe tener entre 3 y 50 caracteres")
    private String name;
    
    @Size(max = 300, message="La descripción debe tener hasta 300 caracteres")
    private String description;

    private BigDecimal price;

    private Integer stock;

    private Boolean active;
}

