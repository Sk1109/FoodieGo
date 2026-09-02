package com.foodiego.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
import com.foodiego.entity.MenuItem;
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
	public ResponseEntity<ResponseStructure<Restaurant>> getRestaurantById(@PathVariable Integer id) {
		return new ResponseEntity<ResponseStructure<Restaurant>>(restaurantService.getRestaurantById(id), HttpStatus.OK);
	}
	@PatchMapping("/restaurant/{id}")
	public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurant(@PathVariable Integer id,
			@RequestBody Map<String, Object> data) {
		return new ResponseEntity<ResponseStructure<Restaurant>>(restaurantService.updateRestaurant(id, data), HttpStatus.OK);
	}
	@DeleteMapping("/restaurant/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteRestaurant(@PathVariable Integer id) {
		return new ResponseEntity<ResponseStructure<String>>(restaurantService.deleteRestaurant(id), HttpStatus.OK);
	}
	@GetMapping("/restaurant/location/{location}")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByLocation(@PathVariable String location){
		return new ResponseEntity<ResponseStructure<List<Restaurant>>>(restaurantService.getByLocation(location), HttpStatus.OK);
	}
	@GetMapping("/restaurant/name/{name}")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByName(@PathVariable String name){
		return new ResponseEntity<ResponseStructure<List<Restaurant>>>(restaurantService.getByName(name), HttpStatus.OK);
	}
	@GetMapping("/restaurant/rating/{rating}")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByRatingGreaterThan(@PathVariable Integer rating){
		return new ResponseEntity<ResponseStructure<List<Restaurant>>>(restaurantService.getByRatingGreaterThan(rating),HttpStatus.OK);
	}
	@GetMapping("/restaurant/menu/{id}")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getMenuOfRestaurant(@PathVariable Integer id){
		return new ResponseEntity<ResponseStructure<List<MenuItem>>>(restaurantService.getMenuOfRestaurant(id), HttpStatus.OK);
	}

}
