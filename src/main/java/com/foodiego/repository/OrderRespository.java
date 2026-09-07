package com.foodiego.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.foodiego.entity.Order;
import com.foodiego.enums.OrderStatus;

@RestControllerAdvice
public interface OrderRespository extends JpaRepository<Order, Integer>{

	List<Order> findByCustomer_Id(Integer customerId);
	
	List<Order> findByStatus(OrderStatus status);
	
	List<Order> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);
	
	List<Order> findDistinctByOrderItems_MenuItem_Restaurant_Id(Integer restaurantId);

}
