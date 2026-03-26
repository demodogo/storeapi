package com.duoc.storeapi.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(min = 2, max = 120, message = "El nombre del producto debe tener entre 2 y 120 caracteres")
    @Column(nullable = false, length = 120)
    private String name;

    @Size(max = 300, message = "La descripción del producto debe tener hasta 300 caracteres")
    @Column(length = 300)
    private String description;

    @NotNull(message = "El precio del producto es requerido")
    @Min(value = 0, message = "El precio del producto debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "El stock del producto es requerido")
    @Min(value = 0, message = "El stock del producto debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}