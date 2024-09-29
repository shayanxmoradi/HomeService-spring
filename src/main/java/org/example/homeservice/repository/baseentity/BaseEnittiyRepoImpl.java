package org.example.homeservice.repository.baseentity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.homeservice.entites.BaseEntity;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseEnittiyRepoImpl<T extends BaseEntity<ID>, ID extends Serializable>
        implements BaseEnitityRepo<T, ID> {



    @PersistenceContext
    private EntityManager entityManager;

  //  @Override
    public List<T> findWithAttribute(String attributeName, Object attributeValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());

        Predicate predicate = cb.equal(root.get(attributeName), attributeValue);
        cq.where(predicate);

        return entityManager.createQuery(cq).getResultList();
    }



    //@Override
    public boolean existsByAttribute(String attributeName, Object attributeValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(getEntityClass());

        Predicate predicate = cb.equal(root.get(attributeName), attributeValue);
        cq.select(cb.count(root)).where(predicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}

