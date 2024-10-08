package com.urbanchic.service;

import com.urbanchic.dto.ReviewDto;
import com.urbanchic.entity.Review;
import com.urbanchic.event.DeleteAllReviewsOfProductEvent;

import java.util.List;

public interface ReviewService {
    Review addReview(ReviewDto addReviewDto);

    Review updateReview(ReviewDto updateReviewDto, String reviewId);

    List<Review> getAllReviewOfProduct(String productId);

    Review getReviewOfProductByUser(String userId, String productId);

    String deleteReviewById(String reviewId);

    void deleteAllReviewByProductId(String sellerId);
}
