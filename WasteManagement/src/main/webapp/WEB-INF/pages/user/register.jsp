<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<spring:eval var="captchaEnabled" expression="@configurationManager.getProperty('recaptcha.enable', 'true')" />
<c:set var="hasCapcha" value="" />
<c:if test="${captchaEnabled}">
	<c:set var="hasCapcha" value="checked" />
</c:if>
<input type="checkbox" class="hidden" name='hasCaptcha' id="hasCaptcha" ${hasCapcha} />
<sf:form modelAttribute="uservo" role="form" method="post" id="meterForm" action="${pageContext.request.contextPath}/register">
	<sf:hidden path="user.id" id="id" />
	<div class="row">
		<div class="col-xs-12 col-sm-8 small">
			<span class="text-warning"><small><i class="fa fa-exclamation-triangle"></i> Note: All fields are required</small></span>
		</div>
		<div class="col-xs-12 col-sm-8">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">First Name</label>
						<sf:input path="user.userDetail.firstName" class="form-control required" placeholder="Enter first name" />
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">Surname</label>
						<sf:input path="user.userDetail.lastName" class="form-control required" placeholder="Enter Surname" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">Phone Number</label>
				<sf:input path="user.userDetail.phoneNumber" class="form-control required" placeholder="Enter Phone Number" />
			</div>
			<div class="form-group">
				<label class="control-label">Username</label>
				<sf:input path="user.userDetail.email" id="userEmail" class="form-control" placeholder="Enter Email" />
			</div>
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">Password</label>
						<sf:password path="user.password" id="password" class="form-control required" placeholder="Enter Password" />
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">Confirm Password</label> <input type="password" name="confirmPassword" class="form-control"
							placeholder="Confirm Password" />
					</div>
				</div>
			</div>
			<div class="form-group meter-info">
				<label class="control-label">Meter</label>
				<div class="input-group">
					<span class="input-group-addon"><input type="checkbox" name="haveMeter" id="haveMeter" /></span>
					<sf:input path="meters[0].meterId" class="form-control meterId" placeholder="Enter Meter Number If Available" readonly="true" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label"> <input type="checkbox" name="termsAndConditions" id="termsAndConditions" /> <span>By clicking here
						you have accepted our <strong><a href="/about/terms" target="_blank">Terms and Conditions <i class="fa fa-external-link"></i></a></strong>
				</span>
				</label>
			</div>
			<div class="form-group" id="captcha-row">
				<jsp:include page="/WEB-INF/pages/util/captcha.jsp"></jsp:include>
			</div>
			<div class="form-group">
				<button type="button" id="registerButton" class="btn btn-primary"><i class='fa fa-user'></i> Register</button>
				<button type="button" id="cancel" class="btn btn-danger"><i class='fa fa-ban'></i> Cancel</button>
			</div>
		</div>
	</div>
</sf:form>
<script type="text/javascript">
	$(function() {
	    $("[name='haveMeter']").click(function(e) {
		    if ($(this).is(":checked")) {
			    $(".meterId").prop("readonly", false);
		    } else {
			    $(".meterId").prop("readonly", true);
		    }
	    });

	    $("button#cancel").click(function() {
		    window.history.back();
	    })
	    $("#meterForm").validate({
	        rules : {
	            "termsAndConditions" : {
		            required : true
	            },
	            "user.userDetail.email" : {
	                required : true,
	                email : true,
	                remote : {
	                    url : "${pageContext.request.contextPath}/register/email/validity",
	                    method : "POST"
	                }
	            },
	            "confirmPassword" : {
		            equalTo : "#password"
	            },
	            "meters[0].meterId" : {
	                required : "#haveMeter:checked",
	                remote : {
	                    url : "${pageContext.request.contextPath}/register/meter/validity",
	                    method : "POST"
	                }
	            }
	        },
	        messages : {
	            "termsAndConditions" : "",
	            "user.password" : "Please enter a password",
	            "user.userDetail.firstName" : "Please enter a first name",
	            "user.userDetail.lastName" : "Please enter a surname",
	            "user.userDetail.phoneNumber" : "Please enter a phone number",
	            "user.userDetail.email" : {
	                required : "Please enter an email address",
	                remote : "Email already exists on the system"
	            },
	            "meters[0].meterId" : {
	                required : "Please enter a Meter ID",
	                remote : "This meter is not available"
	            }
	        }
	    });

	    $("#registerButton").click(function() {
		    if ($("#meterForm").valid()) {
			    if ($("#hasCaptcha").prop("checked")) {
				    $.pageutil.verifyDotCaptcha({
					    caller : "#registerButton"
				    }).done(function(result) {
				    	log(result);
					    if (result.valid) {
						   $("#meterForm")[0].submit();
					    } else {
						    $.pageutil.showDotCaptchaError(result.message);
					    }
				    });
			    } else {
				    log("firstly...");
				    $("#meterForm")[0].submit();
			    }

		    }
	    });
    });
</script>