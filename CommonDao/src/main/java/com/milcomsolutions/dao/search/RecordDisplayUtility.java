package com.milcomsolutions.dao.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.utils.CrudFilter;


public class RecordDisplayUtility<T extends BaseModel> implements Serializable {

    private final Class<T> classType;

    private List<T> records;

    private Integer recordCount;

    private List<Map<String, Object>> nativeRecords;

    private Integer nativeRecordCount;

    private static final long serialVersionUID = 4618227533753773789L;

    public static final String DEFAULT_TABLE_ID = "row";

    public static final int START_PAGE_NO = 1;

    public static final Integer PAGE_SIZE_DEFAULT = 1000;

    private Integer pageNo;

    private Integer pageSize;

    private List<String> filterColumns;

    private String searchParam;

    private Integer currentStartIndex;

    private Map<String, Object> recordInfo;

    private SearchInfo searchInfo;

    private static final Log LOG = LogFactory.getLog(RecordDisplayUtility.class);

    public static final String PAGE_SIZE_KEY = "pageSize";

    public static final String RECORD_LIST_KEY = "records";

    public static final String TOTAL_SIZE_KEY = "totalSize";

    public static final String RECORD_UTIL = "recordUtil";

    private boolean ordered;

    private boolean ascendingOrder = true;


    public Integer getStartIndex() {
        if (getCurrentStartIndex() == null) {
            setCurrentStartIndex((pageNo - 1) * pageSize);
        }
        return getCurrentStartIndex();
    }


    private RecordDisplayUtility(Class<T> classType) {
        this.classType = classType;
        pageSize = RecordDisplayUtility.PAGE_SIZE_DEFAULT;
        filterColumns = new ArrayList<String>();
        recordInfo = new HashMap<String, Object>();
        searchParam = StringUtils.EMPTY;
        setPageNo(RecordDisplayUtility.START_PAGE_NO);
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createDefaultInstance(Class<T> classType) {
        return new RecordDisplayUtility<T>(classType);
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createInstanceWithSearchInfo(Class<T> classType, SearchInfo searchInfo) {
        RecordDisplayUtility<T> utility = RecordDisplayUtility.createDefaultInstance(classType);
        utility.setSearchInfo(searchInfo);
        return utility;
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createInstanceWithSearchInfo(Class<T> classType, SearchInfo searchInfo, CrudFilter filter) {
        RecordDisplayUtility<T> utility = RecordDisplayUtility.createDefaultInstance(classType);
        utility.setSearchInfo(searchInfo);
        utility.setPageNo(filter.getDraw());
        utility.setPageSize(filter.getLength());
        utility.setCurrentStartIndex(filter.getStart());
        return utility;
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createWithPageSize(Class<T> classType, Integer pageSize) {
        RecordDisplayUtility<T> util = RecordDisplayUtility.createDefaultInstance(classType);
        util.setPageSize(pageSize);
        return util;
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createWithFilterParam(Class<T> classType, List<String> filterColumns) {
        RecordDisplayUtility<T> util = RecordDisplayUtility.createDefaultInstance(classType);
        util.setFilterColumns(filterColumns);
        return util;
    }


    public static <T extends BaseModel> RecordDisplayUtility<T> createWithPageSizeAndFilterParam(Class<T> classType, Integer pageSize,
            List<String> filterColumns) {
        RecordDisplayUtility<T> util = RecordDisplayUtility.createDefaultInstance(classType);
        util.setPageSize(pageSize);
        util.setFilterColumns(filterColumns);
        return util;
    }


    public static Integer getPageSizeDefault() {
        return RecordDisplayUtility.PAGE_SIZE_DEFAULT;
    }


    public Integer getPageNo() {
        return pageNo;
    }


    public void setPageNo(Integer pageNo) {
        if (pageNo != null) {
            this.pageNo = pageNo;
        }
    }


    public void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }


    public Integer getPageSize() {
        if (pageSize == null) {
            return getPageSizeDefault();
        }
        return pageSize;
    }


    public List<String> getFilterColumns() {
        return filterColumns;
    }


    public void setFilterColumns(List<String> filterColumns) {
        this.filterColumns = filterColumns;
    }


    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }


    public String getSearchParam() {
        return searchParam;
    }


    public Map<String, Object> getRecordInfo() {
        return recordInfo;
    }


    public void setRecordInfo(Map<String, Object> recordInfo) {
        this.recordInfo = recordInfo;
    }


    public Integer getCurrentStartIndex() {
        return currentStartIndex;
    }


    public void setCurrentStartIndex(Integer currentStartIndex) {
        this.currentStartIndex = currentStartIndex;
    }


    public String getQueryString() {
        String queryString = getSearchInfo().buildQueryString();
        RecordDisplayUtility.LOG.debug("query string: " + queryString);
        return queryString;
    }


    public String getQueryCountString() {
        return getSearchInfo().buildQueryCountString();
    }


    public Query buildQueryUnpaged(Query query) {
        getSearchInfo().addQueryParameters(query);
        return query;
    }


    public TypedQuery<T> buildTypedQueryUnpaged(TypedQuery<T> query) {
        getSearchInfo().addQueryParameters(query);
        return query;
    }


    public Query buildQuery(Query query) {
        getSearchInfo().addQueryParameters(query);
        query.setMaxResults(getPageSize());
        query.setFirstResult(getPageNo());
        return query;
    }


    public TypedQuery<T> buildTypedQuery(TypedQuery<T> query) {
        getSearchInfo().addQueryParameters(query);
        query.setMaxResults(getPageSize());
        query.setFirstResult(getStartIndex());
        return query;
    }


    public SearchInfo getSearchInfo() {
        return searchInfo;
    }


    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }


    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }


    public boolean isOrdered() {
        return ordered;
    }


    public boolean isAscendingOrder() {
        return ascendingOrder;
    }


    public void setAscendingOrder(boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }


    public String getOrderString() {
        StringBuffer buffer = new StringBuffer("");
        if (isOrdered()) {
            String orderColumn = searchInfo.getOrderString();
            if (MapUtils.isNotEmpty(searchInfo.getOrderColumnMapper())
                    && (StringUtils.isNotBlank(searchInfo.getOrderColumnMapper().get(searchInfo.getOrderColumn())))) {
                LOG.info("column index: " + searchInfo.getOrderColumn());
                orderColumn = searchInfo.getOrderColumnMapper().get(searchInfo.getOrderColumn());
                LOG.info("ordering by: " + orderColumn);
            }
            buffer.append("order by " + orderColumn);
            if (ascendingOrder) {
                buffer.append(" asc");
            } else {
                buffer.append(" desc");
            }
        }
        return buffer.toString();
    }


    public void setRecords(List<T> records) {
        this.records = records;
    }


    public List<T> getRecords() {
        return records;
    }


    public Class<T> getClassType() {
        return classType;
    }


    public Integer getRecordCount() {
        return recordCount;
    }


    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }


    public String getNativeQueryString() {
        return String.format("%s limit %s, %s", getSearchInfo().getNativeQueryString(), getStartIndex(), getPageSize());
    }


    public String getNativeQueryCount() {
        return getSearchInfo().getNativeQueryCountQl();
    }


    public List<Map<String, Object>> getNativeRecords() {
        return nativeRecords;
    }


    public void setNativeRecords(List<Map<String, Object>> nativeRecords) {
        this.nativeRecords = nativeRecords;
    }


    public Integer getNativeRecordCount() {
        return nativeRecordCount;
    }


    public void setNativeRecordCount(Integer nativeRecordCount) {
        this.nativeRecordCount = nativeRecordCount;
    }
}
