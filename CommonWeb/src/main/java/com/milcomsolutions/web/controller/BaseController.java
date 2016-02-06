package com.milcomsolutions.web.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.commons.ConfigurationManager;
import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.dao.search.SearchInfo;
import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.service.BaseService;
import com.milcomsolutions.service.CompanyService;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.web.security.SecureAppUser;
import com.milcomsolutions.web.security.UserUtil;


public class BaseController implements InitializingBean {

    private static final Log LOG = LogFactory.getLog(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected BaseService genericService;

    @Autowired
    protected CompanyService companyService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ConfigurationManager configurationManager;

    protected SimpleDateFormat dateFormatter = new SimpleDateFormat(AppConstants.DATE_FORMAT_FULL_JAVA);

    protected DecimalFormat numberFormatter = new DecimalFormat(AppConstants.MONEY_FORMAT);

    protected DecimalFormat countFormatter = new DecimalFormat(AppConstants.COUNT_FORMAT);

    protected Gson gson = new Gson();

    protected ApplicationEventPublisher eventPublisher;


    protected SearchInfo getSearchInfo() {
        throw new NotYetImplementedException("No Implementation found");
    }


    // FIXME this gets called by every method...it's a model attribute!
    @ModelAttribute("searchInfo")
    public SearchInfo getControllerSearchInfo() {
        SearchInfo searchInfo = null;
        try {
            searchInfo = getSearchInfo();
            resolveCompaniesUsersCanActOn(searchInfo);
        } catch (Exception e) {
            BaseController.LOG.debug("problem getting Controller SearchInfo. Message: " + e.getMessage());
        }
        return searchInfo;
    }


    public SearchInfo getControllerSearchInfo(String searchValue) {
        SearchInfo searchInfo = getControllerSearchInfo();
        searchInfo.setSearchValue(searchValue);
        return searchInfo;
    }


    public void resolveCompaniesUsersCanActOn(SearchInfo searchInfo) {
        if (searchInfo != null) {
            if (StringUtils.isNotBlank(searchInfo.getCompanyId())) {
                if (isUserCompanyAdmin() || isUserSuperAdmin()) {
                    searchInfo.setCompanyTreeIds(findCompanyAndSubCompanyIds(Long.valueOf(searchInfo.getCompanyId())));
                } else {
                    searchInfo.setCompanyTreeIds(Collections.singleton(Long.valueOf(searchInfo.getCompanyId())));
                }
            }
        }
    }


    public void resolveCompanyTreeIdAfterFilter(SearchInfo searchInfo) {
        if (searchInfo.getBranchId() != null) {
            searchInfo.setCompanyTreeIds(Collections.singleton(searchInfo.getBranchId()));
        } else if (searchInfo.getParentCompanyId() != null) {
            searchInfo.setCompanyTreeIds(findCompanyAndSubCompanyIds(searchInfo.getParentCompanyId()));
        } else if (StringUtils.isNotBlank(searchInfo.getCompanyId()) && (isUserCompanyAdmin() || isUserSuperAdmin())) {
            searchInfo.setCompanyTreeIds(findCompanyAndSubCompanyIds(Long.valueOf(searchInfo.getCompanyId())));
        }
    }


    public Date getDateAtEndOFDay(Date date) {
        return new LocalDate(date.getTime()).toDateTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
    }


    public Date getDateAtStartOFDay(Date date) {
        return new LocalDate(date.getTime()).toDateTimeAtStartOfDay().toDate();
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT_JAVA);
        NumberFormat format = NumberFormat.getInstance();
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, format, true));
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, format, true));
    }


    protected User getUserLoggedIn() {
        return UserUtil.getAuthenticatedUser();
    }


    protected boolean isGuest() {
        return UserUtil.getAuthenticatedUser() == null;
    }


    SecureAppUser getSecuredUser() {
        return UserUtil.getSecuredUser();
    }


    public void addSuccessMessage(RedirectAttributes redAttribs, String message) {
        redAttribs.addFlashAttribute(AppConstants.SUCCESS_MSG, message);
    }


    public void addDefaultSuccessMessage(RedirectAttributes redAttribs) {
        redAttribs.addFlashAttribute(AppConstants.SUCCESS_MSG, AppConstants.DEFAULT_SUCCESS_MESSAGE);
    }


    public void addDefaultErrorMessage(RedirectAttributes redAttribs) {
        redAttribs.addFlashAttribute(AppConstants.ERROR_MSG, AppConstants.DEFAULT_ERROR_MESSAGE);
    }


    public void addErrorMessage(RedirectAttributes redAttribs, String message) {
        redAttribs.addFlashAttribute(AppConstants.ERROR_MSG, message);
    }


    public void addWarningMessage(RedirectAttributes redAttribs, String message) {
        redAttribs.addFlashAttribute(AppConstants.WARNING_MSG, message);
    }


    public boolean userHasRole(String role) {
        return getUserRoles().contains(new UserRole(role, null));
    }


    public boolean userHasRoles(String... roles) {
        List<UserRole> userRoles = new ArrayList<>();
        for (String role : roles) {
            userRoles.add(new UserRole(role, null));
        }
        return getUserRoles().containsAll(userRoles);
    }


    protected Map<Object, Object> prepareParameterMap(HttpServletRequest request) {
        Map<Object, Object> requestParameterMap = new HashMap<Object, Object>();
        requestParameterMap.putAll(request.getParameterMap());
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            requestParameterMap.put(name, request.getParameterValues(name)[0]);
        }
        return requestParameterMap;
    }


    protected Map<String, Object> resolveRequestParams(HttpServletRequest request) {
        Map<String, Object> requestParameterMap = new HashMap<>();
        requestParameterMap.putAll(request.getParameterMap());
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            requestParameterMap.put(name, request.getParameterValues(name)[0]);
        }
        return requestParameterMap;
    }


    public List<UserRole> getUserRoles() {
        try {
            return getSecuredUser().getRoles();
        } catch (Exception e) {
            BaseController.LOG.debug("problem getting user roles. Returning no roles. Message: " + e.getMessage());
        }
        return Collections.emptyList();
    }


    public List<UserRole> findUserRoles(User user) {
        return userService.getUserRoles(user);
    }


    protected void removeItems(Long[] ids, RedirectAttributes redAttrib, Class<?> classType) {
        try {
            BaseController.LOG.info(String.format("removing %s with ids: %s", classType.getSimpleName(), Arrays.toString(ids)));
            genericService.removeEntities(classType, Arrays.asList(ids));
            redAttrib.addFlashAttribute(AppConstants.SUCCESS_MSG, "Item(s) removed Successfully");
        } catch (Exception e) {
            redAttrib.addFlashAttribute(AppConstants.ERROR_MSG, AppConstants.DEFAULT_ERROR_MESSAGE);
            BaseController.LOG.error(e);
        }
    }


    public String getCompanyId() {
        return String.valueOf(getUserLoggedIn().getCompanyId());
    }


    public Long getRootCompanyId() {
        Company company = null;
        Long companyCId = null;
        if (StringUtils.isNotBlank(getCompanyId())) {
            companyCId = Long.valueOf(getCompanyId());
            while (true) {
                try {
                    company = companyService.findEntityById(Company.class, companyCId);
                } catch (Exception e) {
                    BaseController.LOG.error(e);
                }
                if (company != null) {
                    companyCId = company.getId();
                    if (company.getParentCompany() != null) {
                        companyCId = company.getParentCompany().getId();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return companyCId;
    }


    public boolean isUserCanActOnCompany(Long companyId) {
        Long myCompanyId = null;
        if (companyId != null) {
            myCompanyId = companyId;
        }
        if (myCompanyId == companyId) {
            return true;
        }
        List<Company> companies = companyService.findAllBranchAndSubsByCompanyIdLight(companyId);
        for (Company company : companies) {
            if (company.getId() == companyId) {
                return true;
            }
        }
        return false;
    }


    public List<Company> findCompanyAndSubCompanies(Long companyId) {
        List<Company> companiesSet = new ArrayList<>();
        if (companyId != null) {
            companiesSet = companyService.findCompanyAllBranchAndSubsByCompanyIdLight(companyId);
        }
        return companiesSet;
    }


    public List<Company> findCompanyAndSubCompanies(String companyId) {
        List<Company> companiesSet = new ArrayList<>();
        if (StringUtils.isNotBlank(companyId) && NumberUtils.isDigits(companyId)) {
            companiesSet = companyService.findCompanyAllBranchAndSubsByCompanyIdLight(Long.parseLong(companyId));
        }
        return companiesSet;
    }


    public Set<Long> findCompanyAndSubCompanyIds(Long companyId) {
        Set<Long> companiesSet = new HashSet<Long>();
        if (companyId != null) {
            companiesSet.add(companyId);
            List<Company> companies = companyService.findAllBranchAndSubsByCompanyIdLight(companyId);
            for (Company company : companies) {
                companiesSet.add(company.getId());
            }
        }
        return companiesSet;
    }


    public Set<Company> findCompaniesUsersCanActOn() {
        Long companyId = Long.parseLong(getCompanyId());
        List<Company> companiesSet = new ArrayList<>();
        if (companyId != null) {
            companiesSet = companyService.findCompanyAllBranchAndSubsByCompanyIdLight(companyId);
        }
        return new HashSet<>(companiesSet);
    }


    public Set<Long> getCompaniesUsersCanActOn() {
        Set<Long> companiesSet = new HashSet<Long>();
        if (StringUtils.isNotBlank(getCompanyId())) {
            companiesSet = findCompanyAndSubCompanyIds(Long.valueOf(getUserLoggedIn().getCompanyId()));
        }
        return companiesSet;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
    }


    public boolean isUserCompanyAdmin() {
        boolean isCompanyAdmin = false;
        try {
            return userHasRole(AppConstants.ROLE_COMPANYADMIN);
        } catch (Exception e) {
            BaseController.LOG.error("problem determining if user is a company admin. Message: " + e.getMessage());
        }
        return isCompanyAdmin;
    }


    public boolean isUserAnAdmin() {
        boolean isAdmin = false;
        try {
            return userHasRole(AppConstants.ROLE_ADMIN) || userHasRole(AppConstants.ROLE_COMPANYADMIN) || userHasRole(AppConstants.ROLE_VIEWONLY);
        } catch (Exception e) {
            BaseController.LOG.error("problem determining if user is an admin. Message: " + e.getMessage());
        }
        return isAdmin;
    }


    public boolean isUserSuperAdmin() {
        return userHasRole(AppConstants.ROLE_ADMIN);
    }


    protected Company findUserParentCompany() {
        try {
            return findParentCompany(getUserLoggedIn().getCompanyId());
        } catch (Exception e) {
            BaseController.LOG.error("problem finding user parent company. Message: " + e.getMessage());
        }
        return null;
    }


    protected Company findParentCompany(Company company) {
        try {
            return companyService.findParentCompany(company);
        } catch (Exception e) {
            BaseController.LOG.error("problem getting parent company...", e);
        }
        return null;
    }


    protected Company findParentCompany(Long companyId) {
        try {
            return companyService.findParentCompany(companyId);
        } catch (Exception e) {
            BaseController.LOG.error("problem getting parent company by id...", e);
        }
        return null;
    }


    protected String getCompanyCodeById(String companyId) {
        if (StringUtils.isNotBlank(companyId)) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", Long.parseLong(companyId));
            try {
                return (String) genericService.findEntityMapByParams(Company.class, params, "code").get("code");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    protected String getUserCompanyCodeById() {
        return getCompanyCodeById(getCompanyId());
    }


    // FIXME Performance
    protected List<Company> getAllParentCompanies() {
        if (isUserSuperAdmin()) {
            return companyService.findAllParentCompaniesLight();
        } else {
            return Collections.singletonList(companyService.findEntityById(Company.class, Long.parseLong(getCompanyId())));
        }
    }


    protected String getStringValue(String value, String defaultValue) {
        return StringUtils.defaultIfBlank(value, defaultValue);
    }


    protected String getStringValue(String value) {
        return getStringValue(value, AppConstants.NOT_AVAILABLE);
    }


    protected String getDateValue(Date value) {
        return value == null ? AppConstants.NOT_AVAILABLE : dateFormatter.format(value);
    }


    protected String getObjectValue(Object value) {
        return value == null ? AppConstants.NOT_AVAILABLE : String.valueOf(value);
    }


    protected String getDateValue(Object value) {
        return value == null ? AppConstants.NOT_AVAILABLE : getDateValue((Date) value);
    }


    protected String getNumberValue(Number value) {
        return value == null ? AppConstants.NOT_AVAILABLE : numberFormatter.format(value);
    }


    protected String getNumberValue(Object value) {
        if (value != null && NumberUtils.isNumber(String.valueOf(value))) {
            return numberFormatter.format(value);
        } else {
            return AppConstants.NOT_AVAILABLE;
        }
    }


    protected String getCountValue(Number value) {
        return value == null ? AppConstants.NOT_AVAILABLE : countFormatter.format(value);
    }


    protected String getCountValue(Object value) {
        if (value != null && NumberUtils.isNumber(String.valueOf(value))) {
            return countFormatter.format(value);
        } else {
            return AppConstants.NOT_AVAILABLE;
        }
    }


    protected final <T extends BaseModel> void setAscendingOrder(String orderDirection, RecordDisplayUtility<T> displayUtility) {
        try {
            displayUtility.setOrdered(true);
            displayUtility.setAscendingOrder(orderDirection.equalsIgnoreCase("asc"));
            displayUtility.getSearchInfo().setOrderDirection(orderDirection);
        } catch (Exception e) {
            BaseController.LOG.error("problem order not good. Message: " + e.getMessage());
            displayUtility.setAscendingOrder(true);
        }
    }


    public void publishApplicationEvent(ApplicationEvent event) {
        this.genericService.publishApplicationEvent(event);
    }


    protected List<UserRole> getCustomerServiceRoles() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole(AppConstants.ROLE_TICKETSUPPORT));
        roles.add(new UserRole(AppConstants.ROLE_COMPANYADMIN));
        roles.add(new UserRole(AppConstants.ROLE_ADMIN));
        return roles;
    }
}
