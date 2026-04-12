package com.duoc.storeapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductSearchRequest {
  private String name;
  
  private String description;

  @Min(value = 0, message = "El precio mínimo no puede ser negativo")
  private BigDecimal minPrice;
  
  @Min(value = 0, message = "El precio máximo no puede ser negativo")
  private BigDecimal maxPrice;
  
  private Boolean active;
}
