<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html lang="en">
<head>
<c:set var="template" value="/WEB-INF/pages/landing" />
<jsp:include page="${template}/header-styles.jsp"></jsp:include>
<jsp:include page="${template}/template-scripts.jsp"></jsp:include>
<title><tiles:getAsString name="pagetitle" /></title>
</head>
<body>
	<tiles:insertAttribute name="menu" />
	<jsp:include page="/WEB-INF/pages/util/pagehead.jsp">
		<jsp:param name="title">
			<jsp:attribute name="value">
			<i class="fa fa-book"></i> <tiles:getAsString name="pagetitle" />
		</jsp:attribute>
		</jsp:param>
	</jsp:include>
	<div class="container landing-container">		
		<tiles:insertAttribute name="messages" />
		<tiles:insertAttribute name="body" />
	</div>
	<tiles:insertAttribute name="modal" />
	<tiles:insertAttribute name="footer" />
</body>
</html>
