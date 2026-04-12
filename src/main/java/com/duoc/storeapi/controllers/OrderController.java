package com.duoc.storeapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.storeapi.dto.OrderRequest;
import com.duoc.storeapi.models.Order;
import com.duoc.storeapi.models.enums.OrderStatusEnum;
import com.duoc.storeapi.services.OrderService;
import com.duoc.storeapi.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<List<Order>> getOrders(@RequestHeader(value="X-User-Role", required=false) String role) {
    AuthUtil.requireAdminRole(role);
      return ResponseEntity.ok(orderService.findAll());
  }
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
    AuthUtil.requireAdminOrOwner(headerUserId, role, userId);
    return ResponseEntity.ok(orderService.findByUserId(userId));
  }
  
  @GetMapping("/status")
  public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam OrderStatusEnum value, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
    AuthUtil.requireAdminOrOwner(headerUserId, role, headerUserId);
    return ResponseEntity.ok(orderService.findByStatus(value));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
    Order order = orderService.findById(id);
    AuthUtil.requireAdminOrOwner(headerUserId, role, order.getUser().getId());
    return ResponseEntity.ok(order);
  }
@PostMapping
public ResponseEntity<Order> createOrder(
        @Valid @RequestBody OrderRequest request, 
        @RequestHeader(value="X-User-Role", required=false) String role, 
        @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
    
    AuthUtil.requireAuthedUser(headerUserId, role);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request, headerUserId));
}
  
}
