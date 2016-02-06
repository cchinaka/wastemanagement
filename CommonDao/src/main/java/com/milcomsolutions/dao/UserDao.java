package com.milcomsolutions.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;


@Repository("userDao")
public class UserDao extends DefaultDao {

    private static final Log LOG = LogFactory.getLog(UserDao.class);


    public User findUserByUsername(String username) {
        List<User> users = getEntityManager().createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username)
                .getResultList();
        if (CollectionUtils.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }


    public List<UserRole> getUserRoles(User user) {
        UserDao.LOG.info("user info: " + user);
        return getEntityManager().createQuery("select r from User u, in(u.roles)r where u.id = :id", UserRole.class).setParameter("id", user.getId())
                .getResultList();
    }


    public List<UserRole> getUserRolesLight(User user) {
        UserDao.LOG.info("user info: " + user);
        return getEntityManager().createQuery("select new UserRole(r.id) from User u, in(u.roles)r where u.id = :id", UserRole.class)
                .setParameter("id", user.getId()).getResultList();
    }


    public void addUserRole(User user, String code) throws Exception {
        user = getEntityManager().find(User.class, user.getId());
        if (!user.hasRole(code)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("code", code);
            user.getRoles().add(findEntityByParams(UserRole.class, params));
            saveEntity(user);
        }
    }


    public List<UserRole> findRolesByCodes(String... codes) {
        String jpql = "select u from UserRole u where u.code in (:codes)";
        return getEntityManager().createQuery(jpql, UserRole.class).setParameter("codes", Arrays.asList(codes)).getResultList();
    }


    public UserRole findRoleByCode(String code) {
        return findRolesByCodes(code).get(0);
    }


    public boolean updateUserLastLoginById(Long userId) {
        Query query = getEntityManager().createQuery("update User u set u.lastLoggedIn = :lastLoggedIn where u.id = :id");
        query.setParameter("lastLoggedIn", new Date());
        query.setParameter("id", userId);
        return query.executeUpdate() > 0;
    }


    public List<User> findUsersByRole(UserRole role) {
        return getEntityManager().createQuery("select u from User u, in(u.roles)r where r.code = :roleCode ", User.class)
                .setParameter("roleCode", role.getCode()).getResultList();
    }


    public List<UserRole> findRolesByUserRoles(List<UserRole> userRoles) {
        List<UserRole> roles = findAllEntities(UserRole.class);
        roles.remove(new UserRole(AppConstants.ROLE_USER));
        if (!userRoles.contains(new UserRole(AppConstants.ROLE_ADMIN))) {
            roles.removeAll(Arrays.asList(new UserRole(AppConstants.ROLE_LICENSE), new UserRole(AppConstants.ROLE_ADMIN)));
        }
        return roles;
    }


    public List<Company> findAllCompaniesLight() {
        return getEntityManager().createQuery(String.format("select new Company(%s) from Company", Company.INIT_LIGHT), Company.class).getResultList();
    }


    public Long findUserCountByIdentifiers(String username, String customerNo, String phoneNumber) {
        return getEntityManager()
                .createQuery("select count(u.id) from User u where u.username = :username or u.customerNo=:customerNo or u.userDetail.phoneNumber=:phoneNumber",
                        Number.class)
                .setParameter("username", username).setParameter("customerNo", customerNo).setParameter("phoneNumber", phoneNumber).getSingleResult()
                .longValue();
    }


    public List<User> findUserByIdentifiers(String username, String customerNo, String phoneNumber) {
        return getEntityManager()
                .createQuery("select u from User u where u.username = :username or u.customerNo=:customerNo or u.userDetail.phoneNumber=:phoneNumber",
                        User.class)
                .setParameter("username", username).setParameter("customerNo", customerNo).setParameter("phoneNumber", phoneNumber).getResultList();
    }


    public List<User> findUsersByRolesLight(List<UserRole> userRoles, String initQl) {
        return getEntityManager()
                .createQuery(String.format("select new User(%s) from User u, in(u.roles)r where r.code in (:roleCode) ", User.INIT_QL), User.class)
                .setParameter("roleCode", getRoleCodes(userRoles)).getResultList();
    }


    @SuppressWarnings({ "unchecked" })
    public List<Map<String, Object>> findUsersByCompanyIdsLight(Set<Long> companiesUsersCanActOn) {
        String userQl = String.format(
                "select new map(u.id as id, "
                        + "CASE WHEN (u.userDetail.dataloadName is not null) THEN trim(both from concat(u.userDetail.dataloadName,'(',username,')')) ELSE  trim(both from concat(u.name,'(',username,')')) END as name) "
                        + "from User u where u.active = true %s order by u.name",
                CollectionUtils.isNotEmpty(companiesUsersCanActOn) ? "u.companyId in (:companyIds)" : "");
        Query query = getEntityManager().createQuery(userQl);
        if (CollectionUtils.isNotEmpty(companiesUsersCanActOn)) {
            List<String> companyIds = new ArrayList<>();
            for (Long companyId : companiesUsersCanActOn) {
                companyIds.add(String.valueOf(companyId));
            }
            query.setParameter("companyIds", companyIds);
        }
        return query.getResultList();
    }


    public Company getUserCompanyLight(User user) {
        String companyQl = "select new Company(u.company.id, u.company.name, u.company.code) from User u where u.id = :id";
        return getEntityManager().createQuery(companyQl, Company.class).setParameter("id", user.getId()).getSingleResult();
    }
}
