package com.milcomsolutions.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;


public class QueryBuilder {

    private static final Log LOG = LogFactory.getLog(QueryBuilder.class);

    private static final String WHERE = " where ";

    private static final String AND = " and ";

    private static final String DOT = ".";

    private static final String COMMA = ", ";

    public static final String ALIAS_PLACE_HOLDER = " _ALIAS_ ";

    private static final String UNDER_SCORE = "_";

    private static final String OR = " or ";

    private static QueryBuilder qbuilder = null;


    public QueryBuilder getInstance() {
        if (QueryBuilder.qbuilder == null) {
            QueryBuilder.qbuilder = new QueryBuilder();
        }
        return QueryBuilder.qbuilder;
    }


    public Query setParameters(Query query, Map<String, Object> params) {
        if (!CollectionUtils.isEmpty((params))) {
            for (Entry<String, Object> paramEntry : params.entrySet()) {
                if (!paramEntry.getKey().startsWith("_IN_")) {
                    String key = paramEntry.getKey().replaceAll("[.]", QueryBuilder.UNDER_SCORE);
                    if (key != null) {
                        if (paramEntry.getValue() instanceof java.util.Date || paramEntry.getValue() instanceof java.sql.Date) {
                            query.setParameter(key, (java.util.Date) paramEntry.getValue(), TemporalType.DATE);
                        } else if (paramEntry.getValue() instanceof java.sql.Time) {
                            query.setParameter(key, (java.sql.Time) paramEntry.getValue(), TemporalType.TIME);
                        } else if (paramEntry.getValue() instanceof java.sql.Timestamp) {
                            query.setParameter(key, (java.sql.Timestamp) paramEntry.getValue(), TemporalType.TIMESTAMP);
                        } else {
                            query.setParameter(key, paramEntry.getValue());
                        }
                    }
                }
            }
        }
        return query;
    }


    public <S> Query createQuery(EntityManager eManager, Class<S> entityClass, String jpql, Object... params) {
        if (eManager == null) {
            throw new RuntimeException("Entitymanager must be set for query to be executed");
        } else {
            Object[] updatedParams = null;
            Query q = eManager.createQuery(jpql);
            q = setParameters(q, updatedParams);
            return q;
        }
    }


    public Query setParameters(Query q, Object... params) {
        int index = 1;
        for (final Object parameter : params) {
            q = q.setParameter(index, parameter);
            index++;
        }
        return q;
    }


    public <S> Query createQuery(EntityManager eManager, Class<S> entityClass, String jpql, Map<String, Object> params) {
        if (eManager == null) {
            throw new RuntimeException("Entitymanager must be set for query to be executed");
        } else {
            Map<String, Object> qParams = new HashMap<String, Object>();
            qParams.putAll(params);
            Query query = eManager.createQuery(jpql);
            query = setParameters(query, qParams);
            return query;
        }
    }


    public <S> Query createQueryMap(EntityManager eManager, Class<S> entityClass, Map<String, Object> params, String[] fields) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveQuery(entityClass, fields, params, null, null, null, false);
        Query query = null;
        try {
            query = eManager.createQuery(jpql);
            query = setParameters(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }


    public <S> Query createQuery(EntityManager eManager, Class<S> entityClass, Map<String, Object> params) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveQuery(entityClass, params, null, null, null, false);
        Query query = null;
        try {
            LOG.debug("jpql: " + jpql);
            query = eManager.createQuery(jpql);
            query = setParameters(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }


    public <S> Query createLightQuery(EntityManager eManager, Class<S> entityClass, String initQl, Map<String, Object> params) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveLightQuery(entityClass, params, null, null, null, false, initQl);
        Query query = null;
        try {
            LOG.debug("jpql: " + jpql);
            query = eManager.createQuery(jpql);
            query = setParameters(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }


    public <S> Query createQueryCount(EntityManager eManager, Class<S> entityClass, Map<String, Object> params) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveQueryCount(entityClass, params);
        Query query = null;
        try {
            LOG.debug("jpql: " + jpql);
            query = eManager.createQuery(jpql);
            query = setParameters(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }


    public <S> Query createQueryMap(EntityManager eManager, Class<S> entityClass, Map<String, Object> params, String[] fields, String sSearch,
            List<String> filterCols, String orderField, boolean ascending) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveQuery(entityClass, fields, params, sSearch, filterCols, orderField, ascending);
        Query query = eManager.createQuery(jpql);
        query = setParameters(query, params);
        return query;
    }


    public <S> Query createCountQuery(EntityManager eManager, Class<S> entityClass, Map<String, Object> filter, String sSearch, List<String> filterCols) {
        String jpql = resolveCountQL(entityClass, filter, sSearch, filterCols);
        Query query = eManager.createQuery(jpql);
        query = setParameters(query, filter);
        return query;
    }


    public <S> String resolveQuery(Class<S> entityClass, String[] fields, Map<String, Object> params, String sSearch, List<String> filterCols,
            String orderField, boolean ascending) {
        String mapEntries = buildMapEntries(fields);
        String whereClause = buildWhereClause(params, "e");
        whereClause = buildFilterClause(whereClause, sSearch, filterCols);
        String orderClause = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(orderField)) {
            orderClause = String.format("order by e.%s %s", orderField.trim(), (ascending ? "asc" : "desc"));
        }
        String jpql = String.format("select new map(%s) from %s e %s %s", mapEntries, entityClass.getName(), whereClause, orderClause);
        return jpql;
    }


    public <S> String resolveLightQuery(Class<S> entityClass, Map<String, Object> params, String sSearch, List<String> filterCols, String orderField,
            boolean ascending, String initQL) {
        String prefix = getEntityPrefix(initQL);
        String whereClause = buildWhereClause(params, prefix);
        whereClause = buildFilterClause(whereClause, sSearch, filterCols);
        String orderClause = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(orderField)) {
            orderClause = String.format("order by %s.%s %s", prefix, orderField.trim(), (ascending ? "asc" : "desc"));
        }
        return String.format("select new %s(%s) from %s %s %s %s", entityClass.getName(), initQL, entityClass.getName(), prefix, whereClause, orderClause);
    }


    public <S> String resolveQuery(Class<S> entityClass, Map<String, Object> params, String sSearch, List<String> filterCols, String orderField,
            boolean ascending) {
        String whereClause = buildWhereClause(params, "e");
        whereClause = buildFilterClause(whereClause, sSearch, filterCols);
        String orderClause = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(orderField)) {
            orderClause = String.format("order by e.%s %s", orderField.trim(), (ascending ? "asc" : "desc"));
        }
        String jpql = String.format("select e from %s e %s %s", entityClass.getName(), whereClause, orderClause);
        return jpql;
    }


    public <S> String resolveQueryCount(Class<S> entityClass, Map<String, Object> params) {
        String whereClause = buildWhereClause(params, "e");
        whereClause = buildFilterClause(whereClause, null, null);
        String jpql = String.format("select count(e.id) from %s e %s", entityClass.getName(), whereClause);
        return jpql;
    }


    private String buildFilterClause(String whereClause, String sSearch, List<String> filterCols) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(sSearch) && !CollectionUtils.isEmpty(filterCols)) {
            sSearch = "%" + sSearch + "%";
            for (String field : filterCols) {
                builder.append(String.format("e.%s like ('%s')", field, sSearch)).append(QueryBuilder.OR);
            }
            String filter = String.format("( %s )", StringUtils.removeEnd(builder.toString(), QueryBuilder.OR));
            if (StringUtils.isBlank(whereClause)) {
                whereClause = String.format(" where %s ", filter);
            } else {
                whereClause += String.format(" and %s ", filter);
            }
        }
        return whereClause;
    }


    public <S> String resolveCountQL(Class<S> entityClass, Map<String, Object> params, String sSearch, List<String> filterCols) {
        String whereClause = buildWhereClause(params, "e");
        whereClause = buildFilterClause(whereClause, sSearch, filterCols);
        String jpql = String.format("select count(e) from %s e %s", entityClass.getName(), whereClause);
        return jpql;
    }


    public String buildMapEntries(String[] fields) {
        StringBuilder builder = new StringBuilder();
        String key = StringUtils.EMPTY;
        for (String field : fields) {
            key = StringUtils.replace(field, QueryBuilder.DOT, QueryBuilder.UNDER_SCORE);
            builder.append(String.format("e.%s as %s", field, key)).append(QueryBuilder.COMMA);
        }
        return StringUtils.removeEnd(builder.toString(), QueryBuilder.COMMA);
    }


    public String buildWhereClause(Map<String, Object> params, String prefix) {
        prefix = StringUtils.defaultIfBlank(prefix, "e");
        if (CollectionUtils.isEmpty(params)) {
            return StringUtils.EMPTY;
        }
        StringBuilder builder = new StringBuilder(QueryBuilder.WHERE);
        String key = StringUtils.EMPTY;
        for (String parameter : params.keySet()) {
            key = StringUtils.replace(parameter, QueryBuilder.DOT, QueryBuilder.UNDER_SCORE);
            if (parameter.startsWith("_IN_")) {
                if (params.get(parameter) != null) {
                    String val = (String) params.get(parameter);
                    parameter = StringUtils.replace(parameter, "_IN_", StringUtils.EMPTY);
                    builder.append(String.format("%s.%s IN ( %s )", prefix, parameter, val)).append(QueryBuilder.AND);
                }
            } else {
                builder.append(String.format("%s.%s=:%s", prefix, parameter, key)).append(QueryBuilder.AND);
            }
        }
        return StringUtils.removeEnd(builder.toString(), QueryBuilder.AND);
    }


    public <S> Query createDeleteQuery(EntityManager eManager, Class<S> entityClass, Map<String, Object> params) {
        Assert.notNull(eManager, "Entitymanager must be set for query to be executed");
        String jpql = resolveDeleteQuery(entityClass, params);
        Query query = null;
        try {
            LOG.info("remove jpql: " + jpql);
            query = eManager.createQuery(jpql);
            query = setParameters(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }


    public <S> String resolveDeleteQuery(Class<S> entityClass, Map<String, Object> params) {
        String whereClause = buildWhereClause(params, "e");
        whereClause = buildFilterClause(whereClause, null, null);
        return String.format("delete from %s e %s", entityClass.getName(), whereClause);
    }


    public String getEntityPrefix(String initParams) {
        String firstParam = initParams.split(",")[0].trim();
        String result = firstParam.substring(0, firstParam.indexOf("."));
        return result;
    }
}
