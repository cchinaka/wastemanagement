<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<c:choose>
	<c:when test="${verifed}">
		<div class="alert alert-success">
			<h1>Email Verification Success</h1>
			Email Verifed.you may processed to <a href="${pageContext.request.contextPath}/login">Login</a>
		</div>
	</c:when>
	<c:otherwise>
		<div class="alert alert-danger">
			<h1>Email Verification Error</h1>
			A Problem was encountered while processing your request.Please contact support on ${supportNumber} or click 
			<a href="${pageContext.request.contextPath}/login/verify/email">here</a> to request a verification mail.
		</div>
	</c:otherwise>
</c:choose>

