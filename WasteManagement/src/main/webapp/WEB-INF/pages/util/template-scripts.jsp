<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval var="version" expression="@configurationManager.getProperty('resource.version','1.0.0')" htmlEscape="true" />
<script src="${root}/custom/js/app-scripts.js?v=${version}"></script>
<script src="${root}/custom/js/pageutil.js?v=${version}"></script>