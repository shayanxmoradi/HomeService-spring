package org.example.homeservice.service.baseentity;


import org.example.homeservice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseEntityService<T extends BaseEntity<ID>, ID extends Serializable, D,RDTO> {

    Optional<RDTO> save(D dto);
    Optional<RDTO> update(D dto);
    boolean delete(T entity);
    boolean deleteById(ID id);
    Optional<RDTO> findById(ID id);
    Optional<List<RDTO>> findAll();
    boolean existsByAttribute(String attributeName, Object attributeValue);
}