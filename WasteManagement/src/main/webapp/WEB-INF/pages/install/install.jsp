<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />
<link href="${root}/bootstrap/css/signin.css" rel="stylesheet">
<div class="row">
	<div class="col-md-6 col-md-offset-3 col-xs-12">
		<div class="panel panel-default loginpanel">
			<div class="panel-body">
				<h1 class="text-center fa-4x space-down">
					Install <span class="fa fa-youtube-play"></span>
				</h1>
				
				<sf:form cssClass="form-horizontal" modelAttribute="licenseVo" role="form" id="installationForm" method="post" enctype="multipart/form-data">
					<div class="form-group">
						<label class="col-sm-3 control-label" for="email">Email</label>
						<div class="col-sm-9">
							<sf:input path="email" cssClass="form-control required email" id="email" placeholder="Enter an email address" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="mpFile">License</label>
						<div class="col-sm-9">
							<input type="file" name="mpFile" class="required" value="Add File" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<button type="submit" class="btn btn-lg btn-primary">
								Install <span class="fa fa-play"></span>
							</button>
						</div>
					</div>
				</sf:form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
	    initializeValidationDefaults();
	    $("#installationForm").validate({
		    messages : {
		        mpFile : "Please browse and select a license file",
		        email : {
			        required : "Please provide an email to send your login detail to"
		        }
		    }
	    });
    });
</script>