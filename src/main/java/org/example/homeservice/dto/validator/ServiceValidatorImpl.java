package org.example.homeservice.dto.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.homeservice.dto.ServiceRequest;

public class ServiceValidatorImpl implements ConstraintValidator<ServiceValidator, ServiceRequest> {

    @Override
    public boolean isValid(ServiceRequest serviceRequest, ConstraintValidatorContext context) {
        if (serviceRequest.category() && serviceRequest.basePrice() != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Category services cannot have a price")
                    .addPropertyNode("basePrice")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}