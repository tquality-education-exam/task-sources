package com.a1qa.common.utils;

import com.a1qa.common.QueryParam;
import com.a1qa.model.constants.Config;
import com.a1qa.model.domain.ABaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by p.ordenko on 11.05.2015, 18:05.
 * TODO Refactor to opening and closing sessions in one place (method)
 */
public class HibernateUtil {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("urPersistence");
    private static ThreadLocal<EntityManager> em = new ThreadLocal<>();

    /**
     * Save entity to DB
     * @param entity Entity
     * @param <T> Extends ABaseEntity
     */
    public static <T extends ABaseEntity> void save(T entity) {
        beginTransaction();
        try {
            getEntityManager().persist(entity);
        }
        finally {
            endTransaction();
        }

    }

    /**
     * Update entity info in DB
     * @param entity Entity
     * @param <T> Extends ABaseEntity
     */
    public static <T extends ABaseEntity> void update(T entity) {
        beginTransaction();
        getEntityManager().merge(entity);
        endTransaction();
    }

    /**
     * Remove entity info from DB
     * @param entity Entity
     * @param <T> Extends ABaseEntity
     */
    public static <T extends ABaseEntity> void remove(T entity) {
        beginTransaction();
        getEntityManager().remove(entity);
        endTransaction();
    }

    /**
     * Get entities list
     * @param namedQuery Query name in hibernate named queries storage
     * @param params Params for named query
     * @param pageNum Page number. Begin index for fetch results will be calculated as pageNum * {@link Config#RESULTS_PER_PAGE}
     * @param <T> Extends ABaseEntity
     * @return List with entities
     */
    @SuppressWarnings("unchecked")
    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery, List<QueryParam> params, int pageNum) {
        beginTransaction();
        Query query = getNamedQueryWithParams(namedQuery, params, pageNum);
        List<T> result = query.getResultList();
        endTransaction();
        return result;
    }

    /**
     * Get entities list
     * @param namedQuery Query name in hibernate named queries storage
     * @param params Params for named query
     * @param <T> Extends ABaseEntity
     * @return List with entities
     */
    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery, List<QueryParam> params) {
        return getEntities(namedQuery, params, -1);
    }


    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery, int pageNum, QueryParam... params) {
        return getEntities(namedQuery, Arrays.asList(params), pageNum);
    }

    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery, QueryParam... params) {
        return getEntities(namedQuery, Arrays.asList(params));
    }

    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery, int pageNum) {
        return getEntities(namedQuery, new ArrayList<>(), pageNum);
    }

    public static <T extends ABaseEntity> List<T> getEntities(String namedQuery) {
        return getEntities(namedQuery, new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends ABaseEntity> T getEntityById(Class<T> entityClass, long id) {
        beginTransaction();
        T result = getEntityManager().find(entityClass, id);
        endTransaction();
        return result;
    }


    @SuppressWarnings("unchecked")
    public static List<?> getObjects(String namedQuery, List<QueryParam> params, int pageNum) {
        beginTransaction();
        Query query = getNamedQueryWithParams(namedQuery, params, pageNum);
        List<?> result = query.getResultList();
        endTransaction();
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<?> getObjects(String namedQuery, List<QueryParam> params) {
        return getObjects(namedQuery, params, -1);
    }

    public static List<?> getObjects(String namedQuery, QueryParam... params) {
        return getObjects(namedQuery, Arrays.asList(params));
    }

    public static List<?> getObjects(String namedQuery) {
        return getObjects(namedQuery, new ArrayList<>());
    }

    //////////////////
    // Private methods
    //////////////////

    private static Query getNamedQueryWithParams(String namedQuery, List<QueryParam> params, int pageNum, int resultsCount) {
        Query query = getEntityManager().createNamedQuery(namedQuery);
        if (params != null && !params.isEmpty()) {
            for (QueryParam param : params) {
                query.setParameter(param.getName(), param.getValue());
            }
        }
        if (resultsCount < 0) {
            query.setFirstResult(pageNum * Config.RESULTS_PER_PAGE);
        } else {
            query.setFirstResult(pageNum * Config.RESULTS_PER_PAGE).setMaxResults(resultsCount);
        }
        return query;
    }

    private static Query getNamedQueryWithParams(String namedQuery, List<QueryParam> params, int pageNum) {
        return (pageNum > 0) ?
                getNamedQueryWithParams(namedQuery, params, pageNum - 1, Config.RESULTS_PER_PAGE) :
                getNamedQueryWithParams(namedQuery, params, 0, -1);
    }

    private static void beginTransaction() {
        if (getEntityManager() != null && getEntityManager().getTransaction().isActive()) {
            endTransaction();
        }
        em.set(emf.createEntityManager());
        getEntityManager().getTransaction().begin();
    }

    private static void endTransaction() {
        getEntityManager().getTransaction().commit();
        getEntityManager().close();
    }
    
    private static EntityManager getEntityManager() {
        return em.get();
    }

}
