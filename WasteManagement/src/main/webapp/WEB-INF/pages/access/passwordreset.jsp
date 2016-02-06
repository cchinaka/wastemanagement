<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval var="productName" expression="@configurationManager.getProperty('spring.product.name')" />
<link href="${pageContext.request.contextPath}/static/dist/css/signin.min.css" rel="stylesheet">
<c:if test="${not empty message}">
	<div class="alert alert-danger">${message}</div>
</c:if>
<div class="row">
	<div class="col-md-offset-4 col-md-4 col-xs-12 text-center">
		<div class="panel panel-default loginpanel">
			<div class="panel-body">
				<form class="form-signin" action="/passwordreset" method="post" data-role="form">
					<h3 class="form-signin-heading ">
						<a href="${pageContext.request.contextPath}/">
						Waste Management
						</a>
					</h3>
					<div class="form-group">
						<h5>Changing your password is simple</h5>
						<p>Please enter your email address to get instructions.</p>
						<input type="text" class="form-control" placeholder="Enter you registered email" required autofocus name="email" autocomplete="off" />
					</div>
					<div class="row">
						<div class="col-xs-12 text-center"><button class="btn btn-lg btn-success" type="submit"><i class="fa fa-refresh"></i> Reset</button>
						<a href="${pageContext.request.contextPath}/login" class="btn btn-lg btn-default"><i class="fa fa-arrow-left"></i> Go Back</a></div>
					</div>
										
				</form>
			</div>
		</div>
	</div>
</div>
<!-- /container -->
