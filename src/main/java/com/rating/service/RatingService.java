package com.rating.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.rating.dto.Fooditem;
import com.rating.dto.RatingFeedback;
import com.rating.dto.User;
import com.rating.exception.CartExistsException;
import com.rating.exception.RatingNotFoundException;
import org.aspectj.lang.reflect.NoSuchAdviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rating.model.Rating;
import com.rating.repository.RatingRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {
    @Autowired
    private  RatingRepository ratingRepository;

    @Autowired
    private RestTemplate restTemplate;


    public User fetchUserDetails(Long userId) {
        String userMicroserviceUrl = "http://localhost:9001/users/{userId}";
        ResponseEntity<User> response = restTemplate.exchange(userMicroserviceUrl, HttpMethod.GET, null, User.class, userId);
        return response.getBody();
    }

    public Fooditem fetchItemDetails(Long itemId) {
        String itemMicroserviceUrl = "http://localhost:9002/items/{itemId}";
        ResponseEntity<Fooditem> response = restTemplate.exchange(itemMicroserviceUrl, HttpMethod.GET, null, Fooditem.class, itemId);
        return response.getBody();
    }
    public Rating saveRating(Rating rating) {
        // Fetch user details from user microservice
        User user = fetchUserDetails(rating.getUserId());

        // Fetch course details from course microservice
        Fooditem fooditem = fetchItemDetails(rating.getItemId());
        if (ratingRepository.existsByItemIdAndUserId(fooditem.getId(), user.getId())){
            throw new CartExistsException("Enrollment already exists for ItemId: " + fooditem.getId() +
                    " and userId: " + user.getId());
        }



        //rating.setDate(LocalDate.now());


        // Save the enrollment in the enrollment microservice
        Rating savedRating = ratingRepository.save(rating);

        return savedRating;
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(Long id) throws NoSuchAdviceException {
        return ratingRepository.findById(id).orElseThrow(()-> new NoSuchAdviceException("Rating not found"));
    }
    
    public Rating updateRating(Long id, Rating updatedRating) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating not found"));

//        rating.setCourseId(updatedRating.getCourseId());
        rating.setFeedback(updatedRating.getFeedback());
        rating.setRating(updatedRating.getRating());
//        rating.setUserId(updatedRating.getUserId());

        return ratingRepository.save(rating);
    }
    public List<RatingFeedback> getRatingsAndFeedbackByItemId(Long itemId) {
        List<Rating> ratings = ratingRepository.findByItemId(itemId);
        List<RatingFeedback> ratingFeedbackList = new ArrayList<>();

        for (Rating rating : ratings) {
            RatingFeedback ratingFeedback = new RatingFeedback();
            ratingFeedback.setRating(rating.getRating());
            ratingFeedback.setFeedback(rating.getFeedback());
            ratingFeedbackList.add(ratingFeedback);
        }

        return ratingFeedbackList;
    }

    public RatingFeedback getRatingAndFeedbackForItemByUser(Long userId, Long ItemId) {
        Rating existingRating = ratingRepository.findByItemIdAndUserId(ItemId, userId);
        if (existingRating == null) {
            throw new RatingNotFoundException("Rating not found for userId: " + userId + " and ItemId: " + ItemId);
        }

        RatingFeedback ratingFeedbackResponse = new RatingFeedback();
        ratingFeedbackResponse.setRating(existingRating.getRating());
        ratingFeedbackResponse.setFeedback(existingRating.getFeedback());

        return ratingFeedbackResponse;
    }

//    public List<RatingFeedback> getRatingsAndFeedbackByItemId(Long itemId) {
//    }

//    public RatingFeedback getRatingAndFeedbackForItemByUser(Long userId, Long itemId) {
//    }
}
    




