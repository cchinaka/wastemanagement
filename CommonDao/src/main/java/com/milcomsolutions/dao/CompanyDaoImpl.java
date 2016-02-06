package com.milcomsolutions.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;


@Repository("companyDao")
public class CompanyDaoImpl extends DefaultDao implements CompanyDao {

    private static final Log LOG = LogFactory.getLog(CompanyDaoImpl.class);


    @Override
    public List<String> loadBranchNamesByCompanyId(Long id) {
        String jpql = "select c.name from Company c where c.parentCompany.id = :companyId";
        return getEntityManager().createQuery(jpql, String.class).setParameter("companyId", id).getResultList();
    }


    @Override
    public List<Company> findAllParentCompanies() {
        String jpql = "select c from Company c where c.parentCompany is null";
        return getEntityManager().createQuery(jpql, Company.class).getResultList();
    }


    @Override
    public List<Company> findAllParentCompaniesLight() {
        String jpql = String.format("select new Company(%s) from Company c where c.parentCompany is null", Company.INIT_LIGHT);
        return getEntityManager().createQuery(jpql, Company.class).getResultList();
    }


    @Override
    public List<User> findtUsersByCompanyId(Long companyId) {
        String jpql = "select u from User u where u.companyId = :companyId";
        return getEntityManager().createQuery(jpql, User.class).setParameter("companyId", companyId).getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findLightUsersByCompanyId(Long companyId) {
        String jpql = "select new map(u.id as id, u.name as name) from User u where u.companyId = :companyId order by u.name";
        return getEntityManager().createQuery(jpql).setParameter("companyId", companyId.toString()).getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findLightUsersAgentsByCompanyId(Long companyId) {
        String jpql = "select new map(u.id as id, u.name as name) from User u, in(u.roles)r where u.companyId = :companyId and r.code in (:roleCodes) order by u.name";
        return getEntityManager().createQuery(jpql).setParameter("companyId", companyId.toString())
                .setParameter("roleCodes", Collections.singletonList("ROLE_COLLECTINGAGENT")).getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findAllParentCompaniesMap() {
        String jpql = "select new map(c.id as id, c.code as code, c.name as name) from Company c where c.parentCompany is null";
        return getEntityManager().createQuery(jpql).getResultList();
    }


    @Override
    public Company findParentCompany(Long subCompanyId) {
        return findParentCompany(findEntityById(Company.class, subCompanyId));
    }


    @Override
    public Company findParentCompany(Company subCompany) {
        if (subCompany.getParentCompany() != null) {
            return findParentCompany(subCompany.getParentCompany());
        } else {
            return subCompany;
        }
    }


    @Override
    public List<Company> findAllBranchAndSubsByCompanyIdLight(Long parentCompanyId) {
        List<Company> subCompanies = new ArrayList<>();
        String companyQl = "select new Company(c.id, c.name, c.code, c.parentCompany.id) from Company c where c.parentCompany.id = :parentCompanyId and c.parentCompany.id <> c.id order by c.name";
        subCompanies.addAll(getEntityManager().createQuery(companyQl, Company.class).setParameter("parentCompanyId", parentCompanyId).getResultList());
        if (CollectionUtils.isNotEmpty(subCompanies)) {
            // TODO Review: Not sure if this works.
            List<Company> subs = new ArrayList<>();
            for (Company company : subCompanies) {
                subs.addAll(findAllBranchAndSubsByCompanyIdLight(company.getId()));
            }
            subCompanies.addAll(subs);
        }
        return subCompanies;
    }


    @Override
    public List<Company> findCompanyAllBranchAndSubsByCompanyIdLight(Long companyId) {
        // FIXME CC. WHAT THE FUCK MAN?
        List<Company> companies = new ArrayList<>();
        Company all = getEntityManager()
                .createQuery("select new Company(c.id, c.name, c.code, p.id) from Company c left join c.parentCompany p where c.id = :id", Company.class)
                .setParameter("id", companyId).getSingleResult();
        companies.add(all);
        companies.addAll(findAllBranchAndSubsByCompanyIdLight(companyId));
        return companies;
    }


    @Override
    public List<User> findCollectingAgents(List<UserRole> userRoles, String companyId) {
        List<User> users = new ArrayList<>();
        try {
            CompanyDaoImpl.LOG.info("company id: " + companyId);
            String jpql = "select new User(u.id, u.username,  u.userDetail.firstName, u.userDetail.lastName) from User u, in(u.roles) r where r.code = :code";
            if (userRoles.contains(new UserRole(AppConstants.ROLE_ADMIN))) {
                users = getEntityManager().createQuery(jpql, User.class).setParameter("code", AppConstants.ROLE_COLLECTINGAGENT).getResultList();
            } else if (userRoles.contains(new UserRole(AppConstants.ROLE_COMPANYADMIN))) {
                List<String> companyIds = findMyCompanyAndAllMyBranchAndSubIDSByCompanyIdLightAsString(companyId);
                CompanyDaoImpl.LOG.info("company ids: " + companyIds);
                jpql += " and u.companyId in (:companyIds)";
                users = getEntityManager().createQuery(jpql, User.class).setParameter("code", AppConstants.ROLE_COLLECTINGAGENT)
                        .setParameter("companyIds", companyIds).getResultList();
            } else {
                jpql += " and u.companyId = :companyId";
                users = getEntityManager().createQuery(jpql, User.class).setParameter("code", AppConstants.ROLE_COLLECTINGAGENT)
                        .setParameter("companyId", companyId).getResultList();
            }
            CompanyDaoImpl.LOG.info("generated query for collecting agents: " + jpql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    private List<String> findMyCompanyAndAllMyBranchAndSubIDSByCompanyIdLightAsString(String companyId) {
        List<Company> companies = findAllBranchAndSubsByCompanyIdLight(Long.parseLong(companyId));
        List<String> companyIds = new ArrayList<>();
        companyIds.add(companyId);
        for (Company company : companies) {
            companyIds.add(company.getId() + StringUtils.EMPTY);
        }
        return companyIds;
    }


    @Override
    public List<User> findUsersByCompanyIdAndRolesLight(Long companyId, List<UserRole> roles) {
        return getEntityManager().createQuery(
                String.format("select new User(%s) from User u, in(u.roles)r where u.companyId = :companyId and r.code in (:roleCode) ", User.INIT_QL),
                User.class).setParameter("companyId", String.valueOf(companyId)).setParameter("roleCode", getRoleCodes(roles)).getResultList();
    }
}
