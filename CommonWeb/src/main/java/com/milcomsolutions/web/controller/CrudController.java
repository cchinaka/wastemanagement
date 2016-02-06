package com.milcomsolutions.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.dao.search.RecordDisplayUtility;
import com.milcomsolutions.dao.search.SearchInfo;
import com.milcomsolutions.entity.core.BaseModel;
import com.milcomsolutions.utils.CrudFilter;


public abstract class CrudController<T extends BaseModel> extends BaseController {

    private static final Log LOG = LogFactory.getLog(CrudController.class);


    protected abstract String getRedirectPageRoot();


    @RequestMapping(method = RequestMethod.GET)
    public abstract String start(Model model);


    public abstract Class<T> getEntityType();


    protected abstract String[] createRow(T record);


    @Override
    protected abstract SearchInfo getSearchInfo();


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public abstract String editRecord(@PathVariable Long id, Model model);


    @ModelAttribute("crudFilter")
    public CrudFilter getCrudFilter() {
        return new CrudFilter();
    }


    protected List<String[]> prepareData(RecordDisplayUtility<T> displayUtility) {
        List<String[]> data = new ArrayList<>();
        int index = displayUtility.getStartIndex();
        for (T t : displayUtility.getRecords()) {
            try {
                data.add((String[]) ArrayUtils.addAll(
                        ArrayUtils.addAll(new String[] { String.valueOf(++index),
                                String.format("<input type='checkbox' name='ids' value='%s' class='toggle-checkee'/>", t.getId()) }, createRow(t)),
                        new String[] { String.format("<i class='fa %s'>", (t.isActive() ? "fa-check" : "fa-times")) }));
            } catch (Exception e) {
                e.printStackTrace();
                CrudController.LOG.error(String.format("problem load %s %s. Message: %s", t.getClass(), t.getId(), e.getMessage()));
            }
        }
        return data;
    }


    public Map<String, Object> buildResultMap(RecordDisplayUtility<T> displayUtility) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", prepareData(displayUtility));
        resultMap.put("recordsTotal", displayUtility.getRecordCount());
        resultMap.put("recordsFiltered", displayUtility.getRecordCount());
        resultMap.put("draw", displayUtility.getPageNo());
        return resultMap;
    }


    @RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String, Object> loadJsonData(@ModelAttribute("searchInfo") SearchInfo searchInfo, @ModelAttribute CrudFilter crudFilter,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", required = false, defaultValue = "3") Integer orderColumn,
            @RequestParam(value = "order[0][dir]", required = false, defaultValue = "asc") String orderDirection) {
        searchInfo.setSearchValue(searchValue);
        searchInfo.setOrderColumn(orderColumn);
        resolveCompanyTreeIdAfterFilter(searchInfo);
        RecordDisplayUtility<T> displayUtility = getDisplayUtility(crudFilter, searchInfo);
        setAscendingOrder(orderDirection, displayUtility);
        configureDisplayUtility(displayUtility);
        displayUtility = genericService.findAllEntitiesPaginated(displayUtility);
        return buildResultMap(displayUtility);
    }


    @RequestMapping(value = "print/{format}", method = { RequestMethod.GET, RequestMethod.POST })
    public String exportData(@ModelAttribute("searchInfo") SearchInfo searchInfo, @ModelAttribute CrudFilter crudFilter,
            @RequestParam(value = "search[value]", required = false) String searchValue, @RequestParam(value = "reportTitle") String reportTitle,
            @RequestParam(value = "exportKey", required = false, defaultValue = StringUtils.EMPTY) String exportKey, @PathVariable String format,
            RedirectAttributes redAttribs) {
        // searchInfo.setSearchValue(searchValue);
        // searchInfo.setExportKey(exportKey);
        // resolveCompanyTreeIdAfterFilter(searchInfo);
        // RecordDisplayUtility<T> displayUtility = getDisplayUtility(crudFilter, searchInfo);
        // configureDisplayUtility(displayUtility);
        // ReportRequestVO reportRequestVO = new ReportRequestVO();
        // reportRequestVO.setReportId(StringUtils.EMPTY);
        // request.getSession().removeAttribute(AppConstants.REPORT_SEARCH_INFO_DETAILS);
        // request.getSession().setAttribute(AppConstants.REPORT_SEARCH_INFO_DETAILS, searchInfo);
        // request.getSession().removeAttribute(AppConstants.REPORT_TITLE);
        // request.getSession().setAttribute(AppConstants.REPORT_TITLE, reportTitle);
        // ReportViewFormat printFormat = ReportViewFormat.getFormatByDescription(format);
        // switch (format.toUpperCase().trim()) {
        // case "EXCEL":
        // printFormat = ReportViewFormat.PDF;
        // break;
        // }
        return "redirect:/user/export/format/";// + printFormat.getDescription();
    }


    protected void configureDisplayUtility(RecordDisplayUtility<T> displayUtility) {
        // give unique behaviour to display utility
    }


    protected RecordDisplayUtility<T> getDisplayUtility(CrudFilter crudFilter, SearchInfo searchInfo) {
        RecordDisplayUtility<T> displayUtility = RecordDisplayUtility.createInstanceWithSearchInfo(getEntityType(), searchInfo, crudFilter);
        return displayUtility;
    }


    @RequestMapping(method = RequestMethod.POST, params = { "delete" })
    public String delete(Long[] ids, RedirectAttributes redAttrib) {
        removeItems(ids, redAttrib, getEntityType());
        return getRedirectPageRoot();
    }
}
