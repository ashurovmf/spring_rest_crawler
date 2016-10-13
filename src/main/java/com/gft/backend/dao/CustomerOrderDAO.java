package com.gft.backend.dao;

import com.gft.backend.entities.CustomerOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by miav on 2016-10-13.
 */

public class CustomerOrderDAO extends AbstractDAOBean<CustomerOrder>{
    @PersistenceContext
    EntityManager em;

    public CustomerOrderDAO() {
        super(CustomerOrder.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
