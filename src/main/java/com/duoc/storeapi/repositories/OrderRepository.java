package com.duoc.storeapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.storeapi.models.Order;
import com.duoc.storeapi.models.enums.OrderStatusEnum;


public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserId(Long userId);
  List<Order> findByStatus(OrderStatusEnum status);
}
