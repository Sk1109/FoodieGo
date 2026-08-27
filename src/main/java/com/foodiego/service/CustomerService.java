package com.foodiego.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
import com.foodiego.exception.CustomerNotFoundException;
import com.foodiego.exception.DuplicateContactException;
import com.foodiego.exception.DuplicateEmailException;
import com.foodiego.exception.NoRecordException;
import com.foodiego.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public ResponseStructure<Customer> createCustomer(Customer customer) {
		if (customerRepository.existsByEmail(customer.getEmail())) {
			throw new DuplicateEmailException("Duplicate Email! Please enter unique email.");
		}
		if (customerRepository.existsByContact(customer.getContact())) {
			throw new DuplicateContactException("Duplicate Contact! Please enter unique contact.");
		}
		if (!customer.getContact().matches("\\d{10}")) {
			throw new DuplicateContactException("Contact must be of 10 digits.");
		} else {

			ResponseStructure<Customer> res = new ResponseStructure<Customer>();
			Customer cust = customerRepository.save(customer);
			res.setStatusCode(HttpStatus.CREATED.value());
			res.setMessage("Customer successfully added in Database..!!");
			res.setData(cust);
			return res;
		}
	}

	public ResponseStructure<List<Customer>> getAllCustomers() {
		ResponseStructure<List<Customer>> res = new ResponseStructure<List<Customer>>();
		List<Customer> custList = customerRepository.findAll();
		if (custList.size() == 0) {
			throw new NoRecordException("No Customer records in the database!!");
		} else {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("All Customers fetched..!!");
			res.setData(custList);
			return res;
		}

	}

	public ResponseStructure<Customer> getCustomerById(Integer id) {

		Optional<Customer> opt = customerRepository.findById(id);
		if (opt.isEmpty()) {
			throw new CustomerNotFoundException("No customer found with the specified ID");
		} else {
			ResponseStructure<Customer> res = new ResponseStructure<>();
			Customer customer = opt.get();
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Customer fetched successfully.");
			res.setData(customer);
			return res;
		}

	}

	public ResponseStructure<Customer> updateCustomer(Integer id, Map<String, Object> data) {
		Optional<Customer> opt = customerRepository.findById(id);
		if (opt.isEmpty()) {
			throw new CustomerNotFoundException("No customer found with the specified ID");
		} else {
			Customer customer = opt.get();
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				switch (key) {
				case "name":
					customer.setName((String) value);
					break;
				case "email":
					String email = (String) value;
					if (email.equals(customer.getEmail()) || !customerRepository.existsByEmail(email)) {
						customer.setEmail(email);
						break;

					} else {
						throw new DuplicateEmailException("Duplicate Email! Please enter unique email.");
					}
				case "contact":
					String contact = (String) value;

					if (!contact.matches("\\d{10}")) {
						throw new DuplicateContactException("Contact must be of 10 digits.");
					}
					if (contact.equals(customer.getContact()) || !customerRepository.existsByContact(contact)) {
						customer.setContact(contact);
						break;
					} else {
						throw new DuplicateContactException("Duplicate Contact! Please enter unique contact.");
					}
				case "address":
					customer.setAddress((String) value);
					break;

				}
			}
			Customer updatedCustomer = customerRepository.save(customer);
			ResponseStructure<Customer> res = new ResponseStructure<>();
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Customer updated successfully.");
			res.setData(updatedCustomer);
			return res;
		}

	}
}
