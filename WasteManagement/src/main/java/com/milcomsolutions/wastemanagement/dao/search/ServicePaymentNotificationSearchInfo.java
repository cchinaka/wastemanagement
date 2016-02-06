package com.milcomsolutions.wastemanagement.dao.search;

import org.apache.commons.lang.StringUtils;

import com.milcomsolutions.dao.search.AbstractSearchInfo;


public class ServicePaymentNotificationSearchInfo extends AbstractSearchInfo {

    @Override
    public String buildQueryCountString() {
        return String.format("select count(o.id) from ServicePaymentNotificationConfig o %s", buildWhereClause());
    }


    private String buildWhereClause() {
        StringBuffer whereClause = new StringBuffer(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(searchValue)) {
            whereClause.append(" where lower(concat(o.serviceTypeId, o.name, o.description)) like lower(:searchValue) ");
        }
        return whereClause.toString();
    }


    @Override
    public String buildQueryString() {
        return String.format("select o from ServicePaymentNotificationConfig o %s", buildWhereClause());
    }


    @Override
    public String getOrderString() {
        return "o.creationDate";
    }
}
