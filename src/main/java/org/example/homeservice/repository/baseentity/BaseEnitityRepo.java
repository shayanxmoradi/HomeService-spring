package org.example.homeservice.baseentity;

import org.example.homeservice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseEnitityRepo<T extends BaseEntity<ID>, ID extends Serializable> {

    Optional<T> save(T entity);
    Optional<T> update(T entity);
    boolean deleteByID(ID id);
    boolean delelte(T eintity);
    Optional<T> findById(ID id);
    public Optional<List<T>> findWithAttribute(Class<T> clazz, String attributeName, Object attributeValue);
    Optional<List<T>> findAll();

    public <T> boolean existsWithAttribute(Class<T> clazz, String attributeName, Object attributeValue);
}
