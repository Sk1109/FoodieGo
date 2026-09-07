package com.foodiego.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.OrderItem;
import com.foodiego.service.OrderItemService;

@RestController
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("/orderitem/add/{orderId}")
	public ResponseEntity<ResponseStructure<OrderItem>> addOrderItem(@PathVariable Integer orderId, @RequestBody OrderItem orderItem) {
	    return new ResponseEntity<ResponseStructure<OrderItem>>(orderItemService.addOrderItem(orderId, orderItem),HttpStatus.CREATED);
	}
	@PutMapping("/orderitem/updateqty/{orderId}/{orderItemId}/{qty}")
	public ResponseEntity<ResponseStructure<OrderItem>> updateOrderItemQuantity(@PathVariable Integer orderId, @PathVariable Integer orderItemId,  @PathVariable Integer qty) {
	    return new ResponseEntity<>(orderItemService.updateOrderItemQuantity(orderId, orderItemId, qty), HttpStatus.OK);
	}
	@DeleteMapping("/orderitem/delete/{orderId}/{orderItemId}")
	public ResponseEntity<ResponseStructure<OrderItem>> removeOrderItem( @PathVariable Integer orderId,  @PathVariable Integer orderItemId) {
	    return new ResponseEntity<ResponseStructure<OrderItem>>(orderItemService.removeOrderItem(orderId, orderItemId),HttpStatus.OK);
	}
	@GetMapping("/orders/{orderId}/items")
	public ResponseEntity<ResponseStructure<List<OrderItem>>> getOrderItemsByOrderId( @PathVariable Integer orderId) {
	    return new ResponseEntity<ResponseStructure<List<OrderItem>>>(orderItemService.getOrderItemsByOrderId(orderId),HttpStatus.OK);
	}
}
