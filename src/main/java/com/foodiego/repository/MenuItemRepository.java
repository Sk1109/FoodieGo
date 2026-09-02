package com.foodiego.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodiego.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer>{

	List<MenuItem> findByItemName(String name);
}
