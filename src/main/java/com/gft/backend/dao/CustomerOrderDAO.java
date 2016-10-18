package com.gft.backend.dao;

import com.gft.backend.entities.CustomerOrder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    @Transactional
    public List<CustomerOrder> findAllByUserName(String userName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CustomerOrder> cq = cb.createQuery(CustomerOrder.class);
        Root<CustomerOrder> orderRoot = cq.from(CustomerOrder.class);
        cq.select(orderRoot).where(cb.equal(orderRoot.get("userName"),userName));
        TypedQuery<CustomerOrder> q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }
}
