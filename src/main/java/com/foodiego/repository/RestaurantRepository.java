package com.foodiego.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodiego.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{

}
