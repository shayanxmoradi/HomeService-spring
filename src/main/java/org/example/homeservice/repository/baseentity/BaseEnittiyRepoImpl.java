package org.example.homeservice.baseentity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.entites.BaseEntity;
import org.example.exceptions.NoEntityFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseEnittiyRepoImpl<T extends BaseEntity<ID>, ID extends Serializable> implements BaseEnitityRepo<T, ID> {
    public final EntityManager entityManager;

    protected BaseEnittiyRepoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> save(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<T> update(T entity) {
        entityManager.getTransaction().begin();

        entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean deleteByID(ID id) {
        try {
            entityManager.getTransaction().begin();
            T entity = entityManager.find(getEntityClass(), id);
            if (entity == null) {
                entityManager.getTransaction().rollback();
                System.err.println("Entity with ID " + id + " does not exist.");
                return false;
            }
            entityManager.remove(entityManager.find(getEntityClass(), id));
            entityManager.getTransaction().commit();
            return true;

        } catch (NoEntityFoundException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delelte(T eintity) {
        entityManager.getTransaction().begin();
        entityManager.remove(eintity);
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public Optional<T> findById(ID id) {

        return Optional.ofNullable(entityManager.find(getEntityClass(), id));

//            entityManager.getTransaction().begin();
//
//        T founded;
//        try {
//            founded = entityManager.find(getEntityClass(), id);
//        } finally {
//            entityManager.close();
//        }
//        entityManager.getTransaction().commit();
//        return founded;
    }

    @Override
    public Optional<List<T>> findAll() {
        // entityManager.getTransaction().begin();
        TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClass().getName() + " e", getEntityClass());

        return Optional.ofNullable(query.getResultList());
    }

    public abstract Class<T> getEntityClass();

    public Optional<List<T>> findWithAttribute(Class<T> clazz, String attributeName, Object attributeValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        // Create dynamic condition for the attribute
        Predicate predicate = cb.equal(root.get(attributeName), attributeValue);

        cq.where(predicate);
        return Optional.ofNullable(entityManager.createQuery(cq).getResultList());
    }

    public <T> boolean existsWithAttribute(Class<T> clazz, String attributeName, Object attributeValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(clazz);

        // Create dynamic condition for the attribute
        Predicate predicate = cb.equal(root.get(attributeName), attributeValue);

        // Select count instead of the whole entity
        cq.select(cb.count(root)).where(predicate);

        // Return true if any result exists, false otherwise
        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }
}

