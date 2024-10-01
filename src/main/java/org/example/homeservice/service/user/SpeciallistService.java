package org.example.homeservice.service.user;


import org.example.homeservice.dto.*;
import org.example.homeservice.entity.Specialist;

public interface SpeciallistService extends BaseUserService<Specialist, SpecialistRequest, SpecialistResponse> {
 byte[]  processImage(String imagePath);
 void retriveImageOfSpecialist(Long SpecialistId, String savingPath);
}
