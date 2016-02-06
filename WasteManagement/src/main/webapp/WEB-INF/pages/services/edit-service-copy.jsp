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
							class="btn btn-warning btn-tiny btn-add-income-data-config">
							<i class="fa fa-plus-circle"></i> Add Income Data Config
						</button>
					</span>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<span class='no-income-data'><i
								class='fa fa-exclamation-circle'></i> <i>You have no
									Incoming Data Config defined at the moment</i></span>
							<div class="config-income-data"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-sm-12 col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<i class="fa fa-signal"></i> Actions Config <span
						class="pull-right">
						<button type="button" class="btn btn-tiny" data-toggle="modal"
							data-target=".action-modal-sm">
							<i class="fa fa-plus-circle"></i> Add Action
						</button>
					</span>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<span class='no-action'><i
								class='fa fa-exclamation-circle'></i> <i>You have no actions
									defined at the moment</i></span>

							<div class="config-actions"></div>
						</div>
					</div>
				</div>
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


<div class="modal fade action-modal-sm" tabindex="-1" id="actionModal"
	role="dialog">
	<div class="vertical-alignment-helper">
		<div class="modal-dialog modal-sm vertical-align-center">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>

					</button>
					<h4 class="modal-title" id="myModalLabel">Action Type</h4>

				</div>
				<div class="modal-body">
					<div class="form-group">
						<select id="actionType" name="actionType"
							class="form-control required">
							<option value="">Select Type of Action</option>
							<option value="DB">Database</option>
							<option value="EMAIL">Email Address</option>
						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-info btn-add-action">
						<i class="fa fa-plus-square"></i> Add Action
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="hidden" id="page-templates">
	<div class="row action-email-row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="name"><strong>Action Name</strong></label> <input
					type="text" class="form-control action-name"
					placeholder="Enter an action name" />
			</div>
			<div class="checkbox">
				<label><input type="checkbox" name="activeAction" class=""
					checked /> Active</label>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="form-group">
				<label>Email</label> <input type="email" class="form-control"
					placeholder="Enter Email">
			</div>
			<div class="well well-sm">
				<button type="button" class="btn btn-danger btn-remove-row"
					data-parent-class="action-email-row">
					<i class="fa fa-minus-circle"></i> Remove
				</button>
			</div>
		</div>
	</div>
	<div class="row action-row">
		<div class="col-xs-12">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label for="name"><strong>Action Name</strong></label> <input
							type="text" class="form-control action-name"
							placeholder="Enter an action name" />
					</div>
					<div class="form-group">
						<label for="name">Database Type</label> <input type="text"
							class="form-control database-type"
							placeholder="Enter a database type" />
					</div>
					<div class="form-group">
						<label for="name">Database URL</label> <input type="text"
							class="form-control database-url"
							placeholder="Enter a database Url" />
					</div>
					<div class="form-group">
						<label for="name">Username</label> <input type="text"
							class="form-control username" placeholder="Enter a user name" />
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label for="name">Password</label> <input type="password"
									class="form-control password" placeholder="Enter a password" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label for="name">Confirm Password</label> <input
									type="password" class="form-control password"
									placeholder="Please confirm the password" />
							</div>
						</div>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" name="activeAction" class=""
							checked /> Active</label>
					</div>
				</div>
				<div class="col-sm-6">

					<div class="form-group">
						<label for="name">Payment Instruction SQL</label>
						<textarea class="form-control" row="20"
							placeholder="Instruction SQL"></textarea>
					</div>
					<div class="well well-sm">
						<button type="button" class="btn">
							<i class="fa fa-life-buoy"></i> Test Connection
						</button>
						<button type="button" class="btn btn-danger btn-remove-row"
							data-parent-class="action-row">
							<i class="fa fa-minus-circle"></i> Remove
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-12">
			<hr />
		</div>
	</div>

	<div class="row income-data-row">
		<div class="col-xs-12">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label for="name"><strong>Data Config Name</strong></label> <input
							type="text" class="form-control action-name required"
							placeholder="Enter an action name" />
					</div>
					<div class="form-group">
						<label for="name">Database Type</label> <input type="text"
							class="form-control database-type required"
							placeholder="Enter a database type" />
					</div>
					<div class="form-group">
						<label for="name">Database URL</label> <input type="text"
							class="form-control database-url required"
							placeholder="Enter a database Url" />
					</div>
					<div class="form-group">
						<label for="name">Username</label> <input type="text"
							class="form-control username required"
							placeholder="Enter a user name" />
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label for="name">Password</label> <input type="password"
									class="form-control password" placeholder="Enter a password" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label for="name">Confirm Password</label> <input
									type="password" class="form-control password"
									placeholder="Please confirm the password" />
							</div>
						</div>
					</div>
					<div class="checkbox">
						<label><input type="checkbox" name="activeAction" class=""
							checked /> Active</label>
					</div>
				</div>
				<div class="col-sm-6">

					<div class="form-group">
						<label for="name">Payment Instruction SQL</label>
						<textarea class="form-control" row="20"
							placeholder="Instruction SQL"></textarea>
					</div>
					<div class="well well-sm">
						<button type="button" class="btn btn-warning">
							<i class="fa fa-life-buoy"></i> Test Connection
						</button>
						<button type="button" class="btn btn-danger btn-remove-row"
							data-parent-class="income-data-row">
							<i class="fa fa-minus-circle"></i> Remove
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-12">
			<hr />
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		var configController = {
			submitForm : function(event) {
				event.preventDefault();
				if ($("#serviceConfigForm").valid()) {
					$("#serviceConfigForm").submit();
				}
			},
			addAction : function(event) {
				event.preventDefault();
				$("#actionModal").modal("hide");
				$(".no-action").addClass("hidden");

				var actionType = $("#actionType").val();
				var actionRow = null;
				if (actionType === 'EMAIL') {
					actionRow = $("#page-templates > .action-email-row").clone(false);
				} else {
					//DB
					actionRow = $("#page-templates > .action-row").clone(false);
				}

				actionRow.find(".btn-remove-row").click(function() {
					var parentClass = $(this).data("parent-class");
					$(this).closest("." + parentClass).remove();
					var actionRowsLength = $(".config-actions .action-row").length;
					if (actionRowsLength === 0) {
						$(".no-action").removeClass("hidden");
					}
				});
				
				configController.addActionBehaviour(actionType, actionRow);
				actionRow.appendTo(".config-actions");

			},
			addActionBehaviour: function(actionType, actionRow){
				switch(actionType){
				case "EMAIL":
						break;
				case "DB":
				default:
						//actionRow.find("[type='text']")
						break;
				}
			},
			addDataConfig : function(event) {
				event.preventDefault();
				var actionRow = $("#page-templates > .income-data-row").clone(false);
				$(".no-income-data").addClass("hidden");
				var dataRow = $("#page-templates > .income-data-row").clone(false);
				dataRow.find(".btn-remove-row").click(function() {
					var parentClass = $(this).data("parent-class");
					$(this).closest("." + parentClass).remove();
					var incomeRowsLength = $(".config-income-data .income-data-row").length;
					if (incomeRowsLength === 0) {
						$(".no-income-data").removeClass("hidden");
					}
				});
				dataRow.appendTo(".config-income-data");
			},
			initValidation : function() {
				$("#serviceConfigForm").validate();
			},
			init : function() {
				this.initValidation();
				$(".switch").bootstrapSwitch({
					onText : "Yes",
					offText : "No"
				});
				$("#saveButton").click(this.submitForm);
				$(".btn-add-action").click(this.addAction);
				$(".btn-add-income-data-config").click(this.addDataConfig);
			}
		};

		configController.init();
	});
</script>