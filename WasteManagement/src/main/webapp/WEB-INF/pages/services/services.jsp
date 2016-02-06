<!DOCTYPE HTML >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<html>
<form id="servicesForm" method="post">
	<div class="row">
		<div class="col-xs-12">
			<table id="servicesTable"
				class="table table-hover table-condensed small">
				<thead>
					<tr>
						<th class="width1">S/N</th>
						<th class="width1"><input type="checkbox"
							class="toggle-checker"></th>
						<th class="">Name</th>
						<th class="">Description</th>
						<th class="">Creation Date</th>
						<th class="">Service Type ID</th>
						<th class="width1">Active</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div class="well well-sm">
		<button type="button" id="add" class="btn btn-primary">
			<i class="fa fa-plus-circle"></i> Add
		</button>
		<button type="button" id="delete" class="btn btn-danger btn-sm">
			<i class="fa fa-times"></i> Delete
		</button>
	</div>
</form>
<script type="text/javascript">
	$(function() {
		$.pageutil.createAjaxDisplayTable({
			tableId : "#servicesTable",
			url : "${pageContext.request.contextPath}/user/services/list",
			settings : {
				searching : true,
				createdRow : function(row, data, dataIndex) {
					//$(row).children("td:nth-child(2)").addClass("hidden");
				}
			},
			resolveData : function(json) {
				for (var i = 0, ien = json.data.length; i < ien; i++) {
					json.data[i][2] = "<a href='${pageContext.request.contextPath}/user/services/" + $(json.data[i][1]).val() + "'>" + json.data[i][2] + "</a>";
				}
				return json.data;
			}
		});

		$.pageutil.initializeCrudOperations({
			formSelector : "#servicesForm",
			addSelector : "#add",
			deleteSelector : "#delete",
			addUrl : "${pageContext.request.contextPath}/user/services/0",
			deleteUrl : "${pageContext.request.contextPath}/user/services?delete"
		});
	});
</script>