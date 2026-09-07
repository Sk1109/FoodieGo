package com.foodiego.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.MenuItem;
import com.foodiego.entity.Order;
import com.foodiego.entity.OrderItem;
import com.foodiego.enums.OrderStatus;
import com.foodiego.exception.MinimumQuantityException;
import com.foodiego.exception.NAException;
import com.foodiego.exception.NoRecordException;
import com.foodiego.exception.OrderModificationException;
import com.foodiego.exception.RecordNotFoundException;
import com.foodiego.repository.MenuItemRepository;
import com.foodiego.repository.OrderItemRepository;
import com.foodiego.repository.OrderRespository;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRespository orderRespository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	public ResponseStructure<OrderItem> addOrderItem(Integer orderId, OrderItem orderItem) {

	    Optional<Order> orderOpt = orderRespository.findById(orderId);
	    if (orderOpt.isEmpty()) {
	        throw new RecordNotFoundException("No Order found with the specified ID");
	    }
	    Order order = orderOpt.get();
	    if (order.getStatus() == OrderStatus.PREPARING || order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED ) {
	        throw new OrderModificationException("Order cannot be modified after preparation has started");
	    }
	    if (orderItem.getMenuItem() == null ||orderItem.getMenuItem().getItemId() == null) {
	    	throw new RecordNotFoundException("Menu Item must be provided for the Order Item");
	    }
	    Optional<MenuItem> menuItemOpt = menuItemRepository.findById(orderItem.getMenuItem().getItemId());
	    if (menuItemOpt.isEmpty()) {
	        throw new RecordNotFoundException("No Menu Item found with the specified ID");
	    }
	    MenuItem menuItem = menuItemOpt.get();
	    if (!menuItem.isAvailability()) {
	        throw new NAException( "Item Not available: " + menuItem.getItemName());
	    }
	    if (orderItem.getQuantity() == null || orderItem.getQuantity() < 1) {
	        throw new MinimumQuantityException("Quantity must be at least 1");
	    }
	    orderItem.setMenuItem(menuItem);
	    orderItem.setOrder(order);
	    Integer subTotal = menuItem.getPrice() * orderItem.getQuantity();
	    orderItem.setSubTotal(subTotal);

	    order.setTotalAmount(order.getTotalAmount() + subTotal);
	    OrderItem savedOrderItem = orderItemRepository.save(orderItem);
	    orderRespository.save(order);
	    ResponseStructure<OrderItem> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.CREATED.value());
	    res.setMessage("Order Item added successfully!");
	    res.setData(savedOrderItem);
	    return res;
	}
	
	public ResponseStructure<OrderItem> updateOrderItemQuantity(Integer orderId, Integer orderItemId, Integer qty){
		Optional<Order> orderOpt = orderRespository.findById(orderId);
	    if (orderOpt.isEmpty()) {
	        throw new RecordNotFoundException("No Order found with the specified ID");
	    }
	    Order order = orderOpt.get();
	    if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED ) {
	        throw new OrderModificationException("Order can be modified only when in preparing state.");
	    }
	    Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
	    if(orderItemOpt.isEmpty()) {
	    	throw new RecordNotFoundException("No OrderItem found with the specified ID: " + orderItemId);
	    }
	    OrderItem orderItem = orderItemOpt.get();
	    
	    if (orderItem.getQuantity().equals(qty) || qty < 1) {
	        throw new MinimumQuantityException("Quantity must be at least 1 and not same");
	    }
	    Integer oldSubTotal = orderItem.getSubTotal();
	    Integer newSubTotal = orderItem.getMenuItem().getPrice() * qty;

	    orderItem.setQuantity(qty);
	    orderItem.setSubTotal(newSubTotal);

	    Integer newTotal = order.getTotalAmount() - oldSubTotal + newSubTotal;
	    order.setTotalAmount(newTotal);
	    OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
	    orderRespository.save(order);
	    
	    ResponseStructure<OrderItem> res = new ResponseStructure<OrderItem>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("OrderItem Quantity updated successfully!");
	    res.setData(updatedOrderItem);
	    return res;
	    
	}
	public ResponseStructure<OrderItem> removeOrderItem(
	        Integer orderId, Integer orderItemId) {

	    Optional<Order> orderOpt = orderRespository.findById(orderId);

	    if (orderOpt.isEmpty()) {
	        throw new RecordNotFoundException(
	                "No Order found with the specified ID");
	    }

	    Order order = orderOpt.get();

	    if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
	        throw new OrderModificationException("OrderItem cannot be removed at this stage");
	    }
	    Optional<OrderItem> orderItemOpt =
	            orderItemRepository.findById(orderItemId);

	    if (orderItemOpt.isEmpty()) {
	        throw new RecordNotFoundException(
	                "No OrderItem found with the specified ID: " + orderItemId);
	    }

	    OrderItem orderItem = orderItemOpt.get();

	    if (!orderItem.getOrder().getOrderId().equals(orderId)) {
	        throw new RecordNotFoundException(
	                "OrderItem does not belong to the specified Order");
	    }

	    Integer subTotal = orderItem.getSubTotal();

	    orderItemRepository.delete(orderItem);

	    order.setTotalAmount(order.getTotalAmount() - subTotal);

	    orderRespository.save(order);

	    ResponseStructure<OrderItem> res = new ResponseStructure<OrderItem>();

	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("OrderItem removed successfully!");
	    res.setData(orderItem);
	    return res;
	}
	
	public ResponseStructure<List<OrderItem>> getOrderItemsByOrderId(Integer orderId) {

	    Optional<Order> orderOpt = orderRespository.findById(orderId);
	    if (orderOpt.isEmpty()) {
	        throw new RecordNotFoundException("No Order found with the specified ID");
	    }
	    List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(orderId);

	    if (orderItems.isEmpty()) {
	        throw new NoRecordException("No OrderItems found for the specified Order");
	    }
	    ResponseStructure<List<OrderItem>> res =new ResponseStructure<>();

	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("OrderItems fetched successfully!");
	    res.setData(orderItems);
	    return res;
	}
}
