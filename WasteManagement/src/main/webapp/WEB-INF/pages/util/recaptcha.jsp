<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style type="text/css">
div.realperson-challenge>div.realperson-text {
	font-size: 1.5em;
	line-height: 0.25;
}

div.realperson-regen {
	text-align: center;
}

div.realperson-regen {
	visibility: hidden;
}
</style>
<spring:eval var="publicKey" expression="@configurationManager.getProperty('recaptcha.publickey', '6Lc0K-cSAAAAALY7bV_QOcH3pOEISZ4x0wBwf0VA')" />
<spring:eval var="captchaEnabled" expression="@configurationManager.getProperty('recaptcha.enable', 'true')" />
<c:if test="${captchaEnabled}">
	<script type="text/javascript">
		$.pageutil.createRecaptcha({
            element : "recaptcha_widget",
            publicKey : "${publicKey}"
        });
	</script>
	<div id="recaptcha_widget" style="display: none">
		<div class="control-group">
			<div class="controls">
				<div id="recaptcha_image" class="recaptcha_image_div"></div>
				<div class="recaptcha_control_div">
					<div class="controls">
						<div class="input-group ">
							<input type="text" id="recaptcha_response_field" name="recaptcha_response_field" class="form-control" placeholder="Enter the words above" /> <span
								class="input-group-addon">
								<button class="btn refresh" type="button" onclick="$.pageutil.reloadCaptcha()">
									<span class="load-refresh"><i class="fa fa-refresh"></i></span>
								</button>
							</span> <span class="input-group-addon recaptcha_only_if_image">
								<button class="btn headphone" type="button" onclick="Recaptcha.switch_type('audio')">
									<i title="Get an audio CAPTCHA" class="fa fa-headphones"></i>
								</button>
							</span> <span class="input-group-addon recaptcha_only_if_audio">
								<button class="btn picture" type="button" onclick="Recaptcha.switch_type('image')">
									<i title="Get an image CAPTCHA" class="fa fa-picture-o"></i>
								</button>
							</span> <span class="input-group-addon">
								<button class="btn question" onclick="Recaptcha.showhelp()">
									<i class="fa fa-question"></i>
								</button>
							</span>
						</div>
						<div class="recaptcha_error hidden text-danger loaderinfo"></div>
						<div class="recaptcha_error hidden text-danger"></div>
						<label class="recaptcha_only_if_image control-label hidden">Enter the words above:</label> <label
							class="recaptcha_only_if_audio control-label hidden">Enter the numbers you hear:</label>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="recaptcha_control_div">
		<input type="text" id="defaultReal" name="defaultReal" class="form-control" placeholder="Enter the words above" /> <span class="input-group-addon">
			<button class="btn btn-tiny refresh" type="button" onclick="$.pageutil.reloadCaptcha()">
				<span class="load-refresh"><i class="fa fa-refresh"></i></span>
			</button>
		</span>
	</div>
</c:if>