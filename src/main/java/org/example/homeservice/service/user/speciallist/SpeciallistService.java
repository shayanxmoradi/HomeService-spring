package org.example.homeservice.service.user.speciallist;


import org.example.homeservice.dto.*;
import org.example.homeservice.entity.Specialist;
import org.example.homeservice.service.user.BaseUserService;

import java.util.Optional;

public interface SpeciallistService extends BaseUserService<Specialist, SpecialistRequest, SpecialistResponse> {
 byte[]  processImage(String imagePath);
 void retriveImageOfSpecialist(Long SpecialistId, String savingPath);
 //Optional<OrderResponse> getOrderOfSpecialist(Long SpecialistId);
}
