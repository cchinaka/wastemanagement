package com.milcomsolutions.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.entity.core.Company;


public interface BaseService {

    <T> List<T> findAllEntities(Class<T> clazz);


    <T> List<T> findAllEntitiesLight(Class<T> clazz, String initParams);


    <T> List<T> findAllEntities(Class<T> clazz, int firstRecord, int maxRecords);


    <T> T saveEntity(T entity) throws Exception;


    <T> T findEntityById(Class<T> clazz, Serializable id);


    <T> Long findEntityCount(Class<T> t);


    <T> T findEntityByParams(Class<T> entityClass, Map<String, Object> params);


    <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params);


    <T> List<T> findEntitiesByParamsLight(Class<T> entityClass, String initParams, Map<String, Object> params);


    <T> void removeEntities(Class<T> clazz, List<Long> ids) throws Exception;


    <T> void removeEntity(T t) throws Exception;


    <T> long findEntityCountByParams(Class<T> entityClass, Map<String, Object> params);


    public <T> boolean doRecordsExist(Class<T> entityClass, String idName);


    <T> boolean doRecordsExist(Class<T> entityClass, Map<String, Object> params);


    <T> List<T> saveEntities(List<T> entities) throws Exception;


    <T extends BaseModel> Integer findEntityCount(RecordDisplayUtility<T> searchUtil);


    <T extends BaseModel> List<T> findAllEntitiesPaginated(Class<T> clazz, RecordDisplayUtility<T> searchUtil);


    <T extends BaseModel> RecordDisplayUtility<T> findAllEntitiesPaginated(RecordDisplayUtility<T> searchUtil);


    <T> int removeEntitiesByParams(Class<T> type, Map<String, Object> params) throws Exception;


    void flush();


    <T extends BaseModel> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params, int maxResult);


    <T> Map<String, Object> findEntityMapByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception;


    <T> List<Map<String, Object>> findEntityMapsByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception;


    <T> List<Map<String, Object>> findEntityMapsByParamsOrdered(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            String... fields) throws Exception;


    <T> Long findEntityMaxId(Class<T> entityClass);


    <T> T saveEntityTxn(T entity) throws Exception;


    <T> void disableEntitiesByIds(Class<T> theClass, List<Long> ids);


    Long getRootCompanyId(Long companyId);


    Company getRootCompany(Long companyCId);


    void publishApplicationEvent(ApplicationEvent event);


    <T extends BaseModel> RecordDisplayUtility<T> findNativeRecordsPaginated(RecordDisplayUtility<T> searchUtil);


    <S extends BaseModel> S createEntity(S entity) throws Exception;
}
