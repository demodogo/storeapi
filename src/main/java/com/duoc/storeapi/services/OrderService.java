package com.duoc.storeapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.storeapi.dto.OrderDetailRequest;
import com.duoc.storeapi.dto.OrderRequest;
import com.duoc.storeapi.exceptions.ResourceNotFoundException;
import com.duoc.storeapi.models.Order;
import com.duoc.storeapi.models.OrderDetails;
import com.duoc.storeapi.models.Product;
import com.duoc.storeapi.models.User;
import com.duoc.storeapi.models.enums.OrderStatusEnum;
import com.duoc.storeapi.repositories.OrderDetailRepository;
import com.duoc.storeapi.repositories.OrderRepository;
import com.duoc.storeapi.repositories.ProductRepository;
import com.duoc.storeapi.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  public List<Order> findAll() {
    log.info("Buscando órdenes");
    return orderRepository.findAll();
  }

  public Order findById(Long id) {
    log.info("Buscando orden con id {}", id);
    return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
  }

  public List<Order> findByUserId(Long userId) {
    log.info("Buscando ordenes del usuario con id {}", userId);
    return orderRepository.findByUserId(userId);
  }

  public List<Order> findByStatus(OrderStatusEnum status) {
    log.info("Buscando ordenes con estado {}", status);
    return orderRepository.findByStatus(status);
  }

  @Transactional
  public Order create(OrderRequest request, Long userId) {
    log.info("Creando orden para usuario ID: {}", userId);
    
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    if (Boolean.FALSE.equals(user.getEnabled())) {
        throw new IllegalArgumentException("Usuario inactivo");
    }

    Order order = Order.builder()
            .user(user)
            .orderDate(LocalDate.now())
            .status(OrderStatusEnum.PENDING)
            .totalAmount(BigDecimal.ZERO)
            .build();
    
    order = orderRepository.save(order);
    
    BigDecimal total = BigDecimal.ZERO;
    List<OrderDetails> details = new ArrayList<>();

    for (OrderDetailRequest req : request.getProducts()) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + req.getProductId()));
        
        if (Boolean.FALSE.equals(product.getActive())) {
            throw new IllegalArgumentException("Producto inactivo: " + product.getName());
        }
        
        if (product.getStock() < req.getQty()) {
            throw new IllegalArgumentException("Stock insuficiente para: " + product.getName());
        }

        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(req.getQty()));
        
        OrderDetails detail = OrderDetails.builder()
                .order(order)
                .product(product)
                .qty(req.getQty())
                .unitPrice(product.getPrice())
                .subtotal(subtotal)
                .build();
        
        orderDetailRepository.save(detail);
        details.add(detail);
        total = total.add(subtotal);

        product.setStock(product.getStock() - req.getQty());
        productRepository.save(product);
    }

    order.setTotalAmount(total);
    order.setDetails(details);
    order.setStatus(OrderStatusEnum.COMPLETED);
    
    log.info("Orden creada exitosamente con ID: {}", order.getId());
    return orderRepository.save(order);
  }
}
