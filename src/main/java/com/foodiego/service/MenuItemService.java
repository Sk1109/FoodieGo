package com.foodiego.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.MenuItem;
import com.foodiego.exception.CannotUpdateException;
import com.foodiego.exception.NoRecordException;
import com.foodiego.exception.PriceException;
import com.foodiego.exception.RecordNotFoundException;
import com.foodiego.repository.MenuItemRepository;
import com.foodiego.repository.RestaurantRepository;

@Service
public class MenuItemService {

	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	public ResponseStructure<MenuItem> addMenuItem(MenuItem menuItem) {
		if (menuItem.getPrice() < 0) {
			throw new PriceException("Price of item cannot be in negative!");
		}
		if (menuItem.getRestaurant() == null || menuItem.getRestaurant().getId() == null) {
			throw new RecordNotFoundException("Restaurant must be provided for the Menu Item");
		}
		if (!restaurantRepository.existsById(menuItem.getRestaurant().getId())) {
			throw new RecordNotFoundException("Restaurant ID provided does not exists in Database!");
		}
		MenuItem m = menuItemRepository.save(menuItem);
		ResponseStructure<MenuItem> res = new ResponseStructure<>();
		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("Item added to the restaurant");
		res.setData(m);
		return res;
	}

	public ResponseStructure<List<MenuItem>> getAllMenuItem() {
		List<MenuItem> menuItemList = menuItemRepository.findAll();
		if (menuItemList.size() == 0) {
			throw new NoRecordException("There are no menu items in the database. ");
		}
		ResponseStructure<List<MenuItem>> res = new ResponseStructure<List<MenuItem>>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Menu Items fetched successfully!");
		res.setData(menuItemList);
		return res;
	}

	public ResponseStructure<MenuItem> getMenuItemById(Integer id) {
		Optional<MenuItem> opt = menuItemRepository.findById(id);
		if (opt.isEmpty()) {
			throw new RecordNotFoundException("No Menu Item found with the specified ID");
		} else {
			ResponseStructure<MenuItem> res = new ResponseStructure<>();
			MenuItem menuItem = opt.get();
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Menu Item fetched successfully.");
			res.setData(menuItem);
			return res;

		}

	}

	public ResponseStructure<MenuItem> updatePriceAndAvailability(Integer id, Map<String, Object> data) {
		Optional<MenuItem> opt = menuItemRepository.findById(id);
		if(opt.isEmpty()) {
			throw new RecordNotFoundException("No Menu Item found with the specified ID");
		}
		MenuItem menuItem = opt.get();
		for(Map.Entry<String, Object> entry : data.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			switch (key) {
			case "itemName" :
				throw new CannotUpdateException("Only Price and Availablity can be update!");
			case "price" :
				if((Double)value < 0) {
			        throw new PriceException("Price of item cannot be in negative!");
			    }
				menuItem.setPrice((Double)value);
				break;
			case "availability" :
				menuItem.setAvailability((Boolean)value);
				break;
			case "restaurant" :
				throw new CannotUpdateException("Only Price and Availablity can be update!");
			case "orderItems" :
				throw new CannotUpdateException("Only Price and Availablity can be update!");	
			}
		}
		MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
		ResponseStructure<MenuItem> res = new ResponseStructure<MenuItem>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Menu Item updated successfully!!");
		res.setData(updatedMenuItem);
		return res;
	}

	public ResponseStructure<List<MenuItem>> sortByPrice() {
		List<MenuItem> menuList = menuItemRepository.findAll(Sort.by("price").ascending());
		 if (menuList.isEmpty()) {
		        throw new NoRecordException("There are no menu items in the database.");
		    }
		 else {
			 ResponseStructure<List<MenuItem>> res = new ResponseStructure<>();
			    res.setStatusCode(HttpStatus.OK.value());
			    res.setMessage("Menu Items sorted by price successfully!");
			    res.setData(menuList);
			    return res;
	 
		 }
		 
	}

	public ResponseStructure<List<MenuItem>> getMenuItemByName(String name) {
		List<MenuItem> menuList = menuItemRepository.findByItemName(name);
		if(menuList.isEmpty()) {
			throw new RecordNotFoundException("No Menu Item found with the specified Name");
		}
		else {
			ResponseStructure<List<MenuItem>> res = new ResponseStructure<>();
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Menu Items with name : " + name + " fetched successfully.");
			res.setData(menuList);
			return res;

		}

	}
	
}
