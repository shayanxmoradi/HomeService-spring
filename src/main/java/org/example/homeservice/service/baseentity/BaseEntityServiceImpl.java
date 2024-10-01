package org.example.homeservice.service.baseentity;

import org.example.homeservice.entity.BaseEntity;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseEntityServiceImpl<T extends BaseEntity<ID>, ID extends Serializable, R extends BaseEnitityRepo<T, ID>, D,RDTO>
        implements BaseEntityService<T, ID, D,RDTO> {
    protected final R baseRepository;


    public BaseEntityServiceImpl(R baseRepository) {
        this.baseRepository = baseRepository;
    }

    protected T toEntity(D dto) {
        throw new UnsupportedOperationException("Conversion not implemented");
    }

    protected RDTO toDto(T entity) {
        throw new UnsupportedOperationException("Conversion not implemented");
    }

    @Override
    public Optional<RDTO> save(D dto) {
        T entity = toEntity(dto);
        return Optional.of(toDto(baseRepository.save(entity)));
    }

    @Override
    public Optional<RDTO> update(D dto) {
        T entity = toEntity(dto);
        return Optional.of(toDto(baseRepository.save(entity)));
    }

    @Override
    public boolean delete(T entity) {
        baseRepository.delete(entity);
        return true;
    }

    @Override
    public boolean deleteById(ID id) {
        baseRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<RDTO> findById(ID id) {

        return baseRepository.findById(id).map(this::toDto);
    }

    @Override
    public Optional<List<RDTO>> findAll() {
        List<T> entities = baseRepository.findAll();
        return Optional.of(entities.stream().map(this::toDto).toList());
    }


    @Override
    public boolean existsByAttribute(String attributeName, Object attributeValue) {
        return false;
    }
}