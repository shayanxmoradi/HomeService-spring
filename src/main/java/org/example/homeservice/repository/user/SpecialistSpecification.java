package org.example.homeservice.repository.user;

import jakarta.persistence.criteria.Join;
import org.example.homeservice.domain.Service;
import org.example.homeservice.domain.Specialist;
import org.springframework.data.jpa.domain.Specification;

public class SpecialistSpecification {

    public static Specification<Specialist> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("firstName"), "%" + name + "%");
        };
    }
    public static Specification<Specialist> filterByLastName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("lastName"), "%" + name + "%");
        };
    }



    public static Specification<Specialist> filterByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }

    public static Specification<Specialist> filterByServiceName(String serviceName) {
        return (root, query, criteriaBuilder) -> {
            if (serviceName == null || serviceName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Specialist, Service> join = root.join("workServices");
            return criteriaBuilder.like(join.get("name"), "%" + serviceName + "%");
        };
    }
}
