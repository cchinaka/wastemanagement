<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp"%>

<html lang="en">
<head profile="http://www.w3.org/2005/10/profile">

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="author" content="Chukwukadibia Chinaka">
<meta name="description"
	content="Waste Management System">
<title>Waste Management System</title>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />
<spring:eval var="version"
	expression="@configurationManager.getProperty('resource.version','1.0.0')"
	htmlEscape="true" />



<link rel="shortcut icon" href="${root}/dist/images/icons/favicon.ico">
<link rel="apple-touch-icon"
	href="${root}/dist/images/icons/favicon.png">
<link rel="apple-touch-icon" sizes="72x72"
	href="${root}/dist/images/icons/favicon-72x72.png">
<link rel="apple-touch-icon" sizes="114x114"
	href="${root}/dist/images/icons/favicon-114x114.png">
<link href="${root}/dist/css/wastemanagement.min.css?v=${version}"
	rel="stylesheet">
<script src="${root}/dist/js/wastemanagement.js?v=${version}"></script>
</head>

<body>
	<div>
		<!--BEGIN BACK TO TOP-->
		<a id="totop" href="#"><i class="fa fa-angle-up"></i></a>
		<!--END BACK TO TOP-->
		<tiles:insertAttribute name="head" />
		<div id="wrapper">
			<tiles:insertAttribute name="menu" />
			<!--BEGIN PAGE WRAPPER-->
			<div id="page-wrapper">
				<!--BEGIN TITLE & BREADCRUMB PAGE-->
				<div id="title-breadcrumb-option-demo" class="page-title-breadcrumb">
					<div class="page-header pull-left">
						<div class="page-title">
							<i class='<tiles:getAsString name="title-class"/>'></i>
							<tiles:getAsString name="title" />
						</div>
					</div>
					<ol class="breadcrumb page-breadcrumb pull-right">
						<li><i class="fa fa-home"></i>&nbsp;<a
							href="${pageContext.request.contextPath}/dash">Home</a>&nbsp;&nbsp;<i
							class="fa fa-angle-right"></i>&nbsp;&nbsp;</li>
						<li class="hidden"><a href="#"><tiles:getAsString
									name="title" /></a>&nbsp;&nbsp;<i class="fa fa-angle-right"></i>&nbsp;&nbsp;</li>
						<li class="active"><tiles:getAsString name="title" /></li>
					</ol>
					<div class="clearfix"></div>
				</div>
				<!--END TITLE & BREADCRUMB PAGE-->
				<div class="page-content">
					<div id="tab-general">
						<tiles:insertAttribute name="body" />
					</div>
				</div>
				<tiles:insertAttribute name="footer" />
			</div>
			<!--END PAGE WRAPPER-->
		</div>
	</div>
</body>
</html>



