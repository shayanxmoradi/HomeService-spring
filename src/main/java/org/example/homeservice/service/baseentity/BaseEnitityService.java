package org.example.homeservice.service;

import org.example.homeservice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseEnitityService<T extends BaseEntity<ID>, ID extends Serializable> {

    Optional<T> save(T entity);
    Optional<T> update(T entity);
    boolean delete(T entity);
    boolean deleteById(ID id);
    Optional<T> findById(ID id);
    Optional<List<T>> findAll();
    public Optional<List<T>> findByAttribute(Class<T> clazz, String attributeName, Object attributeValue);
    public boolean existisByAttribute(Class<T> clazz, String attributeName, Object attributeValue);


}
