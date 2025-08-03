package org.example.homeservice.repository.user;

import org.example.homeservice.domain.user.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("customerRepo")
public interface CustomerRepo extends JpaSpecificationExecutor<Customer>,BaseUserRepo<Customer> {

}
