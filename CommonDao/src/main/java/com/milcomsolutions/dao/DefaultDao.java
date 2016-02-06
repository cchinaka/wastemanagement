package com.milcomsolutions.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.milcomsolutions.commons.ConfigurationManager;
import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.entity.core.UserRole;


@Repository("defaultDao")
public class DefaultDao extends NamedParameterJdbcDaoSupport implements Dao {

    private static final String FETCH_ALL_ENTITIES = "select e from %s e";

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected ConfigurationManager configurationManager;

    @PersistenceContext
    protected EntityManager entityManager;

    private static final Log LOG = LogFactory.getLog(DefaultDao.class);

    private static final String FETCH_ALL_ORDERED_ENTITIES = "select e from %s e order by e.%s";


    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    public void flush() {
        this.entityManager.flush();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllEntities(Class<T> clazz) {
        List<T> resultList = ListUtils.EMPTY_LIST;
        try {
            Query query = entityManager.createQuery(String.format(DefaultDao.FETCH_ALL_ENTITIES, clazz.getName()));
            resultList = query.getResultList();
        } catch (Exception e) {
            DefaultDao.LOG.error("Error fetching all " + clazz.getSimpleName(), e);
        }
        return resultList;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAllEntities(Class<T> entityClass, String orderField) {
        Query query = entityManager.createQuery(String.format(DefaultDao.FETCH_ALL_ORDERED_ENTITIES, entityClass.getName(), orderField));
        return query.getResultList();
    }


    @Override
    public <T> T findEntityById(Class<T> clazz, Serializable id) {
        return entityManager.find(clazz, id);
    }


    @Override
    public <T> T saveEntity(T entity) throws Exception {
        return entityManager.merge(entity);
    }


    @Override
    public <T> List<T> saveEntities(List<T> entities) {
        List<T> savedEntities = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(entities)) {
            savedEntities = new ArrayList<T>();
            for (T entity : entities) {
                if (entity != null) {
                    savedEntities.add(entityManager.merge(entity));
                }
            }
        }
        return savedEntities;
    }


    @Override
    public <T> void removeEntity(T entity) throws Exception {
        entityManager.remove(entity);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntities(Class<T> entityClass, String jpql, Map<String, Object> params) {
        Query query = new QueryBuilder().createQuery(entityManager, entityClass, jpql, params);
        return query.getResultList();
    }


    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, Object> findEntityMapByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder();
        Query query = queryBuilder.createQueryMap(entityManager, entityClass, params, fields, null, null, StringUtils.EMPTY, false);
        return (Map<String, Object>) query.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T findEntityByParams(Class<T> entityClass, Map<String, Object> params) {
        try {
            QueryBuilder queryBuilder = new QueryBuilder();
            Query query = queryBuilder.createQuery(entityManager, entityClass, params);
            return (T) query.getSingleResult();
        } catch (Exception e) {
            DefaultDao.LOG.info(e.getMessage());
        }
        return null;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Map<String, Object>> findEntityMapsByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder();
        Query query = queryBuilder.createQueryMap(entityManager, entityClass, params, fields, null, null, StringUtils.EMPTY, false);
        return query.getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Map<String, Object>> findEntityMapsByParamsOrdered(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            String... fields) throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder();
        Query query = queryBuilder.createQueryMap(entityManager, entityClass, params, fields, null, null, orderField, ascending);
        return query.getResultList();
    }


    @SuppressWarnings("unchecked")
    public <T> List<Map<String, Object>> findEntityMapByExtJPQL(String jpql) throws Exception {
        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }


    @Override
    public <T> int removeEntityById(Class<T> clazz, Serializable id) throws Exception {
        Query query = entityManager.createQuery(String.format("delete from %s e where e.id=:id", clazz.getName()));
        query.setParameter("id", id);
        return query.executeUpdate();
    }


    @Override
    public <T> int removeEntitiesByIds(Class<T> clazz, List<? extends Serializable> ids) throws Exception {
        Query query = entityManager.createQuery(String.format("delete from %s e where e.id in(:ids)", clazz.getName()));
        query.setParameter("ids", ids);
        return query.executeUpdate();
    }


    @Override
    public <T> List<T> findAllEntities(Class<T> clazz, int firstRecord, int maxRecords) {
        try {
            return entityManager.createQuery(String.format(DefaultDao.FETCH_ALL_ENTITIES, clazz.getName()), clazz).setFirstResult(firstRecord)
                    .setMaxResults(maxRecords).getResultList();
        } catch (Exception e) {
            DefaultDao.LOG.error("Error fetching all " + clazz.getSimpleName());
        }
        return new ArrayList<T>();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesPaginated(Class<T> entityClass, String jpql, Map<String, Object> params, int startIndex, int pageSize) {
        Query query = new QueryBuilder().createQuery(entityManager, entityClass, jpql, params);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<Map<String, Object>> findEntityMapsByParamsOrderedPaginated(Class<T> entityClass, boolean ascending, String orderField,
            Map<String, Object> params, Integer startIndex, Integer pageSize, String sSearch, List<String> filterCols, String... fields) {
        Query query = new QueryBuilder().createQueryMap(entityManager, entityClass, params, fields, sSearch, filterCols, orderField, ascending);
        setQueryFirstIndexAndPageSize(startIndex, pageSize, query);
        return query.getResultList();
    }


    @Override
    public <T> Long findTotalQueryCount(Class<T> entityClass, Map<String, Object> filter, String sSearch, List<String> filterCols) {
        long totalQueryCount = 0;
        try {
            QueryBuilder queryBuilder = new QueryBuilder();
            Query query = queryBuilder.createCountQuery(entityManager, entityClass, filter, sSearch, filterCols);
            totalQueryCount = Long.parseLong(String.valueOf(query.getSingleResult()));
        } catch (Exception e) {
            DefaultDao.LOG.debug("problem fetching count for query", e);
        }
        return totalQueryCount;
    }


    public void setQueryFirstIndexAndPageSize(Integer startIndex, Integer pageSize, Query query) {
        if (startIndex != null) {
            query.setFirstResult(startIndex);
        }
        if (pageSize != null) {
            query.setMaxResults(pageSize);
        }
    }


    @Override
    public <T> Long findEntityCount(Class<T> clazz) {
        return entityManager.createQuery(String.format("select count(c) from %s c", clazz.getName()), Long.class).getSingleResult();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params) {
        QueryBuilder queryBuilder = new QueryBuilder();
        return queryBuilder.createQuery(entityManager, entityClass, params).getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params, int maxResult) {
        QueryBuilder queryBuilder = new QueryBuilder();
        Query query = queryBuilder.createQuery(entityManager, entityClass, params);
        setQueryFirstIndexAndPageSize(1, maxResult, query);
        return query.getResultList();
    }


    @Override
    public <T> boolean doRecordsExist(Class<T> entityClass, String idName) {
        String jpql = String.format("select count(e.%s) from %s e", idName, entityClass.getName());
        Long recordCount = getEntityManager().createQuery(jpql, Long.class).getSingleResult();
        DefaultDao.LOG.info("record count: " + recordCount);
        return recordCount > 0;
    }


    @Override
    public <T> boolean doRecordsExist(Class<T> entityClass, Map<String, Object> params) {
        return findEntityCountByParams(entityClass, params) > 0;
    }


    @Override
    public <T> void removeAll(Class<T> clazz) {
        entityManager.createQuery(String.format("Delete from %s", clazz.getName())).executeUpdate();
    }


    @Override
    public <T> long findEntityCountByParams(Class<T> entityClass, Map<String, Object> params) {
        return (long) new QueryBuilder().createQueryCount(entityManager, entityClass, params).getSingleResult();
    }


    @Override
    public <T extends BaseModel> Integer findEntityCount(RecordDisplayUtility<T> searchUtil) {
        return ((Number) searchUtil.buildQueryUnpaged(getEntityManager().createQuery(searchUtil.getQueryCountString())).getSingleResult()).intValue();
    }


    @Override
    public <T extends BaseModel> List<T> findAllEntitiesPaginated(Class<T> entity, RecordDisplayUtility<T> searchUtil) {
        String jpql = String.format("%s %s", searchUtil.getQueryString(), searchUtil.getOrderString());
        DefaultDao.LOG.info("list jpql: " + jpql);
        return searchUtil.buildTypedQuery(getEntityManager().createQuery(jpql, entity)).getResultList();
    }


    @Override
    public boolean isAvailableEntity(BaseModel entity) {
        return entity != null && entity.getId() != null;
    }


    @Override
    public <T> void removeEntities(Collection<T> entities) throws Exception {
        for (T entity : entities) {
            removeEntity(entity);
        }
    }


    @Override
    public <T extends BaseModel> RecordDisplayUtility<T> findAllEntitiesPaginated(RecordDisplayUtility<T> searchUtil) {
        searchUtil.setRecords(findAllEntitiesPaginated(searchUtil.getClassType(), searchUtil));
        searchUtil.setRecordCount(findEntityCount(searchUtil));
        return searchUtil;
    }


    @Override
    public <T extends BaseModel> RecordDisplayUtility<T> findNativeRecordsPaginated(RecordDisplayUtility<T> searchUtil) {
        String pagedSql = searchUtil.getNativeQueryString();
        String sqlCount = searchUtil.getNativeQueryCount();
        Map<String, Object> params = searchUtil.getSearchInfo().getNativeMapParams();
        DefaultDao.LOG.info("native sql: " + pagedSql);
        searchUtil.setNativeRecords(getNamedParameterJdbcTemplate().queryForList(pagedSql, params));
        searchUtil.setNativeRecordCount(getNamedParameterJdbcTemplate().queryForObject(sqlCount, params, Number.class).intValue());
        return searchUtil;
    }


    @Override
    public <T> int removeEntitiesByParams(Class<T> entityClass, Map<String, Object> params) throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder();
        return queryBuilder.createDeleteQuery(entityManager, entityClass, params).executeUpdate();
    }


    @Override
    public <T> Long findEntityMaxId(Class<T> clazz) {
        return entityManager.createQuery(String.format("select max(c.id) from %s c", clazz.getName()), Long.class).getSingleResult();
    }


    @Override
    public <T> void disableEntitiesByIds(Class<T> theClass, List<Long> ids) {
        String updateQl = String.format("update %s c set c.active = false where c.id in (:ids)", theClass.getName());
        int updatedRecords = getEntityManager().createQuery(updateQl).setParameter("ids", ids).executeUpdate();
        DefaultDao.LOG.info("disabled entities: " + updatedRecords);
    }


    @Override
    public <T> List<T> findAllEntitiesLight(Class<T> clazz, String initParams) {
        List<T> resultList = Collections.emptyList();
        try {
            String queryString = String.format("select new %s(%s) from %s %s", clazz.getName(), initParams, clazz.getName(),
                    new QueryBuilder().getEntityPrefix(initParams));
            DefaultDao.LOG.debug("queryString - " + queryString);
            resultList = entityManager.createQuery(queryString, clazz).getResultList();
        } catch (Exception e) {
            DefaultDao.LOG.error("Error fetching all " + clazz.getSimpleName(), e);
        }
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesByParamsLight(Class<T> entityClass, String initParams, Map<String, Object> params) {
        QueryBuilder queryBuilder = new QueryBuilder();
        return queryBuilder.createLightQuery(entityManager, entityClass, initParams, params).getResultList();
    }


    protected List<String> getRoleCodes(List<UserRole> userRoles) {
        List<String> codes = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            codes.add(userRole.getCode());
        }
        return codes;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesPaginated(Class<T> clazz, int startIndex, int pageSize) {
        Query query = entityManager.createQuery(String.format(DefaultDao.FETCH_ALL_ENTITIES, clazz.getName()));
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findEntitiesPaginated(String jpql, Class<T> clazz, int startIndex, int pageSize) {
        Query query = entityManager.createQuery(jpql);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }


    @Override
    public <S extends BaseModel> S createEntity(S entity) {
        entityManager.persist(entity);
        return entity;
    }
}
