package org.example.homeservice.repository.user;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.domain.service.Order;
import org.example.homeservice.domain.service.Service;
import org.example.homeservice.domain.user.Specialist;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

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


    public static Specification<Specialist> filterByNumberOfOrders(Long count) {
        return (root, query, criteriaBuilder) -> {
            if (count == null) {
                return null;
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Order> orderRoot = subquery.from(Order.class);
            subquery.select(criteriaBuilder.count(orderRoot.get("id")));
            subquery.where(criteriaBuilder.equal(orderRoot.get("chosenSpecialist"), root));

            return criteriaBuilder.equal(subquery, count);
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
    public static Specification<Specialist> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            } else if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("registrationDate"), endDate);
            } else if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), startDate);
            } else {
                return criteriaBuilder.between(root.get("registrationDate"), startDate, endDate);
            }
        };
    }


    public static Specification<Specialist> filterByNumberOfNumber(Long count) {
        return (root, query, criteriaBuilder) -> {
            if (count == null) {
                return null;
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Offer> orderRoot = subquery.from(Offer.class);
            subquery.select(criteriaBuilder.count(orderRoot.get("id")));
            subquery.where(criteriaBuilder.equal(orderRoot.get("specialist"), root));

            return criteriaBuilder.equal(subquery, count);
        };
    }
}
