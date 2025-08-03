package org.example.homeservice.repository.user;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.homeservice.domain.user.Customer;
import org.example.homeservice.domain.service.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    public static Specification<Customer> filterByFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("firstName"), firstName);
        };
    }

    public static Specification<Customer> filterByLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("lastName"), lastName);
        };
    }


//    public static Specification<Customer> filterByNumberOfOrders(Integer orderCount) {
//        return (root, query, criteriaBuilder) -> {
//            if (orderCount == null) {
//                return null;
//            }
//
//            // Join with Order table
//            Join<Customer, Order> orders = root.join("orders", JoinType.LEFT);
//
//            // Group by customer ID and count orders
//            query.groupBy(root.get("id"));
//            query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(orders.get("id")), orderCount.longValue()));
//
//            return query.getRestriction();
//        };
//    }

    public static Specification<Customer> filterByNumberOfOrders(Long count) {
        return (root, query, criteriaBuilder) -> {
            if (count == null) {
                return null;
            }

            // Subquery to count orders per customer
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Order> orderRoot = subquery.from(Order.class);
            subquery.select(criteriaBuilder.count(orderRoot.get("id")));
            subquery.where(criteriaBuilder.equal(orderRoot.get("customer"), root));

            // Main query: filter customers based on order count
            return criteriaBuilder.equal(subquery, count);
        };
    }

    public static Specification<Customer> filterByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), email);
        };
    }

    public static Specification<Customer> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
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

}
