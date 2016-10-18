package com.gft.backend.dao;

import com.gft.backend.entities.OrderResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by miav on 2016-10-14.
 */
public class OrderResultDAO extends AbstractDAOBean<OrderResult> {
    @PersistenceContext
    EntityManager em;

    public OrderResultDAO() {
        super(OrderResult.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
