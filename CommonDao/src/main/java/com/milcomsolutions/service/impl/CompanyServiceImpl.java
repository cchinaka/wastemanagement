package com.milcomsolutions.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.dao.CompanyDao;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.service.CompanyService;


@Transactional
@Service("companyService")
public class CompanyServiceImpl extends GenericServiceImpl implements CompanyService {

    @Autowired
    CompanyDao companyDao;


    @Override
    public Company saveCompany(Company company) throws Exception {
        if (company.getParentCompany() != null && company.getParentCompany().getId() == null) {
            company.setParentCompany(null);
        }
        return saveEntity(company);
    }


    @Override
    public List<String> loadBranchNamesByCompanyId(Long id) {
        return companyDao.loadBranchNamesByCompanyId(id);
    }


    @Override
    public List<Company> findAllParentCompanies() {
        return companyDao.findAllParentCompanies();
    }


    @Override
    public List<User> findtUsersByCompanyId(Long companyId) {
        return companyDao.findtUsersByCompanyId(companyId);
    }


    @Override
    public List<Map<String, Object>> findLightUsersByCompanyId(Long companyId) {
        return companyDao.findLightUsersByCompanyId(companyId);
    }


    @Override
    public List<Map<String, Object>> findAllParentCompaniesMap() {
        return companyDao.findAllParentCompaniesMap();
    }


    @Override
    public List<Map<String, Object>> findLightUsersAgentsByCompanyId(Long companyId) {
        return companyDao.findLightUsersAgentsByCompanyId(companyId);
    }


    @Override
    public List<Company> findAllBranchAndSubsByCompanyIdLight(Long companyId) {
        return companyDao.findAllBranchAndSubsByCompanyIdLight(companyId);
    }


    @Override
    public Company findParentCompany(Long subCompanyId) {
        return companyDao.findParentCompany(subCompanyId);
    }


    @Override
    public Company findParentCompany(Company subCompany) {
        return companyDao.findParentCompany(subCompany);
    }


    @Override
    public List<Company> findCompanyAllBranchAndSubsByCompanyIdLight(Long companyId) {
        return companyDao.findCompanyAllBranchAndSubsByCompanyIdLight(companyId);
    }


    @Override
    public List<Company> findAllParentCompaniesLight() {
        return companyDao.findAllParentCompaniesLight();
    }


    @Override
    public List<User> findCollectingAgents(List<UserRole> userRoles, String companyId) {
        return companyDao.findCollectingAgents(userRoles, companyId);
    }


    @Override
    public List<User> findUsersByCompanyIdAndRolesLight(Long companyId, List<UserRole> roles) {
        return companyDao.findUsersByCompanyIdAndRolesLight(companyId, roles);
    }


    @Override
    public List<String> getUserAdminEmails(User user) {
        List<String> adminEmails = new ArrayList<>();
        Long subCompanyId = user.getCompanyId();
        if (subCompanyId != null) {
            // FIXME Performance bottleneck here...
            Company company = findParentCompany(subCompanyId);
            List<User> users = findUsersByCompanyIdAndRolesLight(company.getId(), getAdminRoles());
            for (User admin : users) {
                adminEmails.add(admin.getUsername());
            }
        }
        adminEmails.add(user.getUsername());
        return adminEmails;
    }


    private List<UserRole> getAdminRoles() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole(AppConstants.ROLE_ADMIN));
        roles.add(new UserRole(AppConstants.ROLE_COMPANYADMIN));
        return roles;
    }


    @Override
    public List<String> getTicketSupportNofiticationAdminEmails(User user) {
        List<String> tickeSuportAdmins = new ArrayList<>();
        Long subCompanyId = user.getCompanyId();
        if (subCompanyId != null) {
            // FIXME Performance bottleneck here...
            Company company = findParentCompany(subCompanyId);
            List<User> users = findUsersByCompanyIdAndRolesLight(company.getId(), getTicketSupportNotificationRoles());
            for (User admin : users) {
                tickeSuportAdmins.add(admin.getUsername());
            }
        }
        tickeSuportAdmins.add(user.getUsername());
        return tickeSuportAdmins;
    }


    private List<UserRole> getTicketSupportNotificationRoles() {
        return new ArrayList<>(Collections.singleton(new UserRole(AppConstants.ROLE_TICKETSUPPORT_NOTIFICATION)));
    }
}
