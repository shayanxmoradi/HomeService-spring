package org.example.homeservice.repository;

import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByOrder_ChosenSpecialist(Specialist specialist);
//    List<Review> findByOrderByOrder_ChosenSpecialistId(Long specialistId);

}
