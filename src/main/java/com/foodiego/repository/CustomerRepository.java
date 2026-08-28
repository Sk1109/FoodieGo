package com.foodiego.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodiego.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	Boolean existsByEmail(String email);
	Boolean existsByContact(String contact);
	Optional<Customer> findByEmail(String email);
	Optional<Customer> findByContact(String contact);
	List<Customer> findByName(String name);
}
