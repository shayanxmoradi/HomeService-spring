package org.example.homeservice.repository.user;

import org.example.homeservice.domain.Customer;
import org.springframework.stereotype.Repository;

@Repository("customerRepo")
public interface CustomerRepo extends BaseUserRepo<Customer>{

}
