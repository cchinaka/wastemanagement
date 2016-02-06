package com.milcomsolutions.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.vo.api.LicenseVo;
import com.milcomsolutions.vo.api.RegistrationVO;


public interface UserService extends BaseService {

    User findUserByUsername(String username);


    User saveUser(User user, boolean updatePassword) throws Exception;


    UserRole findRoleByCode(String string);


    User registerUser(RegistrationVO uservo) throws Exception;


    int removeUsersById(List<String> usernames) throws Exception;


    String[] resolveUserEmailAddress(String[] userNames);


    List<UserRole> getUserRoles(User user);


    boolean updateUserLastLoginById(Long userId);


    List<UserRole> findRolesByCodes(String[] codes);


    User registerUserOnly(User user) throws Exception;


    void updateUserPassword(User user) throws Exception;


    void addLicense(LicenseVo licenseVo) throws Exception;


    User install(LicenseVo licenseVo) throws Exception;


    boolean resetPassword(String email) throws Exception;


    String getUserEmailByUserId(Long userId);


    List<User> findUsersByRole(UserRole userRole);


    void sendEmailVerificationCode(RegistrationVO userVo, User user) throws Exception;


    void sendUserRegistrationEmailVerificationCode(String email) throws Exception;


    void saveUserByAdmin(RegistrationVO uservo) throws Exception;


    User resolveAndSaveUserDetails(Map<String, String> details) throws Exception;


    List<UserRole> findRolesByUserRoles(List<UserRole> userRoles);


    List<User> findUserByIdentifiers(String username, String customerNo, String phoneNumber);


    Long findUserCountByIdentifiers(String username, String customerNo, String phoneNumber);


    List<User> findUsersByRolesLight(List<UserRole> userRoles, String initQl);


    List<Map<String, Object>> findUsersByCompanyIdsLight(Set<Long> companiesUsersCanActOn);


    Company findParentCompanyLigth(String companyId);


    List<Company> findAllCompaniesLight();


    Company getUserCompanyLight(User user);
}
