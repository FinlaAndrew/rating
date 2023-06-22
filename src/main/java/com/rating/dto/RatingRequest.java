package com.rating.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {

    private Long itemId;
    private String feedback;
    private int rating;
    private Long userId;

}
