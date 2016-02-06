<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty successMessage}">
	<div class="alert alert-success fade in">		
	<button data-aria-hidden="true" data-dismiss="alert" class="close" type="button"><span class="glyphicon glyphicon-remove"></span></button>${successMessage}
	</div>
</c:if>
<c:if test="${not empty errorMessage}">
	<div class="alert alert-danger fade in">
	<button data-aria-hidden="true" data-dismiss="alert" class="close" type="button"><span class="glyphicon glyphicon-remove"></span></button> ${errorMessage}
	</div>
</c:if>
<c:if test="${not empty warnMessage}">
	<div class="alert alert-warning fade in">
	<button data-aria-hidden="true" data-dismiss="alert" class="close" type="button"><span class="glyphicon glyphicon-remove"></span></button> 
	<span class="">${warnMessage}</span>
	</div>
</c:if>