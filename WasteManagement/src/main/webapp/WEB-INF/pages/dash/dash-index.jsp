<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />

<jsp:include page="/WEB-INF/pages/dash/system-numbers.jsp"></jsp:include>
<div class="row">
	<div class="col-sm-8">
		<jsp:include page="/WEB-INF/pages/dash/pending-transactions.jsp"></jsp:include>
	</div>
	<div class="col-sm-4">
		<jsp:include page="/WEB-INF/pages/dash/notification.jsp"></jsp:include>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		var dashController = {
			init : function() {
				console.log("initializing application...");
				$.ajax({
					url : "dash/system-numbers",
					success : function(info) {
						$(".pending-trxns").countTo({
							from : 0,
							to : info.pending
						});
						$(".approved-trxns").countTo({
							from : 0,
							to : info.approved
						});
						$(".error-trxns").countTo({
							from : 0,
							to : info.errors
						});
						$(".services-configured").countTo({
							from : 0,
							to : info.services
						});
					}
				});
				$.ajax({
					url : "dash/pending-transactions",
					success : function(trxns) {
						console.log("trxn: ", trxns);
					}
				});
				$.ajax({
					url : "dash/notifications",
					success : function(notes) {
						console.log("notes: ", notes);
					}
				});
			}
		};

		dashController.init();
	});
</script>