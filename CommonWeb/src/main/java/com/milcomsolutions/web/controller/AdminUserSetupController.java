package com.milcomsolutions.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.dao.search.SearchInfo;
import com.milcomsolutions.dao.search.UserSearchInfo;
import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.vo.api.RegistrationVO;


@Controller
@RequestMapping("/admin/usersetup")
public class AdminUserSetupController extends BaseController {

    private static final Log LOG = LogFactory.getLog(AdminUserSetupController.class);



    @ModelAttribute("uservo")
    public RegistrationVO getRegistrationVO() {
        return new RegistrationVO();
    }


    @RequestMapping(method = RequestMethod.GET)
    public String start(Model model) {
        return "users";
    }


    @Override
    protected SearchInfo getSearchInfo() {
        return new UserSearchInfo(getUserRoles(), getCompanyId());
    }


    @RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String, Object> list(@RequestParam(value = "draw", required = false) Integer pageNo,
            @RequestParam(value = "length", required = false) Integer pageSize, @RequestParam(value = "start", required = false) Integer startIndex,
            @RequestParam(value = "search[value]", required = false) String searchValue) {
        SearchInfo searchInfo = getControllerSearchInfo();
        searchInfo.setSearchValue(searchValue);
        Map<String, Object> resultMap = new HashMap<>();
        RecordDisplayUtility<User> displayUtility = RecordDisplayUtility.createInstanceWithSearchInfo(User.class, searchInfo);
        displayUtility.setOrdered(true);
        displayUtility.setAscendingOrder(false);
        displayUtility.setPageNo(pageNo);
        displayUtility.setPageSize(pageSize);
        displayUtility.setCurrentStartIndex(startIndex);
        displayUtility = userService.findAllEntitiesPaginated(displayUtility);
        resultMap.put("data", prepareData(displayUtility));
        resultMap.put("recordsTotal", displayUtility.getRecordCount());
        resultMap.put("recordsFiltered", displayUtility.getRecordCount());
        resultMap.put("draw", displayUtility.getPageNo());
        return resultMap;
    }


    private Object prepareData(RecordDisplayUtility<User> displayUtility) {
        List<Object[]> data = new ArrayList<>();
        int index = displayUtility.getStartIndex();
        for (User user : displayUtility.getRecords()) {
            try {
                data.add(new Object[] { String.valueOf(++index),
                        String.format("<input type='checkbox' name='ids' value='%s' class='toggle-checkee'/>", user.getId()), user.getName(),
                        user.getUserDetail().getEmail(), dateFormatter.format(user.getCreationDate()),
                        String.format("<i class='fa %s'>", (user.isActive() ? "fa-check" : "fa-times")),
                        String.format("<i class='fa %s'>", (user.isVerified() ? "fa-check" : "fa-times")) });
            } catch (Exception e) {
                LOG.error(String.format("problem load order %s. Message: %s", user.getId(), e.getMessage()));
            }
        }
        return data;
    }


    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public String loadUser(@ModelAttribute("uservo") RegistrationVO uservo, @PathVariable Long userId, Model model) throws AccessDeniedException {
        if (userId > 0) {
            User user = userService.findEntityById(User.class, userId);
            user.prepareRoleIds(findUserRoles(user));
            if (!userHasRoles(AppConstants.ROLE_ADMIN, AppConstants.ROLE_VIEWONLY)) {
                if ((user.getRoleCodes().contains(AppConstants.ROLE_ADMIN) || user.getRoleCodes().contains(AppConstants.ROLE_LICENSE)) && !isUserAnAdmin()) {
                    throw new AccessDeniedException("Cannot access loadUser with your current roles");
                }
            }
            uservo.setUser(user);
        }
        model.addAttribute("uservo", uservo);
        List<Company> companies = new ArrayList<>();
        if (isUserSuperAdmin()) {
            companies = companyService.findAllEntitiesLight(Company.class, Company.INIT_LIGHT);
        } else {
            companies = findCompanyAndSubCompanies(getCompanyId());
        }
        model.addAttribute("companies", companies);
        model.addAttribute("roles", findRoles());
        // model.addAttribute("meters", meterService.findAllEntities(Meter.class));
        return "edit-user";
    }


    private List<UserRole> findRoles() {
        return userService.findRolesByUserRoles(getUserRoles());
        // return userService.findAllEntities(UserRole.class);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_COMPANYADMIN')")
    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("uservo") RegistrationVO uservo, RedirectAttributes redAttribs) {
        try {
            userService.saveUserByAdmin(uservo);
            addDefaultSuccessMessage(redAttribs);
        } catch (Exception e) {
            addDefaultErrorMessage(redAttribs);
        }
        return "redirect:/admin/usersetup";
    }
}
