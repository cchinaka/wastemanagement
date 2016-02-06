<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval var="supportMail" expression="@configurationManager.getProperty('application.suppourt.mail')" />
<spring:eval var="productName" expression="@configurationManager.getProperty('spring.product.name')" />
<spring:eval var="siteUrl" expression="@configurationManager.getProperty('spring.product.web.url')" />
<spring:eval var="dateFormat" expression="@configurationManager.getProperty('jsp.date.format','dd/mm/yyyy')" htmlEscape="true" />
<spring:eval var="isDemoMode" expression="@configurationManager.getProperty('mode.demo','false')" htmlEscape="true" />



