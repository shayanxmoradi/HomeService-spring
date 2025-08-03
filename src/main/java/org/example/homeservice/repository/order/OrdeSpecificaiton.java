package org.example.homeservice.repository.order;

import org.example.homeservice.domain.service.Order;
import org.example.homeservice.domain.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrdeSpecificaiton {
    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasService(Long serviceId) {
        return (root, query, criteriaBuilder) ->
                serviceId == null ? null : criteriaBuilder.equal(root.get("choosenService").get("id"), serviceId);
    }

    public static Specification<Order> hasSubservice(Long subserviceId) {
        return (root, query, criteriaBuilder) ->
                subserviceId == null ? null : criteriaBuilder.equal(root.get("subservice").get("id"), subserviceId);
    }

    public static Specification<Order> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            } else if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("serviceTime"), endDate);
            } else if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("serviceTime"), startDate);
            } else {
                return criteriaBuilder.between(root.get("serviceTime"), startDate, endDate);
            }
        };
    }
}
