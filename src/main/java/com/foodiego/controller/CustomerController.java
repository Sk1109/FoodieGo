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
import com.foodiego.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customer")
	public ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer){
		return new ResponseEntity<ResponseStructure<Customer>>(customerService.createCustomer(customer), HttpStatus.CREATED);
	}
	@GetMapping("/customer")
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers(){
		return new ResponseEntity<ResponseStructure<List<Customer>>>(customerService.getAllCustomers(), HttpStatus.OK);
	}
	@GetMapping("/customer/{id}")
	public ResponseEntity<ResponseStructure<Customer>> findById(@PathVariable Integer id){
		return new ResponseEntity<ResponseStructure<Customer>>(customerService.getCustomerById(id) , HttpStatus.OK);
	}
	@PatchMapping("/customer/{id}")
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@PathVariable Integer id, @RequestBody Map<String, Object> data){
		return new ResponseEntity<ResponseStructure<Customer>>(customerService.updateCustomer(id, data), HttpStatus.OK);
	}
}
