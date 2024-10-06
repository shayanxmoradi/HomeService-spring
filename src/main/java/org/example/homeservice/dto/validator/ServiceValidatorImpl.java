package org.example.homeservice.dto.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.homeservice.dto.ServiceRequest;

public class ServiceValidatorImpl implements ConstraintValidator<ServiceValidator, ServiceRequest> {

//    @Override
//    public boolean isValid(ServiceRequest serviceRequest, ConstraintValidatorContext context) {
//        if ((serviceRequest.isCategory()|| serviceRequest.isCategory()==null) && serviceRequest.basePrice() != null) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Category services cannot have a price")
//                    .addPropertyNode("basePrice")
//                    .addConstraintViolation();
//            return false;
//        }
//        return true;
//    }


    @Override
    public boolean isValid(ServiceRequest serviceRequest, ConstraintValidatorContext context) {
        if (serviceRequest == null) {
            return false;
        }

        // Check if it's a isCategory (true) and if basePrice is not null
        Boolean isCategory = serviceRequest.isCategory(); // This can be null
        if (Boolean.TRUE.equals(isCategory) && serviceRequest.basePrice() != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Category services cannot have a price")
                    .addPropertyNode("basePrice")
                    .addConstraintViolation();
            return false; // Return false since the validation failed
        }
        if (Boolean.FALSE.equals(isCategory) && serviceRequest.basePrice() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("real services should have base price")
                    .addPropertyNode("basePrice")
                    .addConstraintViolation();
            return false; // Return false since the validation failed
        }

        return true; // Return true if all validations passed
    }
}