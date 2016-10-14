package com.gft.backend.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

/**
 * Created by miav on 2016-10-13.
 */
@Repository
public abstract class AbstractDAOBean<T> {
    private Class<T> entityClass;

    public AbstractDAOBean(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    @Transactional
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    @Transactional
    public void createCollection(Collection<T> entities) {
        for(T entity : entities) {
            getEntityManager().persist(entity);
        }
    }

    @Transactional
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    @Transactional
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    @Transactional
    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public T findWithLazy(Object id, String... fields) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        for (String field : fields) {
            root.fetch(field);
        }

        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

        TypedQuery<T> typedQuery = getEntityManager().createQuery(criteriaQuery);

        return typedQuery.getSingleResult();
    }

    public List<T> findAllWithLazy(String... fields) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        for (String field : fields) {
            root.fetch(field);
        }

        criteriaQuery.select(root);

        TypedQuery<T> typedQuery = getEntityManager().createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}
