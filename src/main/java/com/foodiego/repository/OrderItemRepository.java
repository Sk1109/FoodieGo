package com.foodiego.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodiego.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}