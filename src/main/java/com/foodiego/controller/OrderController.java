package com.foodiego.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Order;
import com.foodiego.enums.OrderStatus;
import com.foodiego.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	 @PostMapping("/order")
	    public ResponseEntity<ResponseStructure<Order>> placeOrder(@RequestBody Order order) {
	        return new ResponseEntity<ResponseStructure<Order>>(orderService.placeOrder(order), HttpStatus.CREATED);
	    }
	 @GetMapping("/order")
	 public ResponseEntity<ResponseStructure<List<Order>>> getAllOrders() {
	     return new ResponseEntity<ResponseStructure<List<Order>>>(orderService.getAllOrders(),HttpStatus.OK);
	 }
	 @GetMapping("/order/customer/{customerId}")
	 public ResponseEntity<ResponseStructure<List<Order>>> getAllOrdersOfCustomer(@PathVariable Integer customerId) {
	     return new ResponseEntity<ResponseStructure<List<Order>>>(orderService.getAllOrdersOfCustomer(customerId),HttpStatus.OK);
	 }
	 @GetMapping("/order/{orderId}")
	 public ResponseEntity<ResponseStructure<Order>> getOrderById(@PathVariable Integer orderId) {
	     return new ResponseEntity<ResponseStructure<Order>>(orderService.getOrderById(orderId),HttpStatus.OK);
	 }
	 @PutMapping("/orders/status/{orderId}/{status}")
	 public ResponseEntity<ResponseStructure<Order>> updateOrderStatus(@PathVariable Integer orderId, @PathVariable OrderStatus status) {
	     return new ResponseEntity<ResponseStructure<Order>>(orderService.updateOrderStatus(orderId, status),HttpStatus.OK);
	 }
	 @PutMapping("/order/cancel/{orderId}")
	 public ResponseEntity<ResponseStructure<Order>> cancelOrder(@PathVariable Integer orderId) {
	     return new ResponseEntity<ResponseStructure<Order>>(orderService.cancelOrder(orderId),HttpStatus.OK);
	 }
	 @GetMapping("/order/status")
	 public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByStatus(@RequestParam OrderStatus status) {
	     return new ResponseEntity<ResponseStructure<List<Order>>>(orderService.getOrdersByStatus(status),HttpStatus.OK);
	 }
	 @GetMapping("/order/date")
	 public ResponseEntity<ResponseStructure<List<Order>>> getOrdersByDate(@RequestParam LocalDate date) {
	     return new ResponseEntity<ResponseStructure<List<Order>>>(orderService.getOrdersByDate(date),HttpStatus.OK);
	 }
	 @GetMapping("/order/restaurant/{restaurantId}")
	 public ResponseEntity<ResponseStructure<List<Order>>>getOrdersPlacedInRestaurant(@PathVariable Integer restaurantId) {
	     return new ResponseEntity<ResponseStructure<List<Order>>>(orderService.getOrdersPlacedInRestaurant(restaurantId),HttpStatus.OK);
	 }
}
