package com.milcomsolutions.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.milcomsolutions.commons.ConfigurationManager;
import com.milcomsolutions.dao.Dao;
import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.service.BaseService;


@Service("genericService")
@Transactional
public class GenericServiceImpl implements BaseService, ApplicationEventPublisherAware {

    @Autowired
    @Qualifier("defaultDao")
    protected Dao dao;

    @Autowired
    @Qualifier("configurationManager")
    protected ConfigurationManager configurationManager;

    public static final String dateformat = "dd/MM/yyyy";

    public static final String datetimeformat = "dd/MM/yyyy HH:mm:ss:SS a";

    protected static final Log LOG = LogFactory.getLog(GenericServiceImpl.class);

    protected Gson gson = new Gson();

    private ApplicationEventPublisher eventPublisher;


    @Override
    public <T> List<T> findAllEntities(Class<T> clazz) {
        return dao.findAllEntities(clazz);
    }


    @Override
    public <T> List<T> findAllEntities(Class<T> clazz, int firstRecord, int maxRecords) {
        return dao.findAllEntities(clazz, firstRecord, maxRecords);
    }


    @Override
    public <T> T saveEntityTxn(T entity) throws Exception {
        entity = dao.saveEntity(entity);
        flush();
        return entity;
    }


    @Override
    public <T> T saveEntity(T entity) throws Exception {
        return dao.saveEntity(entity);
    }


    @Override
    public <S extends BaseModel> S createEntity(S entity) throws Exception {
        dao.createEntity(entity);
        return entity;
    }


    @Override
    public <T> T findEntityById(Class<T> clazz, Serializable id) {
        try {
            return dao.findEntityById(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            GenericServiceImpl.LOG.error("error finding entity by id. Message: " + e.getMessage());
        }
        return null;
    }


    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }


    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }


    public <T> List<Object[]> findPaginatedEntitiesForDisplayOrdered(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            Integer startIndex, Integer pageSize, String sSearch, List<String> filterCols, String... fields) {
        List<Map<String, Object>> resultMapList = dao.findEntityMapsByParamsOrderedPaginated(entityClass, ascending, orderField, params, startIndex, pageSize,
                sSearch, filterCols, fields);
        return convertMapListToArrayList(resultMapList, fields);
    }


    public List<Object[]> convertMapListToArrayList(List<Map<String, Object>> resultMapList, String[] fields) {
        List<Object[]> recordArrayList = new ArrayList<Object[]>();
        Object[] infoHolder = null;
        for (Map<String, Object> entityMap : resultMapList) {
            int i = 0;
            infoHolder = new Object[entityMap.keySet().size()];
            for (String key : fields) {
                infoHolder[i++] = entityMap.get(key);
            }
            recordArrayList.add(infoHolder);
        }
        return recordArrayList;
    }


    public <T> Long findTotalQueryCount(Class<T> entityClass, Map<String, Object> filter, String sSearch, List<String> filterCols) {
        return dao.findTotalQueryCount(entityClass, filter, sSearch, filterCols);
    }


    @Override
    public <T> Long findEntityCount(Class<T> t) {
        return dao.findEntityCount(t);
    }


    @Override
    public <T> Long findEntityMaxId(Class<T> t) {
        return dao.findEntityMaxId(t);
    }


    @Override
    public <T> T findEntityByParams(Class<T> entityClass, Map<String, Object> params) {
        try {
            return dao.findEntityByParams(entityClass, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public <T> long findEntityCountByParams(Class<T> entityClass, Map<String, Object> params) {
        return dao.findEntityCountByParams(entityClass, params);
    }


    @Override
    public <T> void removeEntities(Class<T> clazz, List<Long> ids) throws Exception {
        dao.removeEntitiesByIds(clazz, ids);
    }


    @Override
    public <T> void removeEntity(T t) throws Exception {
        dao.removeEntity(t);
    }


    @Override
    public <T> boolean doRecordsExist(Class<T> entityClass, String idName) {
        return dao.doRecordsExist(entityClass, idName);
    }


    @Override
    public <T> boolean doRecordsExist(Class<T> entityClass, Map<String, Object> params) {
        return dao.doRecordsExist(entityClass, params);
    }


    @Override
    public <T> List<T> saveEntities(List<T> entities) throws Exception {
        for (T entity : entities) {
            entity = saveEntity(entity);
        }
        return entities;
    }


    @Override
    public <T extends BaseModel> Integer findEntityCount(RecordDisplayUtility<T> searchUtil) {
        return dao.findEntityCount(searchUtil);
    }


    @Override
    public <T extends BaseModel> List<T> findAllEntitiesPaginated(Class<T> clazz, RecordDisplayUtility<T> searchUtil) {
        return dao.findAllEntitiesPaginated(clazz, searchUtil);
    }


    @Override
    public <T extends BaseModel> RecordDisplayUtility<T> findAllEntitiesPaginated(RecordDisplayUtility<T> searchUtil) {
        return dao.findAllEntitiesPaginated(searchUtil);
    }


    @Override
    public <T> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params) {
        return dao.findEntitiesByParams(entityClass, params);
    }


    @Override
    public <T> int removeEntitiesByParams(Class<T> entityClass, Map<String, Object> params) throws Exception {
        return dao.removeEntitiesByParams(entityClass, params);
    }


    @Override
    public void flush() {
        dao.flush();
    }


    @Override
    public <T extends BaseModel> List<T> findEntitiesByParams(Class<T> entityClass, Map<String, Object> params, int maxResult) {
        return dao.findEntitiesByParams(entityClass, params, maxResult);
    }


    @Override
    public <T> Map<String, Object> findEntityMapByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception {
        return dao.findEntityMapByParams(entityClass, params, fields);
    }


    @Override
    public <T> List<Map<String, Object>> findEntityMapsByParams(Class<T> entityClass, Map<String, Object> params, String... fields) throws Exception {
        return dao.findEntityMapsByParams(entityClass, params, fields);
    }


    @Override
    public <T> List<Map<String, Object>> findEntityMapsByParamsOrdered(Class<T> entityClass, boolean ascending, String orderField, Map<String, Object> params,
            String... fields) throws Exception {
        return dao.findEntityMapsByParamsOrdered(entityClass, ascending, orderField, params, fields);
    }


    @Override
    public <T> void disableEntitiesByIds(Class<T> theClass, List<Long> ids) {
        dao.disableEntitiesByIds(theClass, ids);
    }


    @Override
    public <T> List<T> findAllEntitiesLight(Class<T> clazz, String initParams) {
        return dao.findAllEntitiesLight(clazz, initParams);
    }


    @Override
    public Long getRootCompanyId(Long companyId) {
        Long companyCId = companyId;
        Company company = null;
        while (true) {
            try {
                company = findEntityById(Company.class, companyCId);
            } catch (Exception e) {
                LOG.error(e);
            }
            if (company != null) {
                companyCId = company.getId();
                if (company.getParentCompany() != null) {
                    companyCId = company.getParentCompany().getId();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return companyCId;
    }


    @Override
    public Company getRootCompany(Long companyCId) {
        Company company = null;
        while (true) {
            try {
                company = findEntityById(Company.class, companyCId);
            } catch (Exception e) {
                LOG.error(e);
            }
            if (company != null) {
                if (company.getParentCompany() != null) {
                    company = company.getParentCompany();
                    companyCId = company.getId();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return company;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void publishApplicationEvent(ApplicationEvent event) {
        this.eventPublisher.publishEvent(event);
    }


    @Override
    public <T> List<T> findEntitiesByParamsLight(Class<T> entityClass, String initParams, Map<String, Object> params) {
        return dao.findEntitiesByParamsLight(entityClass, initParams, params);
    }


    @Override
    public <T extends BaseModel> RecordDisplayUtility<T> findNativeRecordsPaginated(RecordDisplayUtility<T> searchUtil) {
        return dao.findNativeRecordsPaginated(searchUtil);
    }


    public <T> List<T> findEntitiesPaginated(String jpql, Class<T> klass, int startIndex, int size) {
        return dao.findEntitiesPaginated(jpql, klass, startIndex, size);
    }
}
