package com.foodiego.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.foodiego.dto.ResponseStructure;
import com.foodiego.entity.Customer;
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
	
	
}
