package org.example.homeservice.repository.baseentity;

import org.example.homeservice.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

//@Primary
//@Repository
public interface BaseEnitityRepo<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {


}
