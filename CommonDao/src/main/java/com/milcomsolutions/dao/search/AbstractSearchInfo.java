package com.milcomsolutions.dao.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.milcomsolutions.entity.core.UserRole;


public abstract class AbstractSearchInfo implements SearchInfo {

    protected String searchValue;

    protected String companyId;

    protected final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected Long parentCompanyId;

    protected Long branchId;

    protected Set<Long> companyTreeIds = new HashSet<>();

    protected boolean testMode;

    protected Date startDate;

    protected Date endDate;

    protected Integer orderColumn;

    protected String orderDirection;

    protected Map<String, Object> nativeMapParams = new HashMap<>();

    protected String nativeQueryString;

    protected String nativeQueryCountQl;

    private static Log LOG = LogFactory.getLog(AbstractSearchInfo.class);

    protected String exportKey;


    public AbstractSearchInfo(List<UserRole> userRoles, String companyId) {
        this();
        setCompanyId(companyId);
        if (StringUtils.isNotBlank(companyId)) {
            setBranchId(Long.valueOf(companyId));
        }
        initNativeQueryParams();
    }


    protected void initNativeQueryParams() {
        // TODO Auto-generated method stub
    }


    public AbstractSearchInfo() {
        startDate = getResolvedStartDate();
        endDate = getResolvedEndDate();
    }


    @Override
    public abstract String buildQueryCountString();


    @Override
    public abstract String buildQueryString();


    @Override
    public abstract String getOrderString();


    @Override
    public Map<Integer, String> getOrderColumnMapper() {
        return null;
    }


    @Override
    public Map<Integer, String> getOrderColumnMapperNative() {
        return null;
    }


    @Override
    public void addQueryParameters(Query query) {
        if (StringUtils.isNotBlank(searchValue)) {
            query.setParameter("searchValue", "%" + searchValue + "%");
        }
    }


    public String getSearchValue() {
        return searchValue;
    }


    @Override
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }


    public Date getDateAtEndOFDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new LocalDate(date.getTime()).toDateTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
    }


    public Date getDateAtStartOFDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new LocalDate(date.getTime()).toDateTimeAtStartOfDay().toDate();
    }


    @Override
    public String buildExportQuery() {
        throw new NotYetImplementedException();
    }


    public String addHeaderNull(int startIndex) {
        String header = "";
        for (int i = startIndex; i < 16; i++) {
            header += String.format("null HEADER%s,", i);
        }
        return header;
    }


    public String addStringHeaderAt(int index, String headerName, boolean isNumber) {
        String header = "";
        if (isNumber) {
            header = String.format("Concat('%s',':N') HEADER%s,", headerName, index);
        } else {
            header = String.format("Concat('%s',':S') HEADER%s,", headerName, index);
        }
        return header;
    }


    public String addValueNull(int startIndex) {
        String header = "";
        for (int i = startIndex; i < 16; i++) {
            header += String.format("null VALUE%s,", i);
        }
        return StringUtils.removeEnd(header, ",");
    }


    public String addValueColumn(int index, String colName) {
        return String.format("%s VALUE%s,", colName, index);
    }


    public StringBuffer resolveCompanyAndBusinessUnit(String prefix) {
        StringBuffer whereClause = new StringBuffer(StringUtils.EMPTY);
        // if (CollectionUtils.isNotEmpty(companyTreeIds)) {
        // whereClause.append(String.format(" %s.companyId in (%s) ", prefix, StringUtils.join(companyTreeIds, ",")));
        // }
        return whereClause;
    }


    public StringBuffer resolveCompanyAndBusinessUnit(String prefix, String companyIdSpecifier) {
        StringBuffer whereClause = new StringBuffer(StringUtils.EMPTY);
        if (CollectionUtils.isNotEmpty(companyTreeIds)) {
            whereClause.append(String.format("  %s.%s in (%s) ", prefix, companyIdSpecifier, StringUtils.join(companyTreeIds, ",")));
        }
        return whereClause;
    }


    public StringBuffer resolveCompanyAndBusinessUnitNative(String prefix) {
        StringBuffer x = new StringBuffer(StringUtils.EMPTY);
        if (CollectionUtils.isNotEmpty(companyTreeIds)) {
            x.append(String.format("  %s.company_id in (%s)", prefix, StringUtils.join(companyTreeIds, ",")));
        }
        return x;
    }


    @Override
    public String getCompanyId() {
        return companyId;
    }


    @Override
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    @Override
    public Long getBranchId() {
        return branchId;
    }


    @Override
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }


    public Set<Long> getCompanyTreeIds() {
        return companyTreeIds;
    }


    @Override
    public void setCompanyTreeIds(Set<Long> companyTreeIds) {
        this.companyTreeIds = companyTreeIds;
    }


    @Override
    public Long getParentCompanyId() {
        return parentCompanyId;
    }


    public void setParentCompanyId(Long parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }


    @Override
    public boolean isTestMode() {
        return testMode;
    }


    @Override
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }


    @Override
    public Date getStartDate() {
        // if (startDate == null) {
        // this.startDate = new Date();
        // }
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    @Override
    public Date getEndDate() {
        // if (endDate == null) {
        // this.endDate = this.startDate;
        // }
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    protected Date getResolvedEndDate() {
        endDate = endDate == null ? getDateAtEndOFDay(new Date()) : getDateAtEndOFDay(endDate);
        return endDate;
    }


    protected Date getResolvedStartDate() {
        try {
            return startDate == null ? getDateAtStartOFDay(new DateTime(new Date()).minusWeeks(1).toDate()) : getDateAtStartOFDay(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected Date getResolvedEndDate(Date endDate) {
        endDate = endDate == null ? getDateAtEndOFDay(new Date()) : getDateAtEndOFDay(endDate);
        return endDate;
    }


    protected Date getResolvedStartDate(Date startDate) {
        try {
            return startDate == null ? getDateAtStartOFDay(new DateTime(new Date()).minusWeeks(1).toDate()) : getDateAtStartOFDay(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected String getLikeValue(String value) {
        return "%" + value + "%";
    }


    @Override
    public Integer getOrderColumn() {
        return orderColumn;
    }


    @Override
    public void setOrderColumn(Integer orderColumn) {
        this.orderColumn = orderColumn;
    }


    @Override
    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }


    @Override
    public String getOrderDirection() {
        return this.orderDirection;
    }


    protected String builderOrder() {
        String orderString = StringUtils.EMPTY;
        Map<Integer, String> mapper = getOrderColumnMapperNative();
        if (MapUtils.isNotEmpty(mapper) && mapper.get(getOrderColumn()) != null) {
            if (StringUtils.isNotBlank(getOrderDirection())) {
                orderString = String.format(" order by %s %s", mapper.get(getOrderColumn()), getOrderDirection());
            }
        }
        return orderString;
    }


    @Override
    public Map<String, Object> getNativeMapParams() {
        return nativeMapParams;
    }


    @Override
    public void setNativeMapParams(Map<String, Object> nativeMapParams) {
        this.nativeMapParams = nativeMapParams;
    }


    @Override
    public String getNativeQueryString() {
        LOG.info("nativeQueryString: " + nativeQueryString);
        return nativeQueryString;
    }


    @Override
    public void setNativeQueryString(String nativeQueryString) {
        this.nativeQueryString = nativeQueryString;
    }


    @Override
    public String getNativeQueryCountQl() {
        LOG.info("nativeQueryCountQl: " + nativeQueryCountQl);
        return nativeQueryCountQl;
    }


    @Override
    public void setNativeQueryCountQl(String nativeQueryCountQl) {
        this.nativeQueryCountQl = nativeQueryCountQl;
    }


    @Override
    public String buildSelectedExportQl() {
        if (StringUtils.isBlank(getExportKey())) {
            return buildExportQuery();
        } else {
            throw new NotYetImplementedException();
        }
    }


    @Override
    public String getExportKey() {
        return exportKey;
    }


    @Override
    public void setExportKey(String exportKey) {
        this.exportKey = exportKey;
    }


    protected Date get1970() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970");
        } catch (Exception e) {
        }
        return null;
    }
}
