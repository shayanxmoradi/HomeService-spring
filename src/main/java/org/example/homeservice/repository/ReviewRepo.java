package org.example.homeservice.repository;

import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByOrder_ChosenSpecialist(Specialist specialist);
//    List<Review> findByOrderByOrder_ChosenSpecialistId(Long specialistId);

    @Query("SELECT r FROM Review r WHERE r.order.chosenSpecialist.id = :specialistId")
    List<Review> findRatingsBySpecialistId(@Param("specialistId") Long specialistId);
}
