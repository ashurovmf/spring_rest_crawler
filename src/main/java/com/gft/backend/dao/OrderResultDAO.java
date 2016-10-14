package com.gft.backend.dao;

import com.gft.backend.entities.EBayCategory;
import com.gft.backend.entities.OrderResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
