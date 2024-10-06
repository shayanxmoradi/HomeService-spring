package org.example.homeservice.service.user.speciallist;


import org.example.homeservice.domain.Specialist;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.service.user.BaseUserService;

import java.util.List;

public interface SpeciallistService extends BaseUserService<Specialist, SpecialistRequest, SpecialistResponse> {
 byte[]  processImage(String imagePath);
 void retriveImageOfSpecialist(Long SpecialistId, String savingPath);
 //Optional<OrderResponse> getOrderOfSpecialist(Long SpecialistId);

 List<OrderResponse> getAvilableOrders(Long SpecialistId);
}
