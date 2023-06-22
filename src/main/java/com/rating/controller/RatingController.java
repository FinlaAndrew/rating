package com.rating.controller;

import com.rating.dto.RatingFeedback;
import com.rating.exception.CartExistsException;
import com.rating.exception.ItemNotFoundException;
import com.rating.exception.RatingNotFoundException;
import com.rating.exception.UserNotFoundException;
import org.aspectj.lang.reflect.NoSuchAdviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rating.model.Rating;
import com.rating.service.RatingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratingservice")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Rating> saveRating(@RequestBody Rating rating) {
        try {


            Rating savedRating = ratingService.saveRating(rating);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
        } catch (UserNotFoundException | ItemNotFoundException | CartExistsException e) {
            // Handle any exceptions and return an appropriate error response


            // Return a generic error response if the exception type is not handled
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/getall")
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        try {
            Rating rating = ratingService.getRatingById(id);
            return ResponseEntity.ok(rating);
        } catch (NoSuchAdviceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<RatingFeedback>> getRatingsAndFeedbackByItemId(@PathVariable Long itemId) {
        List<RatingFeedback> ratingsAndFeedback = ratingService.getRatingsAndFeedbackByItemId(itemId);
        return ResponseEntity.ok(ratingsAndFeedback);
    }

    @GetMapping("/users/{userId}/items/{itemId}/rating-feedback")
    public ResponseEntity<RatingFeedback> getRatingAndFeedbackForItemByUser(@PathVariable Long userId, @PathVariable Long itemId) {
        try {
            RatingFeedback ratingFeedbackResponse = ratingService.getRatingAndFeedbackForItemByUser(userId, itemId);
            return ResponseEntity.ok(ratingFeedbackResponse);
        } catch (RatingNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @PutMapping("/update/{id}")
    public Rating updateRating(@PathVariable Long id, @RequestBody Rating rating) {
        return ratingService.updateRating(id, rating);
    }
}