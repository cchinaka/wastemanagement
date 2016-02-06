package com.milcomsolutions.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.entity.core.BaseModel;


// @Repository("dao")
public interface Dao {

    <T> List<T> findAllEntities(Class<T> clazz);


    <T> List<T> findAllEntitiesLight(Class<T> clazz, String initParams);


    <T> List<T> findAllEntities(Class<T> clazz, int firstRecord, int maxRecords);


    <T> T findEntityById(Class<T> clazz, Serializable id);


    <T> List<T> findAllEntities(Class<T> entityClass, String orderField);


    <T> T saveEntity(T entity) throws Exception;


    <T> void removeEntity(T entity) throws Exception;


    <T> List<T> findEntities(Class<T> entityClass, String jpql, Map<String, Object> params);


    <T> Map<String, Object> findEntityMapByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception;


    <T> List<Map<String, Object>> findEntityMapsByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception;


    <T> List<Map<String, Object>> findEntityMapsByParamsOrdered(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            String... fields) throws Exception;


    <T> int removeEntityById(Class<T> clazz, Serializable id) throws Exception;


    <T> int removeEntitiesByIds(Class<T> clazz, List<? extends Serializable> ids) throws Exception;


    <T> List<T> findEntitiesPaginated(Class<T> entityClass, String jpql, Map<String, Object> params, int startIndex, int pageSize);


    <T> List<Map<String, Object>> findEntityMapsByParamsOrderedPaginated(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            Integer startIndex, Integer pageSize, String sSearch, List<String> filterCols, String... fields);


    <T> Long findTotalQueryCount(Class<T> entityClass, Map<String, Object> filter, String sSearch, List<String> filterCols);


    <T> T findEntityByParams(Class<T> entityClass, Map<String, Object> params);


    <T> long findEntityCountByParams(Class<T> entityClass, Map<String, Object> params);


    EntityManager getEntityManager();


    <T> Long findEntityCount(Class<T> t);


    <T> Long findEntityMaxId(Class<T> t);


    <T> boolean doRecordsExist(Class<T> entityClass, String idName);


    // <T> boolean doRecordsExist(Class<T> entityClass, String idName, Map<String, Object> params);
    <T> boolean doRecordsExist(Class<T> entityClass, Map<String, Object> params);


    <T> void removeAll(Class<T> clazz);


    <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params);


    <T> List<T> saveEntities(List<T> entities);


    <T> void removeEntities(Collection<T> entities) throws Exception;


    <T extends BaseModel> Integer findEntityCount(RecordDisplayUtility<T> searchUtil);


    <T extends BaseModel> List<T> findAllEntitiesPaginated(Class<T> clazz, RecordDisplayUtility<T> searchUtil);


    <T extends BaseModel> RecordDisplayUtility<T> findAllEntitiesPaginated(RecordDisplayUtility<T> searchUtil);


    <T extends BaseModel> RecordDisplayUtility<T> findNativeRecordsPaginated(RecordDisplayUtility<T> searchUtil);


    boolean isAvailableEntity(BaseModel entity);


    <T> int removeEntitiesByParams(Class<T> entityClass, Map<String, Object> params) throws Exception;


    void flush();


    <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params, int maxResult);


    <T> void disableEntitiesByIds(Class<T> theClass, List<Long> ids);


    <T> List<T> findEntitiesByParamsLight(Class<T> entityClass, String initParams, Map<String, Object> params);


    <T> List<T> findEntitiesPaginated(Class<T> clazz, int startIndex, int pageSize);


    <T> List<T> findEntitiesPaginated(String jpql, Class<T> clazz, int startIndex, int pageSize);


    <S extends BaseModel> S createEntity(S entity);
}
