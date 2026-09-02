package com.foodiego.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodiego.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	List<Restaurant> findByLocation(String location);

	List<Restaurant> findByName(String name);

	List<Restaurant> findByRatingGreaterThan(Integer rating);
	
	boolean existsById(Integer id);
}
