package com.milcomsolutions.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.commons.security.AppPasswordEncoder;
import com.milcomsolutions.commons.security.SecurityUtil;
import com.milcomsolutions.dao.UserDao;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.License;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserDetails;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.entity.core.VerificationCode;
import com.milcomsolutions.service.NotificationService;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.service.VerificationService;
import com.milcomsolutions.vo.api.LicenseVo;
import com.milcomsolutions.vo.api.RegistrationVO;


@Service("userService")
@Transactional
public class UserServiceImpl extends GenericServiceImpl implements UserService {

    private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    NotificationService notificationService;

    @Autowired
    VerificationService verificationService;


    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }


    @Override
    public User resolveAndSaveUserDetails(Map<String, String> details) throws Exception {
        Long companyId = Long.valueOf(details.get("companyId"));
        details.get("customerId");
        String customerNo = details.get("customerNo");// customerInfo[1].trim();
        String surname = details.get("surname");// customerInfo[2].trim();
        String firstname = details.get("firstname");// customerInfo[3].trim();
        String phoneNumber = StringUtils.defaultIfBlank(details.get("phoneNumber"), StringUtils.EMPTY);
        if (StringUtils.isNotBlank(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("O", "0");
            if (!phoneNumber.startsWith("0")) {
                phoneNumber = "0" + phoneNumber;
            }
        }
        String username = surname + "." + firstname + "@" + phoneNumber;
        User user = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customerNo", customerNo);
            List<User> ls = findEntitiesByParams(User.class, params);
            if (ls != null && !ls.isEmpty()) {
                user = ls.get(0);
            }
        } catch (Exception e) {
            UserServiceImpl.LOG.error(String.format("User %s not found", customerNo));
        }
        if (user == null) {
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("userDetail.phoneNumber", phoneNumber);
                List<User> ls = findEntitiesByParams(User.class, params);
                if (ls != null && !ls.isEmpty()) {
                    user = ls.get(0);
                }
            } catch (Exception e) {
                UserServiceImpl.LOG.error(String.format("User %s not found", phoneNumber));
            }
        }
        if (user == null) {
            user = new User();
            UserDetails ud = new UserDetails(firstname, surname, username);
            user.setUserDetail(ud);
            ud.setPhoneNumber(phoneNumber);
            user.setCreationDate(new Date());
            user.setCompanyId(companyId);
            user.setActive(true);
            user.setUsername(username);
            user.setVerified(true);
            user.getRoles().add(new UserRole("ROLE_USER"));
            user = saveEntityTxn(user);
        }
        return user;
    }


    @Override
    public User saveUser(User user, boolean updatePassword) throws Exception {
        if (updatePassword) {
            user.encodePassword();
        }
        user = registerUserOnly(user);
        return user;
    }


    @Override
    public UserRole findRoleByCode(String code) {
        return userDao.findRoleByCode(code);
    }


    @Override
    public int removeUsersById(List<String> usernames) throws Exception {
        throw new NotImplementedException("Not Yet Implemented");
    }


    @Override
    public String[] resolveUserEmailAddress(String[] userNames) {
        throw new NotImplementedException("Not Yet Implemented");
    }


    @Override
    public List<UserRole> getUserRoles(User user) {
        return userDao.getUserRoles(user);
    }


    @Override
    public User registerUser(RegistrationVO userVo) throws Exception {
        userVo.getUser().setPassword(AppPasswordEncoder.generatePassword(userVo.getUser().getPassword(), userVo.getUser().getUserDetail().getEmail()));
        User user = userVo.getUser();
        user.getRoles().add(findRoleByCode("ROLE_USER"));
        user.setAccountNonLocked(false);
        user.setVerified(false);
        user = saveEntity(user);
        sendEmailVerificationCode(userVo, user);
        return user;
    }


    @Override
    public void sendEmailVerificationCode(RegistrationVO userVo, User user) throws Exception {
        VerificationCode vcode = verificationService.createAndSaveVerificationCode(user);
        notificationService.sendRegisterationNotification(userVo, vcode);
    }


    @Override
    public boolean updateUserLastLoginById(Long userId) {
        return userDao.updateUserLastLoginById(userId);
    }


    @Override
    public void addLicense(LicenseVo licenseVo) throws Exception {
        installLicense(licenseVo);
        userDao.addUserRole(licenseVo.getUser(), AppConstants.ROLE_LICENSE);
    }


    @Override
    public User install(LicenseVo licenseVo) throws Exception {
        User user = installUser(licenseVo);
        installLicense(licenseVo);
        return user;
    }


    private License installLicense(LicenseVo licenseVo) throws Exception {
        License license = updateLicenseWithPropsFile(licenseVo);
        Long fileSize = 0l;
        byte[] lc = null;
        String fileName = StringUtils.EMPTY;
        if (licenseVo.getFile() != null) {
            fileName = licenseVo.getFile().getName();
            fileSize = licenseVo.getFile().length();
            lc = FileUtils.readFileToByteArray(licenseVo.getFile());
        } else {
            fileSize = licenseVo.getMpFile().getSize();
            fileName = licenseVo.getMpFile().getOriginalFilename();
            lc = licenseVo.getMpFile().getBytes();
        }
        license.setLicenseFile(lc);
        license.setFileName(fileName);
        license.setFileSize(fileSize);
        license.setActive(true);
        license = saveEntity(license);
        return license;
    }


    private License updateLicenseWithPropsFile(LicenseVo licenseVo) throws IOException, ParseException {
        Properties prop = resolveProperties(licenseVo);
        licenseVo.getLicense().setLicenseKey((String) prop.get(AppConstants.LICENSE_SERIAL));
        licenseVo.getLicense().setCreationDate(new Date(Long.parseLong((String) prop.get(AppConstants.LICENSE_CRT_DATE))));
        licenseVo.getLicense().setExpiryDate(new Date(Long.parseLong((String) prop.get(AppConstants.LICENSE_EXP_DATE))));
        licenseVo.getLicense().setCompanyId((String) prop.get(AppConstants.LICENSE_COMP_ID));
        licenseVo.getLicense().setCompanyName(safeGetField(prop.get(AppConstants.LICENSE_COMP_NAME)));
        return licenseVo.getLicense();
    }


    private Properties getPropertiesFromLicenseFile(InputStream inputStream) throws IOException {
        // TODO decrypt the content of this file
        Properties prop = new Properties();
        prop.load(inputStream);
        return prop;
    }


    private User installUser(LicenseVo licenseVo) throws Exception {
        Properties prop = resolveProperties(licenseVo);
        User user = new User();
        user.setVerified(true);
        String userEmail = licenseVo.getEmail();
        user.getUserDetail().setEmail(userEmail);
        user.setUsername(userEmail);
        String tempPass = "password";
        user.setPassword(tempPass);
        user.encodePassword();
        user.getUserDetail().setFirstName("System");
        user.getUserDetail().setLastName("Administrator");
        user.getRoles().addAll(findRolesByCodes(new String[] { AppConstants.ROLE_ADMIN, AppConstants.ROLE_USER, AppConstants.ROLE_LICENSE }));
        user.setTempPass(tempPass);
        user.setVerified(true);
        Company company = createNewCompany(licenseVo);
        user.setCompany(company);
        company.getUsers().add(user);
        company = createEntity(company);
        return company.getUsers().iterator().next();
    }


    private Company createNewCompany(LicenseVo licenseVo) {
        Company company = new Company();
        company.setCode(AppConstants.LICENSE_COMP_ID);
        company.setName(AppConstants.LICENSE_COMP_NAME);
        return company;
    }


    private Properties resolveProperties(LicenseVo licenseVo) throws FileNotFoundException, IOException {
        InputStream input = null;
        if (licenseVo.getFile() != null) {
            input = new FileInputStream(licenseVo.getFile());
        } else {
            input = licenseVo.getMpFile().getInputStream();
        }
        Properties prop = getPropertiesFromLicenseFile(input);
        return prop;
    }


    @Override
    public User registerUserOnly(User user) throws Exception {
        return saveEntity(user);
    }


    @Override
    public List<UserRole> findRolesByCodes(String[] codes) {
        return userDao.findRolesByCodes(codes);
    }


    public String safeGetField(Object info) {
        return String.valueOf(ObjectUtils.defaultIfNull(info, StringUtils.EMPTY));
    }


    @Override
    public void updateUserPassword(User user) throws Exception {
        saveUser(user, true);
    }


    @Override
    public boolean resetPassword(String email) throws Exception {
        boolean reset = false;
        User user = userDao.findUserByUsername(email);
        if (user != null) {
            String tempPass = SecurityUtil.generateRandomCaharacters(8);
            user.setPassword(tempPass);
            saveUser(user, true);
            notificationService.sendPasswordResetMail(user, tempPass);
            reset = true;
        }
        return reset;
    }


    @Override
    public String getUserEmailByUserId(Long userId) {
        try {
            return dao.getEntityManager().createQuery("select u.userDetail.email from User u where u.id = :userId", String.class).setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            UserServiceImpl.LOG.error("a problem was encountered while getting user email...", e);
        }
        return StringUtils.EMPTY;
    }


    @Override
    public List<User> findUsersByRole(UserRole role) {
        return userDao.findUsersByRole(role);
    }


    @Override
    public void sendUserRegistrationEmailVerificationCode(String email) throws Exception {
        User user = findUserByUsername(email);
        RegistrationVO userVo = new RegistrationVO();
        userVo.setUser(user);
        VerificationCode vcode = verificationService.createAndSaveVerificationCode(user);
        notificationService.sendRegisterationNotification(userVo, vcode);
    }


    @Override
    public void saveUserByAdmin(RegistrationVO uservo) throws Exception {
        User user = uservo.getUser();
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(AppPasswordEncoder.generatePassword(user.getPassword(), user.getUserDetail().getEmail()));
        } else {
            user.setPassword(user.getCurrentPassword());
        }
        if (user.getRoleCodes() != null) {
            user.setRoles(getUserRolesByCodes(user.getRoleCodes()));
        }
        user.getRoles().add(new UserRole(AppConstants.ROLE_USER));
        user.setAccountNonLocked(true);
        user = saveEntity(user);
        if (uservo.isSendVerificationEmail()) {
            sendEmailVerificationCode(uservo, user);
        }
    }


    private Set<UserRole> getUserRolesByCodes(List<String> roleCodes) {
        Set<UserRole> roles = new HashSet<>();
        for (String roleId : roleCodes) {
            roles.add(new UserRole(roleId));
        }
        return roles;
    }


    @Override
    public Company findParentCompanyLigth(String companyId) {
        Company company = null;
        return company;
    }


    @Override
    public List<UserRole> findRolesByUserRoles(List<UserRole> userRoles) {
        return userDao.findRolesByUserRoles(userRoles);
    }


    @Override
    public List<Company> findAllCompaniesLight() {
        return userDao.findAllCompaniesLight();
    }


    @Override
    public List<User> findUserByIdentifiers(String username, String customerNo, String phoneNumber) {
        return userDao.findUserByIdentifiers(username, customerNo, phoneNumber);
    }


    @Override
    public List<User> findUsersByRolesLight(List<UserRole> userRoles, String initQl) {
        return userDao.findUsersByRolesLight(userRoles, initQl);
    }


    @Override
    public Long findUserCountByIdentifiers(String username, String customerNo, String phoneNumber) {
        return userDao.findUserCountByIdentifiers(username, customerNo, phoneNumber);
    }


    @Override
    public List<Map<String, Object>> findUsersByCompanyIdsLight(Set<Long> companiesUsersCanActOn) {
        return userDao.findUsersByCompanyIdsLight(companiesUsersCanActOn);
    }


    @Override
    public Company getUserCompanyLight(User user) {
        return userDao.getUserCompanyLight(user);
    }
}
