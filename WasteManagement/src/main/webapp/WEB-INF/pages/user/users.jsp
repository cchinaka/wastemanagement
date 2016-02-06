<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>

<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<security:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_ENCODER')"
	var="canCrud" />

<html>
<form id="userForm" method="post">
	<div class="row">
		<div class="col-xs-12">
			<div class="table-responsive">
				<table id="userTable"
					class="table table-hover table-condensed small">
					<thead>
						<tr>
							<th class="width1">S/N</th>
							<th class="width1"><input type="checkbox"
								class="toggle-checker"></th>
							<th class="width30">Name</th>
							<th class="width10">Email</th>
							<th class="width10">Date Created</th>
							<th class="width1">Active</th>
							<th class="width1">Verified</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div class="well well-sm ${canCrud?'':'hidden'}">
		<button type="button" id="add" class="btn btn-primary">
			<i class="fa fa-plus-circle"></i> Add
		</button>
		<button type="submit" id="delete" class="btn btn-danger btn-sm hidden">
			<i class="fa fa-times"></i> Delete
		</button>
	</div>
</form>
<script type="text/javascript">
	$(function() {
		$.pageutil.createAjaxDisplayTable({
			tableId : "#userTable",
			url : "${pageContext.request.contextPath}/admin/usersetup/list",
			settings : {
				searching : true
			},
			resolveData : function(json) {
				for (var i = 0, ien = json.data.length; i < ien; i++) {
					if (json.data[i][1] === null || json.data[i][2] === '' || json.data[i][2] === 'null') {
						json.data[i][1] = 'N/A';
					}
					json.data[i][2] = "<a href='${pageContext.request.contextPath}/admin/usersetup/" + $(json.data[i][1]).val() + "'>" + json.data[i][2] + "</a>";
				}
				return json.data;
			}
		});

		$("#add").click(function() {
			window.location.href = "usersetup/0";
		});

	});
</script>