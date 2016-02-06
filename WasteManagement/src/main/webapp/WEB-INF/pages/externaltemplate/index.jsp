<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@include file="/WEB-INF/pages/util/common-properties.jsp" %>


<html lang="en">
<head>
	<%@ include file="/WEB-INF/pages/util/header-styles.jsp" %>
	<link href="${pageContext.request.contextPath}/static/custom/css/external.css" rel="stylesheet">		
	<%@ include file="/WEB-INF/pages/util/template-scripts.jsp" %>
	<title>${productName} -<tiles:getAsString name="title" /></title>
</head>

<body>

	<div class="container">
		<tiles:insertAttribute name="menu" />
		<tiles:insertAttribute name="messages" />
		<tiles:insertAttribute name="body" />
	</div>
</body>
</html>



