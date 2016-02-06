<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ include file="/WEB-INF/pages/util/common-properties.jsp"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />

<link href="${root}/dist/css/signin.min.css" rel="stylesheet">

<c:if test="${isDemoMode eq 'true'}">
	<style>
body {
	background-image: url('/static/custom/images/demo-landing.png');
}
</style>
</c:if>

<c:set var="currentCompany" value="${sessionScope['current_company']}" />

<script type="text/javascript">
	$(function() {
		if ($(".login-wrapper").length > 1) {
			log("double tap: ", $(".login-wrapper"));
		}
		$(".login-wrapper").removeClass('hidden');
	});
</script>

<div class="row login-wrapper">
	<div class="col-md-4 col-md-offset-4 col-xs-12">
		<div class="panel panel-default loginpanel">
			<div class="panel-body">
				<form class="form-signin" action="/login" method="post"
					data-role="form">
					<h3 class="form-signin-heading text-center">
						<a href="${pageContext.request.contextPath}/">Waste Management</a>
					</h3>
					<div class="form-group">
						<label for="username">Username</label> <input type="text"
							class="form-control" placeholder="User Name" required autofocus
							name="username" autocomplete="off" />
					</div>
					<div class="form-group">
						<label for="password">Password</label> <input type="password"
							name="password" id="password" class="form-control"
							placeholder="Password" required autocomplete="off" />
					</div>
					<button class="btn btn-lg btn-success btn-block" type="submit">Sign
						in</button>
					<div class="form-group">
						<label><a
							href="${pageContext.request.contextPath}/passwordreset">Can't
								access your account?</a></label> <label>Don't have an account? <a
							href="${pageContext.request.contextPath}/register">Sign Up
								Now</a></label>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
