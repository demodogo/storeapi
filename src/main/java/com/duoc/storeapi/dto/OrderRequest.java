package com.duoc.storeapi.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
  @Valid
  @NotEmpty(message = "No se puede completar la compra sin productos")
  private List<OrderDetailRequest> products;
}
