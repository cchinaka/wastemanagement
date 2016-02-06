package com.milcomsolutions.web.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.milcomsolutions.commons.security.AppPasswordEncoder;
import com.milcomsolutions.web.handler.LoginAccessDeniedHandler;
import com.milcomsolutions.web.handler.LoginSuccessHandler;
import com.milcomsolutions.web.security.AppUserDetailService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("appUserDetailService")
    private AppUserDetailService appUserDetailService;

    @Autowired
    private AppPasswordEncoder appPasswordEncoder;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginAccessDeniedHandler loginAccessDeniedHandler;

    @Autowired
    private org.springframework.security.web.authentication.logout.LogoutSuccessHandler logoutSuccessHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("userName");
        provider.setSaltSource(saltSource);
        provider.setUserDetailsService(appUserDetailService);
        provider.setPasswordEncoder(appPasswordEncoder);
        auth.authenticationProvider(provider);
    }


    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        return auth;
    }


    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("password", appUserDetailService);
        rememberMeServices.setCookieName("paygeniermservice");
        rememberMeServices.setParameter("rememberMe");
        return rememberMeServices;
    }


    /**
     * Check if user is login by remember me cookie, refer
     * org.springframework.security.authentication.AuthenticationTrustResolverImpl
     */
    @SuppressWarnings("unused")
    private boolean isRememberMeAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }


    /**
     * save targetURL in session
     */
    @SuppressWarnings("unused")
    private void setRememberMeTargetUrlToSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("targetUrl", "/admin/update");
        }
    }


    /**
     * get targetURL from session
     */
    @SuppressWarnings("unused")
    private String getRememberMeTargetUrlFromSession(HttpServletRequest request) {
        String targetUrl = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            targetUrl = session.getAttribute("targetUrl") == null ? "" : session.getAttribute("targetUrl").toString();
        }
        return targetUrl;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().disable();
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests().antMatchers("/app/**").permitAll();
        http.authorizeRequests().antMatchers("/resources/**").permitAll();
        http.authorizeRequests().antMatchers("/static/**").permitAll();
        http.authorizeRequests().antMatchers("/install").permitAll();
        http.authorizeRequests().antMatchers("/denied").permitAll();
        http.authorizeRequests().antMatchers("/access-denied").permitAll();
        http.authorizeRequests().antMatchers("/passwordreset").permitAll();
        http.authorizeRequests().antMatchers("/register/**").permitAll();
        http.authorizeRequests().antMatchers("/guest/**").permitAll();
        http.authorizeRequests().antMatchers("/home").permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
        // http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated().and().formLogin().successHandler(loginSuccessHandler).failureHandler(loginSuccessHandler)
                .loginPage("/login").permitAll().and().logout().logoutSuccessHandler(logoutSuccessHandler).logoutSuccessUrl("/").permitAll();
        http.rememberMe().rememberMeServices(rememberMeServices()).key("password");
        http.exceptionHandling().accessDeniedHandler(loginAccessDeniedHandler).accessDeniedPage("/access-denied");
    }
}
