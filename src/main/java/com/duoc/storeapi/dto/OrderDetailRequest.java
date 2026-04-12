package com.duoc.storeapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
  
  @NotNull(message = "El id del producto es requerido")
  private Long productId;

  @NotNull(message = "La cantidad es requerida")
  @Min(value = 1, message = "La cantidad debe ser mayor a 0")
  private Integer qty;
}
