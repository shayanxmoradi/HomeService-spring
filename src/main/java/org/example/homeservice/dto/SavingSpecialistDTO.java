package org.example.dto;

import lombok.Data;
import org.example.entites.enums.SpecialistStatus;

import java.util.List;

@Data

public class SavingSpecialistDTO {
        private String firstName;  // Added field from BaseUser
        private String lastName;   // Added field from BaseUser
        private String email;      // Email should be here for validation
        private String password;   // Added field from BaseUser
        private SpecialistStatus specialistStatus;
        private Double rate;
        private List<Long> workServiceIds; // Assuming you want to link services by IDs
        private byte[] personalImage;

}
