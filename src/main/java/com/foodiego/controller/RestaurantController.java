package com.foodiego.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
import com.foodiego.entity.Restaurant;
import com.foodiego.service.RestaurantService;

@RestController
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping("/restaurant")
	public ResponseEntity<ResponseStructure<Restaurant>> createRestaurant(@RequestBody Restaurant restaurant){
		return new ResponseEntity<ResponseStructure<Restaurant>>(restaurantService.createRestauraunt(restaurant),HttpStatus.CREATED);
	}
	@GetMapping("/restaurant")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getAllRestaurant(){
		return new ResponseEntity<ResponseStructure<List<Restaurant>>>(restaurantService.getAllRestaurant(), HttpStatus.OK);
	}
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<ResponseStructure<Restaurant>> findById(@PathVariable Integer id) {
		return new ResponseEntity<ResponseStructure<Restaurant>>(restaurantService.getRestaurantById(id), HttpStatus.OK);
	}
	@PatchMapping("/restaurant/{id}")
	public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurant(@PathVariable Integer id,
			@RequestBody Map<String, Object> data) {
		return new ResponseEntity<ResponseStructure<Restaurant>>(restaurantService.updateRestaurant(id, data), HttpStatus.OK);
	}
	
}
