package org.example.homeservice.repository.baseuser;

import org.example.homeservice.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("customerRepo")
public interface CustomerRepo extends BaseUserRepo<Customer>{

}
