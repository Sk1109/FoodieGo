package com.foodiego.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
import com.foodiego.entity.MenuItem;
import com.foodiego.entity.Restaurant;
import com.foodiego.exception.RecordNotFoundException;
import com.foodiego.exception.NoRecordException;
import com.foodiego.exception.RatingLimitException;
import com.foodiego.repository.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	public ResponseStructure<Restaurant> createRestauraunt(Restaurant restaurant) {
		if (restaurant.getRating() < 0 || restaurant.getRating() > 5) {
			throw new RatingLimitException("Rating can be between 0-5");
		}
		Restaurant r = restaurantRepository.save(restaurant);
		ResponseStructure<Restaurant> res = new ResponseStructure<Restaurant>();
		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("Restaurant added successfully!!");
		res.setData(r);
		return res;
	}

	public ResponseStructure<List<Restaurant>> getAllRestaurant() {
		List<Restaurant> restaurantList = restaurantRepository.findAll();
		if (restaurantList.size() == 0) {
			throw new NoRecordException("No records available in the Database!!");
		}
		ResponseStructure<List<Restaurant>> res = new ResponseStructure<List<Restaurant>>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All restaurants fetched!!");
		res.setData(restaurantList);
		return res;
	}

	public ResponseStructure<Restaurant> getRestaurantById(Integer id) {

		Optional<Restaurant> opt = restaurantRepository.findById(id);
		if (opt.isEmpty()) {
			throw new RecordNotFoundException("No restaurant found with the specified ID");
		} else {
			ResponseStructure<Restaurant> res = new ResponseStructure<>();
			Restaurant restaurant = opt.get();
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Restaurant fetched successfully.");
			res.setData(restaurant);
			return res;
		}

	}

	public ResponseStructure<Restaurant> updateRestaurant(Integer id, Map<String, Object> data) {
		Optional<Restaurant> opt = restaurantRepository.findById(id);
		if (opt.isEmpty()) {
			throw new RecordNotFoundException("No restaurant found with the specified ID");
		}
		Restaurant restaurant = opt.get();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			switch (key) {
			case "name":
				restaurant.setName((String) value);
				break;
			case "location":
				restaurant.setLocation((String) value);
				break;
			case "rating":
				Integer rating = (Integer) value;
				if (rating > 5 || rating < 0) {
					throw new RatingLimitException("Ratig can be between 1 -5 only!");
				}
				restaurant.setRating((Integer) value);
				break;

			}

		}
		Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
		ResponseStructure<Restaurant> res = new ResponseStructure<>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Restaurant updated successfully.");
		res.setData(updatedRestaurant);
		return res;

	}

	public ResponseStructure<String> deleteRestaurant(Integer id) {
		Optional<Restaurant> opt = restaurantRepository.findById(id);
		if (opt.isEmpty()) {
			throw new RecordNotFoundException("No restaurant found with the specified ID");
		}
		Restaurant rest = opt.get();
		restaurantRepository.delete(rest);
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Restaurant deleted successfully!!");
		res.setData("DELETED");
		return res;
	}
	public ResponseStructure<List<Restaurant>> getByLocation(String location){
		List<Restaurant> restaurantList = restaurantRepository.findByLocation(location);
		if(restaurantList.size()==0) {
			throw new RecordNotFoundException("No restaurant found with the specified Location!");
		}
		ResponseStructure<List<Restaurant>> res = new ResponseStructure<List<Restaurant>>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All restaurants fetched with the specified Location");
		res.setData(restaurantList);
		return res;
	}
	public ResponseStructure<List<Restaurant>> getByName(String name){
		List<Restaurant> restaurantList = restaurantRepository.findByName(name);
		if(restaurantList.size()==0) {
			throw new RecordNotFoundException("No restaurant found with the specified Name!");
		}
		ResponseStructure<List<Restaurant>> res = new ResponseStructure<List<Restaurant>>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All restaurants fetched with the specified Name");
		res.setData(restaurantList);
		return res;
	}
	public ResponseStructure<List<Restaurant>> getByRatingGreaterThan(Integer rating){
		if(rating>5 || rating <0) {
			throw new RatingLimitException("Rating can be between 0 to 5 only!");
		}
		List<Restaurant> restaurantList = restaurantRepository.findByRatingGreaterThan(rating);
		if(restaurantList.size()==0) {
			throw new RecordNotFoundException("No restaurant found above the specified rating!");
		}
		ResponseStructure<List<Restaurant>> res = new ResponseStructure<List<Restaurant>>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All restaurant above rating: " + rating + " fetched successfully!");
		res.setData(restaurantList);
		return res;
	}
	public ResponseStructure<List<MenuItem>> getMenuOfRestaurant(Integer id) {
	    Optional<Restaurant> opt = restaurantRepository.findById(id);
	    if (opt.isEmpty()) {
	        throw new RecordNotFoundException("No restaurant found with the specified ID");
	    }
	    Restaurant restaurant = opt.get();
	    List<MenuItem> menuList = restaurant.getMenuItems();

	    ResponseStructure<List<MenuItem>> res = new ResponseStructure<List<MenuItem>>();
	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Menu fetched successfully.");
	    res.setData(menuList);
	    return res;
	}
	

}
