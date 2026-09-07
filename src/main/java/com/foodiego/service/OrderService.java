package com.foodiego.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
import com.foodiego.entity.MenuItem;
import com.foodiego.entity.Order;
import com.foodiego.entity.OrderItem;
import com.foodiego.entity.Restaurant;
import com.foodiego.enums.OrderStatus;
import com.foodiego.enums.PaymentStatus;
import com.foodiego.exception.AmountMismatchException;
import com.foodiego.exception.EmptyCartException;
import com.foodiego.exception.MinimumQuantityException;
import com.foodiego.exception.NAException;
import com.foodiego.exception.NoRecordException;
import com.foodiego.exception.OrderCancellationException;
import com.foodiego.exception.RecordNotFoundException;
import com.foodiego.repository.CustomerRepository;
import com.foodiego.repository.MenuItemRepository;
import com.foodiego.repository.OrderRespository;
import com.foodiego.repository.RestaurantRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRespository orderRespository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	public ResponseStructure<Order> placeOrder(Order order) {
		if (order.getCustomer() == null || order.getCustomer().getId() == null) {
			throw new RecordNotFoundException("Customer ID must be provided for the Order");
		}
		Optional<Customer> customerOpt = customerRepository.findById(order.getCustomer().getId());
		if (customerOpt.isEmpty()) {
			throw new RecordNotFoundException("Customer ID provided does not exist in Database!");
		}
		Customer customer = customerOpt.get();
		order.setCustomer(customer);
		
		if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
			throw new EmptyCartException("No Items in the order!");
		}
		Integer totalAmount = 0;
		for (OrderItem i : order.getOrderItems()) {

			if (i.getMenuItem() == null || i.getMenuItem().getItemId() == null) {
				throw new RecordNotFoundException("Menu Item must be provided for the Order Item");
			}

			Optional<MenuItem> opt = menuItemRepository.findById(i.getMenuItem().getItemId());
			if (opt.isEmpty()) {
				throw new RecordNotFoundException("No Menu Item found with the specified ID");
			}
			MenuItem menuItem = opt.get();
			if (!menuItem.isAvailability()) {
				throw new NAException("Item Not available: " + menuItem.getItemName());
			}
			if (i.getQuantity() == null || i.getQuantity() < 1) {

				throw new MinimumQuantityException("Quantity must be at least 1");
			}
			i.setMenuItem(menuItem);
			Integer subTotal = menuItem.getPrice() * i.getQuantity();
			i.setSubTotal(subTotal);
			i.setOrder(order);
			totalAmount += subTotal;

		}
		order.setTotalAmount(totalAmount);
		
		if (order.getPayment() == null) {

		    throw new RecordNotFoundException("Payment must be provided for the Order");
		}
		if (order.getPayment().getAmount() == null) {

		    throw new RecordNotFoundException("Payment amount must be provided");
		}
		if (order.getPayment().getPaymentMethod() == null) {
		    throw new RecordNotFoundException("Payment method must be provided");
		}
		if (!order.getPayment().getAmount().equals(order.getTotalAmount())) {
			throw new AmountMismatchException("Payment amount must be equal to Order total amount that is: " + order.getTotalAmount());
		}
		order.setStatus(OrderStatus.CONFIRMED);
		order.getPayment().setPaymentStatus(PaymentStatus.SUCCESS);

		Order savedOrder = orderRespository.save(order);

		ResponseStructure<Order> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("Order placed successfully!");
		res.setData(savedOrder);
		return res;

	}
	
	public ResponseStructure<List<Order>> getAllOrders() {

	    List<Order> orderList = orderRespository.findAll();

	    if (orderList.isEmpty()) {
	        throw new NoRecordException("There are no orders in the database.");
	    }
	    ResponseStructure<List<Order>> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("All Orders fetched successfully!");
	    res.setData(orderList);
	    return res;
	}
	
	public ResponseStructure<List<Order>> getAllOrdersOfCustomer(Integer customerId) {

	    Optional<Customer> opt = customerRepository.findById(customerId);

	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException("No Customer found with the specified ID");
	    }
	    List<Order> orderList = orderRespository.findByCustomer_Id(customerId);
	    if (orderList.isEmpty()) {
	        throw new NoRecordException("There are no orders placed by this customer.");
	    }
	    ResponseStructure<List<Order>> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("All Orders of the Customer fetched successfully!");
	    res.setData(orderList);
	    return res;
	}
	
	public ResponseStructure<Order> getOrderById(Integer orderId) {
	    Optional<Order> opt = orderRespository.findById(orderId);
	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException("No Order found with the specified ID");
	    }
	    ResponseStructure<Order> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Order fetched successfully!");
	    res.setData(opt.get());
	    return res;
	}
	
	public ResponseStructure<Order> updateOrderStatus(Integer orderId, OrderStatus status) {
	    Optional<Order> opt = orderRespository.findById(orderId);
	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException("No Order found with the specified ID");
	    }
	    if (status == null) {
	        throw new RecordNotFoundException("Order status must be provided");
	    }
	    Order order = opt.get();
	    order.setStatus(status);
	    Order updatedOrder = orderRespository.save(order);
	    ResponseStructure<Order> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Order status updated successfully!");
	    res.setData(updatedOrder);
	    return res;
	}
	
	public ResponseStructure<Order> cancelOrder(Integer orderId) {
	    Optional<Order> opt = orderRespository.findById(orderId);
	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException( "No Order found with the specified ID");
	    }
	    Order order = opt.get();
	    if (order.getStatus() == OrderStatus.PREPARING || order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED ) {
	        throw new OrderCancellationException("Order cannot be cancelled after preparation has started");
	    }
	    order.setStatus(OrderStatus.CANCELLED);
	    Order cancelledOrder = orderRespository.save(order);
	    ResponseStructure<Order> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Order cancelled successfully!");
	    res.setData(cancelledOrder);
	    return res;
	}
	
	public ResponseStructure<List<Order>> getOrdersByStatus(OrderStatus status) {
	    if (status == null) {
	        throw new RecordNotFoundException("Order status must be provided");
	    }
	    List<Order> orderList = orderRespository.findByStatus(status);
	    if (orderList.isEmpty()) {
	        throw new NoRecordException("No orders found with the specified status");
	    }
	    ResponseStructure<List<Order>> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Orders with the specified status fetched successfully!");
	    res.setData(orderList);
	    return res;
	}
	
	public ResponseStructure<List<Order>> getOrdersByDate(LocalDate date) {
	    LocalDateTime start = date.atStartOfDay();
	    LocalDateTime end = date.plusDays(1).atStartOfDay();
	    List<Order> orderList = orderRespository.findByOrderDateTimeBetween(start, end);
	    if (orderList.isEmpty()) {
	        throw new NoRecordException("No orders found for the specified date");
	    }
	    ResponseStructure<List<Order>> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Orders fetched successfully for the specified date!");
	    res.setData(orderList);
	    return res;
	}
	
	public ResponseStructure<List<Order>> getOrdersPlacedInRestaurant(Integer restaurantId) {
	    Optional<Restaurant> opt = restaurantRepository.findById(restaurantId);
	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException("No Restaurant found with the specified ID");
	    }
	    List<Order> orderList =orderRespository.findDistinctByOrderItems_MenuItem_Restaurant_Id(restaurantId);
	    if (orderList.isEmpty()) {
	        throw new NoRecordException("No orders found for the specified restaurant");
	    }
	    ResponseStructure<List<Order>> res = new ResponseStructure<>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Orders placed in the Restaurant fetched successfully!");
	    res.setData(orderList);
	    return res;
	}
	
	

}
