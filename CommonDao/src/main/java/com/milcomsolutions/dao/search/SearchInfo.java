package com.milcomsolutions.dao.search;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;


public interface SearchInfo {

    String buildQueryString();


    String buildQueryCountString();


    void addQueryParameters(Query query);


    String getOrderString();


    void setSearchValue(String searchValue);


    String buildExportQuery();


    String buildSelectedExportQl();



    void setCompanyId(String companyId);


    void setBranchId(Long branchId);


    void setCompanyTreeIds(Set<Long> set);


    String getCompanyId();


    Long getBranchId();


    Long getParentCompanyId();


    void setTestMode(boolean testMode);


    boolean isTestMode();


    Date getStartDate();


    Date getEndDate();


    void setOrderColumn(Integer orderColumn);


    Integer getOrderColumn();


    void setOrderDirection(String orderDirection);


    String getOrderDirection();


    Map<Integer, String> getOrderColumnMapper();


    Map<Integer, String> getOrderColumnMapperNative();


    String getNativeQueryString();


    Map<String, Object> getNativeMapParams();


    String getNativeQueryCountQl();


    void setNativeMapParams(Map<String, Object> nativeMapParams);


    void setNativeQueryString(String nativeQueryString);


    void setNativeQueryCountQl(String nativeQueryCountQl);


    String getExportKey();


    void setExportKey(String exportKey);
}
