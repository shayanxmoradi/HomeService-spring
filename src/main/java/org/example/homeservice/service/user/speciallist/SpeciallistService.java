package org.example.homeservice.service.user.speciallist;


import org.example.homeservice.domain.Specialist;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.dto.review.SpecialistRateRespone;
import org.example.homeservice.service.user.BaseUserService;

import java.util.List;
import java.util.Optional;

public interface SpeciallistService extends BaseUserService<Specialist, SpecialistRequest, SpecialistResponse> {
    byte[] processImage(String imagePath);

    void retriveImageOfSpecialist(Long SpecialistId, String savingPath);
    //Optional<OrderResponse> getOrderOfSpecialist(Long SpecialistId);

    List<OrderResponse> getAvilableOrders(Long SpecialistId);

    Optional<SpecialistResponse> acceptSpecialist(Long specialistId);

    public List<Specialist> filterSpecialists(String name, String lastName, String email, String serviceName, String sortBy, boolean ascending);

    public Integer submitRating(Long specialsitId, Integer rate);

    public Optional<Specialist> findByIdX(Long specialistId);

    double showRating(Long specialistId);

    List<SpecialistRateRespone> showReviews(Long specialistId);

    Optional<SpecialistResponse> activateSpecialist(Long userId);
    Optional<SpecialistResponse> findByEmail(String email);

}
