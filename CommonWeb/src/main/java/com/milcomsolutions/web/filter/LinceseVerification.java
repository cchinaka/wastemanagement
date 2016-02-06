package com.milcomsolutions.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.service.LicenseService;


// @Component
public class LinceseVerification implements Filter {

    private static Logger LOG = Logger.getLogger(LinceseVerification.class);

    @Autowired
    private LicenseService licenseService;


    public LinceseVerification() {
        // TODO Auto-generated constructor stub
    }


    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }


    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (applyFilterToRequest(httpRequest)) {
            Boolean isValid = Boolean.valueOf(String.valueOf(httpRequest.getSession(true).getAttribute(AppConstants.LINCENSE_IS_VALID)));
            String redirectPage = "/licensing";
            if (isValid) {
                chain.doFilter(request, response);
            } else {
                LinceseVerification.LOG.info("Lincense is not valid.contact support");
                if (licenseService.isInstalled()) {
                    Boolean lValid = licenseService.verifyLicense();
                    httpRequest.getSession(true).setAttribute(AppConstants.LINCENSE_IS_VALID, lValid);
                    if (lValid) {
                        chain.doFilter(request, response);
                    }
                } else {
                    redirectPage = "/install";
                }
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect(redirectPage);
            }
        } else {
            chain.doFilter(request, response);
        }
    }


    private boolean applyFilterToRequest(HttpServletRequest httpRequest) {
        String[] urlsToIgnore = new String[] { "/install", "/licensing" };
        boolean ignore = ArrayUtils.contains(urlsToIgnore, httpRequest.getRequestURI());
        if (!ignore) {
            ignore = httpRequest.getRequestURI().startsWith("/WEB-INF");
        }
        return !ignore;
    }


    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
}
