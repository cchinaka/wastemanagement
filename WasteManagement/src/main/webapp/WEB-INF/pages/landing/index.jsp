<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<spring:eval var="version" expression="@configurationManager.getProperty('resource.version','1.0.0')" htmlEscape="true" />
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="author" content="">
<meta name="description"
	content="Remita Integration Payment Notificaiton Receiver">

<title>Waste Management System</title>
<link href="/static/dist/css/wastemanagement.landing.min.css?v=${version}"
	rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>
<body id="page-top" class="index">
	<tiles:insertAttribute name="menu" />
	<tiles:insertAttribute name="head" />
	<tiles:insertAttribute name="body" />
	<tiles:insertAttribute name="footer" />
	<script src="/static/dist/js/wastemanagement.landing.min.js?v=${version}"></script>
</body>
</html>
