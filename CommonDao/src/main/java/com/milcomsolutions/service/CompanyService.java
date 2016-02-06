package com.milcomsolutions.service;

import java.util.List;
import java.util.Map;

import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;


public interface CompanyService extends BaseService {

    Company saveCompany(Company company) throws Exception;


    List<String> loadBranchNamesByCompanyId(Long companyId);


    List<Company> findAllParentCompanies();


    List<Company> findAllParentCompaniesLight();


    List<Map<String, Object>> findAllParentCompaniesMap();


    List<User> findtUsersByCompanyId(Long companyId);


    List<Map<String, Object>> findLightUsersByCompanyId(Long companyId);


    List<Map<String, Object>> findLightUsersAgentsByCompanyId(Long companyId);


    List<Company> findAllBranchAndSubsByCompanyIdLight(Long companyId);

    Company findParentCompany(Long subCompanyId);


    Company findParentCompany(Company subCompany);


    List<Company> findCompanyAllBranchAndSubsByCompanyIdLight(Long companyId);


    List<User> findCollectingAgents(List<UserRole> userRoles, String companyId);


    List<User> findUsersByCompanyIdAndRolesLight(Long companyId, List<UserRole> adminRoles);


    List<String> getUserAdminEmails(User user);


    List<String> getTicketSupportNofiticationAdminEmails(User user);
}
