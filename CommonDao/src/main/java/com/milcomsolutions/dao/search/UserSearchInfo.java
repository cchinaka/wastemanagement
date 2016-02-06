package com.milcomsolutions.dao.search;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.milcomsolutions.entity.core.UserRole;


public class UserSearchInfo extends AbstractSearchInfo {

    private String searchValue;


    public UserSearchInfo() {
    }


    public UserSearchInfo(List<UserRole> roles, String companyId) {
        super(roles, companyId);
    }


    public UserSearchInfo(String searchValue) {
        this.searchValue = searchValue;
    }


    @Override
    public String buildQueryCountString() {
        return String.format("select count(o.id) from User o %s", buildWhereClause());
    }


    @Override
    public void addQueryParameters(Query query) {
        if (StringUtils.isNotBlank(searchValue)) {
            query.setParameter("searchValue", "%" + searchValue + "%");
        }
    }


    @Override
    public String buildQueryString() {
        return String.format("select o from User o %s", buildWhereClause());
    }


    private String buildWhereClause() {
        StringBuffer whereClause = new StringBuffer(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(searchValue)) {
            whereClause.append(" where lower(concat(o.userDetail.firstName, o.userDetail.lastName, o.userDetail.email)) like :searchValue ");
        }
        String cX = resolveCompanyAndBusinessUnit("o").toString();
        if (StringUtils.isNotBlank(cX)) {
            if (whereClause.toString().isEmpty()) {
                whereClause.append("where");
            }
            if (whereClause.toString().trim().endsWith(" AND") || whereClause.toString().trim().toLowerCase().endsWith("where")) {
                whereClause.append(" (").append(cX).append(")");
            } else {
                whereClause.append(" AND (").append(cX).append(")");
            }
        }
        return whereClause.toString();
    }


    @Override
    public String getOrderString() {
        return "o.creationDate";
    }


    @Override
    public String getSearchValue() {
        return searchValue;
    }


    @Override
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }


    @Override
    public String buildExportQuery() {
        // TODO Auto-generated method stub
        return null;
    }

}
