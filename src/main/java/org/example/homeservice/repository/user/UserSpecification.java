package org.example.homeservice.repository.user;

import org.example.homeservice.domain.Customer;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<Customer> filterByFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("firstName"), firstName);
        };
    }

    public static Specification<Customer> filterByLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("lastName"), lastName);
        };
    }

    public static Specification<Customer> filterByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }



}
