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
import com.foodiego.entity.MenuItem;
import com.foodiego.service.MenuItemService;

@RestController
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;

	@PostMapping("/menuitem")
	public ResponseEntity<ResponseStructure<MenuItem>> addMenuItem(@RequestBody MenuItem menuItem) {
		return new ResponseEntity<>(menuItemService.addMenuItem(menuItem), HttpStatus.CREATED);
	}

	@GetMapping("/menuitem")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getAllMenuItem() {
		return new ResponseEntity<ResponseStructure<List<MenuItem>>>(menuItemService.getAllMenuItem(), HttpStatus.OK);

	}
	@GetMapping("/menuitem/{id}")
	public ResponseEntity<ResponseStructure<MenuItem>> getMenuItemById(@PathVariable Integer id) {
		return new ResponseEntity<ResponseStructure<MenuItem>>(menuItemService.getMenuItemById(id), HttpStatus.OK);
	}
	@PatchMapping("/menuitem/{id}")
	public ResponseEntity<ResponseStructure<MenuItem>> updatePriceAndAvailability(@PathVariable Integer id, Map<String, Object> data){
		return new ResponseEntity<ResponseStructure<MenuItem>>(menuItemService.updatePriceAndAvailability(id , data), HttpStatus.OK);
		
	}
	@GetMapping("/menuitem/sort")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> sortByPrice(){
		return new ResponseEntity<ResponseStructure<List<MenuItem>>>(menuItemService.sortByPrice(), HttpStatus.OK);
	}
	@GetMapping("/menuitem/name/{name}")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getMenuItemByName(@PathVariable String name) {
		return new ResponseEntity<ResponseStructure<List<MenuItem>>>(menuItemService.getMenuItemByName(name), HttpStatus.OK);
	}
}
