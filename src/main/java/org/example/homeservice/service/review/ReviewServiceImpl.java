package org.example.homeservice.service.review;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.dto.mapper.ReviewMapper;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.dto.validator.ReviewRequest;
import org.example.homeservice.repository.ReviewRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepo reviewRepository;
    private final SpeciallistService speciallistService;
    private final SpecialistMapper specialistMapper;
    private final OrderService orderService;
    private final SpeciallistService service;
    private final ReviewMapper reviewMapper;

    @Transactional
    public void addReview(ReviewRequest review) {
        // Save the review
        System.out.println(review);
        Review review1 = new Review();
//    review1.setRating(review.rate());
//    review1.setComment(review.Comment());

        reviewRepository.save(reviewMapper.toReview(review));

        Long orderId = review.orderId();
        OrderResponse orderResponse = orderService.findById(orderId).orElseThrow(ValidationException::new);
//
        Long specialistId = orderResponse.chosenSpecialistId();
        System.out.println("xx"+specialistId);
//        SpecialistResponse specialistResponse = speciallistService.findById(specialistId).orElseThrow(ValidationException::new);
        updateSpecialistRate(specialistId, review.rating());
    }
@Transactional
    void updateSpecialistRate(Long specialistId, int rate) {
    System.out.println("rate " + rate);

    Specialist specialist = speciallistService.findByIdX(specialistId)
            .orElseThrow(() -> new ValidationException("Specialist does not exist"));

    Double pastRate = specialist.getRate() == null ? 0 : specialist.getRate();
    System.out.println("pastRate " + pastRate);

    int pastNumberOfRate = specialist.getNumberOfRate();
    System.out.println("pastNumberOfRate " + pastNumberOfRate);

    // Increment the number of ratings
    specialist.setNumberOfRate(pastNumberOfRate + 1);

    // Correct calculation of new average rate
    double newAverageRate = ((pastRate * pastNumberOfRate) + rate) / (pastNumberOfRate + 1);

    System.out.println("new score " + newAverageRate);

    // Update the specialist's rate
    specialist.setRate(newAverageRate);
    }
}
