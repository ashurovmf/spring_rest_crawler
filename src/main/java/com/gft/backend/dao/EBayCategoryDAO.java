package com.gft.backend.dao;

import com.gft.backend.entities.EBayCategory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by miav on 2016-10-14.
 */
public class EBayCategoryDAO extends AbstractDAOBean<EBayCategory>{
    @PersistenceContext
    EntityManager em;

    public EBayCategoryDAO() {
        super(EBayCategory.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
