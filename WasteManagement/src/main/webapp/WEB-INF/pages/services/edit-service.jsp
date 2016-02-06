<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />

<sf:form novalidate='novalidate' modelAttribute="serviceConfig"
	role="form" method="post" id="serviceConfigForm">
	<sf:hidden path="config.id" id="id" />
	<div class="row">
		<div class="col-sm-12 col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<i class="fa fa-edit"></i> Service Info
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label for="name">Name</label>
								<sf:input path="config.name" cssClass="form-control" id="name"
									placeholder="Name" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label for="name">Service Type ID</label> <input
									name="config.serviceTypeId" class="form-control" id="name"
									placeholder="Type to select a Service Type ID..." />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label>Active?</label><br />
								<sf:checkbox path="config.active" cssClass="switch" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-sm-12 col-xs-12">
			<div class="panel panel-warning">
				<div class="panel-heading">
					<i class="fa fa-database"></i> Incoming Data Config <span
						class="pull-right">
						<button type="button"
							class="btn btn-warning btn-tiny btn-add-income-data-config"
							data-configtype="DB">
							<i class="fa fa-plus-circle"></i> Add Incoming Data Config
						</button>
					</span>
				</div>
				<table class="table table-hover table-data-config">
					<thead>
						<tr class="hidden">
							<th class="width1">S/N</th>
							<th class="width5">Active</th>
							<th class="width25">Name</th>
							<th class="width15">Config Type</th>
							<th class="width10">Database Type</th>
							<th class="width30">DB Url</th>
							<th class="width10">Operations</th>
						</tr>
					</thead>
					<tbody class="config-income-data">
						<tr class='no-income-data'>
							<td colspan="7"><i class='fa fa-exclamation-circle'></i> <i>You
									have no Incoming Data Config defined at the moment</i></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-sm-12 col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<i class="fa fa-signal"></i> Actions Config <span
						class="pull-right">
						<button type="button" class="btn btn-tiny btn-email-action">
							<i class="fa fa-plus-circle"></i> Add Email Action
						</button>
						<button type="button" class="btn btn-tiny btn-db-action">
							<i class="fa fa-plus-circle"></i> Add Database Action
						</button>
					</span>
				</div>				
				<table class="table table-hover table-action">
					<thead>
						<tr class="hidden">
							<th class="width1">S/N</th>
							<th class="width5 text-center">Active</th>
							<th class="width50">Name</th>
							<th class="width30">Action Type</th>
							<th class="width10">Operations</th>
						</tr>
					</thead>
					<tbody class="config-action">
						<tr class='no-action'>
							<td colspan="5"><i class='fa fa-exclamation-circle'></i> <i>You have no actions defined at the moment</i></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="well well-sm">
		<button type="button" id="saveButton" class="btn btn-primary">
			<i class="glyphicon glyphicon-floppy-save"></i> Save
		</button>
		<a href='${pageContext.request.contextPath}/user/services' id="cancel"
			class="btn btn-danger"> <i class="fa fa-ban"></i> Cancel
		</a>
	</div>
</sf:form>


<div id="config-local-db"></div>
<jsp:include page="/WEB-INF/pages/services/page-templates.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/modules/services.js"></script>
