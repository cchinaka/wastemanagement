<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />
<html>
<head>
<link href="${root}/custom/css/poster.css" rel="stylesheet">
<link href="${root}/font-awesome-4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
</head>
<body>
	<c:set var="message" value="Loading. Please wait..." />
	<c:set var="postButtonValue" value="SEND" />
	<c:if test="${not empty postMessage}">
		<c:set var="message" value="${postMessage}" />
	</c:if>
	<c:if test="${not empty postButton}">
		<c:set var="postButtonValue" value="${postButton}" />
	</c:if>
	<div id="content">
		<h1>${message}
			<img src="${root}/custom/images/loading.gif" class="loader" />
		</h1>
		<%/**
		<c:if test="${not isCashOrScratchCard}">
			<span class="plan-b"> If this Page does not redirect In 10
				Seconds, click the <strong>button</strong> below.
			</span>
		</c:if>
		*/ %>
		<form action="${postAction}" id="formId" name="formId" method="POST">
			<c:forEach items="${postLoad}" var="entry">
				<input name="${entry.key}" value="${entry.value}" type="hidden" />
			</c:forEach>
		</form>
	</div>
</body>




<script type="text/javascript">
	(function() {
		document.getElementById("formId").submit();
	})();
</script>
</html>