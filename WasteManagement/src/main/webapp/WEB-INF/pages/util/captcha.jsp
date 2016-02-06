<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style type="text/css">

</style>
<spring:eval var="captchaEnabled" expression="@configurationManager.getProperty('recaptcha.enable', 'true')" />
<c:if test="${captchaEnabled}">
	<div class="recaptcha_control_div">
		<input type="hidden" name="captchaInput" class="captcha-input-hidden" />
		<div class="input-group">
			<input type="text" name="cap-input" class="captcha-input form-control" placeholder="Please enter the characters above" /> <span
				class="input-group-btn">
				<button class="btn btn-small refresh dot-captcha" type="button">
					<span class="load-refresh" title="refresh challenge"><i class="fa fa-refresh"></i></span>
				</button>
			</span>
		</div>
		<p class="help-block"><span class='dot-captcha-error-message text-danger recaptcha_error'></span></p>
	</div>
	<script type="text/javascript">
		$(function() {
	        $.pageutil.createDotCaptcha({
	            length : 5,
	            control : ".captcha-input-hidden",
	            textCollector: ".captcha-input",
	            refreshButton: ".dot-captcha",
	            url: "${pageContext.request.contextPath}/guest/dotcaptcha/validate"
	        });
        });
	</script>
</c:if>