package com.duoc.storeapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.storeapi.models.OrderDetails;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
  List<OrderDetails> findByOrderId(Long orderId);
}
