<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval var="productName" expression="@configurationManager.getProperty('spring.product.name')" />

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Remita Integration Payment Notificaiton Receiver">
<title>${productName}</title>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />
<link rel="icon" type="image/png" href="${root}/custom/images/p.png">

<link href="${root}/custom/css/app-landing.css" rel="stylesheet">
<link href="${root}/custom/css/pgcommons.css" rel="stylesheet">