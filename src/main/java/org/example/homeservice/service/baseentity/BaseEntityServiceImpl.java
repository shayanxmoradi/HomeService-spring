package org.example.homeservice.service;

import org.example.homeservice.baseentity.BaseEnitityRepo;
import org.example.homeservice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseEntityServiceImpl<T extends BaseEntity<ID>,ID extends Serializable,R extends BaseEnitityRepo<T,ID>> implements BaseEnitityService<T,ID> {
  public final R baseRepository;

    public BaseEntityServiceImpl(R baseRepo) {
        this.baseRepository = baseRepo;
    }

    @Override
    public Optional<T> save(T entity) {
        return baseRepository.save(entity);
    }

    @Override
    public Optional<T> update(T entity) {
        return baseRepository.update(entity);
    }

    @Override
    public boolean delete(T entity) {
        return baseRepository.delelte(entity);
    }

    @Override
    public boolean deleteById(ID id) {
        return baseRepository.deleteByID(id);
    }




    @Override
    public Optional<T> findById(ID id) {
        return baseRepository.findById(id);
    }

    @Override
    public Optional<List<T>> findAll() {
        return baseRepository.findAll();
    }
    public Optional<List<T>> findByAttribute(Class<T> clazz, String attributeName, Object attributeValue) {
        return baseRepository.findWithAttribute(clazz, attributeName, attributeValue);
    }

    @Override
    public boolean existisByAttribute(Class<T> clazz, String attributeName, Object attributeValue) {
        return baseRepository.existsWithAttribute( clazz,  attributeName, attributeValue);
    }
}
