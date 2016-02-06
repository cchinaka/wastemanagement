package com.milcomsolutions.dao;

import java.util.List;
import java.util.Map;

import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;


public interface CompanyDao extends Dao {

    List<String> loadBranchNamesByCompanyId(Long id);


    List<Company> findAllParentCompanies();


    List<User> findtUsersByCompanyId(Long companyId);


    List<Map<String, Object>> findLightUsersByCompanyId(Long companyId);


    List<Map<String, Object>> findAllParentCompaniesMap();


    List<Map<String, Object>> findLightUsersAgentsByCompanyId(Long companyId);


    Company findParentCompany(Long subCompanyId);


    Company findParentCompany(Company subCompany);


    List<Company> findAllBranchAndSubsByCompanyIdLight(Long parentCompanyId);


    List<Company> findCompanyAllBranchAndSubsByCompanyIdLight(Long companyId);


    List<Company> findAllParentCompaniesLight();


    List<User> findCollectingAgents(List<UserRole> userRoles, String companyId);


    List<User> findUsersByCompanyIdAndRolesLight(Long companyId, List<UserRole> roles);
}
