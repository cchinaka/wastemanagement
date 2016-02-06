<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static/" />
<spring:eval var="supportPhone"
	expression="@configurationManager.getProperty('application.suppourt.phone')"
	htmlEscape="true" />



<!-- Navigation -->
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header page-scroll">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#page-top">Payment Notification Receiver</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<li class="hidden"><a href="#page-top"></a></li>
				<li class="page-scroll"><a href="#portfolio">Portfolio</a></li>
				<li class="page-scroll"><a href="#about">About</a></li>
				<li><a href="${pageContext.request.contextPath}/login">Login</a></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>