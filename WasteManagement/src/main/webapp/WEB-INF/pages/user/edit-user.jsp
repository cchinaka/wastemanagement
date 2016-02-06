<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"></security:authorize>
<security:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_ENCODER')"
	var="canCrud" />
<html>
<sf:form modelAttribute="uservo" role="form" method="post"
	id="meterForm">
	<sf:hidden path="user.id" id="id" />
	<div class="row">
		<div class="col-xs-12 col-sm-8 ">
			<div class="alert label-default">
				<span class='fa fa-3x fa-exclamation-circle pull-right'></span> <b>User
					Setup Rules:</b>
				<ol class='nopad'>
					<li>You cannot have a ROLE_ADMIN and ROLE_COMPANYADMIN Roles
						assigned to a user. An admin will always be selected in preference</li>
					<li>All Users must have a ROLE_USER role</li>
					<li>All Users with ROLE_COMPANYADMIN, ROLE_COLLECTINGAGENT, ROLE_TICKETSUPPORT, ROLE_TICKETSUPPORT_NOTIFICATION and
						ROLE_COLLECTING_REPORT must be assigned to a company
					<li>Any user given the ROLE_ADMIN role will be unassigned from
						his current company, if he's assigned to one</li>
				</ol>
				These Rules have been applied to relevant fields.
			</div>
		</div>
		<div class="col-xs-12 col-md-8 col-sm-10">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">First Name</label>
						<sf:input path="user.userDetail.firstName"
							class="form-control required" placeholder="Enter first name" />
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group">
						<label class="control-label">Surname</label>
						<sf:input path="user.userDetail.lastName"
							class="form-control required" placeholder="Enter Surname" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">Phone Number</label>
				<sf:input path="user.userDetail.phoneNumber"
					class="form-control required" placeholder="Enter Phone Number" />
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">Username</label>
						<sf:input path="user.userDetail.email" id="userEmail"
							class="form-control" placeholder="Enter Email" />
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label">Password</label>
						<sf:hidden path="user.currentPassword"
							value="${uservo.user.currentPassword}" />
						<input type="password" name="user.password" id="password"
							class="form-control" placeholder="Enter Password"
							autocomplete="off" />
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label">Confirm Password</label> <input
							type="password" name="confirmPassword" class="form-control"
							placeholder="Confirm Password" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">Assigned Company</label>
				<sf:select path="user.companyId" id="company"
					cssClass="form-control">
					<sf:option value="">Select a Company</sf:option>
					<sf:options itemValue="id" itemLabel="name" items="${companies}" />
				</sf:select>
			</div>
			<div class="form-group">
				<label class="control-label"> Use Roles </label>
				<sf:select path="user.roleCodes" id="roleCodes" multiple="true"
					items="${roles}" itemValue="code" itemLabel="code" size="5" />
			</div>
			<div class="row">
				<div class="col-md-2 col-sm-6">
					<div class="form-group">
						<label class="control-label">Verified</label>
						<div>
							<sf:checkbox path="user.verified" id="verified" />
						</div>
					</div>
				</div>
				<div class="col-md-6 col-sm-6 hidden send-verification-email">
					<div class="form-group">
						<label class="control-label">Send Verification Email</label>
						<div>
							<sf:checkbox path="sendVerificationEmail"
								id="sendVerificationEmail" checked="true" />
						</div>
					</div>
				</div>
			</div>
			<c:if test="${isAdmin}">
				<div class="form-group">
					<label class="control-label">Can See Bill Dates</label>
					<div>
						<sf:checkbox path="user.canSeeBillDates" id="canSeeBillDates" />
					</div>
				</div>
			</c:if>
			<div class="form-group meter-info">
				<label class="control-label">Meter</label>
				<div class="input-group">
					<span class="input-group-addon"><input type="checkbox"
						name="haveMeter" id="haveMeter" /></span>
					<sf:input path="meters[0].meterId" class="form-control meterId"
						placeholder="Enter Meter Number If Available" readonly="true" />
				</div>
			</div>

			<c:if test="${not empty assignedMeters}">
				<div class="form-group">
					<label class="control-label">Assigned Meters</label>
					<p class="form-control-static">
						<c:forEach items="${assignedMeters}" var="ameter"
							varStatus="status">
					 		#${ameter.meterId} ${status.last?'':', '}
					 	</c:forEach>
					</p>
				</div>
			</c:if>


			<div class="form-group hidden">
				<label class="control-label"> <input type="checkbox"
					name="termsAndConditions" id="termsAndConditions" checked /> <span>By
						clicking here you have accepted our <strong><a
							href="/about/terms" target="_blank">Terms and Conditions <i
								class="fa fa-external-link"></i></a></strong>
				</span>
				</label>
			</div>
		</div>
	</div>
	<div class="well well-sm">
		<button type="button" id="saveButton" class="btn btn-primary ${canCrud?'':'hidden'}">
			<i class="glyphicon glyphicon-floppy-save"></i> Save User
		</button>
		<button type="button" id="cancel" class="btn btn-sm btn-danger">
			<i class="fa fa-ban"></i> Cancel
		</button>
	</div>
</sf:form>
<script type="text/javascript">
	$(function() {

		if("${canCrud}" !== 'true'){
			$.pageutil.disableControls();
		}
		var validator = $("#meterForm").validate({
			rules : {
				"user.userDetail.email" : getEmailValidationRule(),
				"confirmPassword" : {
					equalTo : "#password"
				},
				"user.companyId" : {
					required : function() {
						var isRequired = $("select#roleCodes").children("option:selected").filter(function() {
							return this.value == 'ROLE_COMPANYADMIN' || this.value == 'ROLE_COLLECTINGAGENT' || this.value == 'ROLE_COLLECTING_REPORT';
						}).length > 0;

						log("isRequired: ", isRequired);
						return isRequired;
					}
				},
				"meters[0].meterId" : {
					required : "#haveMeter:checked",
					remote : {
						url : "${pageContext.request.contextPath}/register/meter/validity",
						method : "POST"
					}
				}
			},
			messages : {
				"termsAndConditions" : "",
				"user.password" : "Please enter a password",
				"user.userDetail.firstName" : "Please enter a first name",
				"user.userDetail.lastName" : "Please enter a surname",
				"user.userDetail.phoneNumber" : "Please enter a phone number",
				"user.userDetail.email" : {
					required : "Please enter an email address",
					remote : "Email already exists on the system"
				},
				"meters[0].meterId" : {
					required : "Please enter a Meter ID",
					remote : "This meter is not available"
				},
				"user.companyId" : "Please select a company"
			}
		});

		var editUser = {
			settings : {
				dualbox : {
					nonSelectedListLabel : 'Available Roles',
					selectedListLabel : 'Assigned Roles',
					preserveSelectionOnMove : 'moved',
					moveOnSelect : false
				}
			},
			init : function() {
				$("#userEmail").prop("readonly", userHasId());

				$("[name='haveMeter']").click(function(e) {
					if ($(this).is(":checked")) {
						$(".meterId").prop("readonly", false);
					} else {
						$(".meterId").prop("readonly", true);
					}
				});
				$('select#roleCodes').bootstrapDualListbox(editUser.settings.dualbox).change(editUser.handleChangedDualListBox);
				$("select#roleCodes option[value='ROLE_USER']").prop("selected", true);
				editUser.refreshDualBox();

				$("#verified").bootstrapSwitch({
					onText : "Yes",
					offText : "No",
					onSwitchChange : function(event, state) {
						if (state) {
							$(".send-verification-email").addClass("hidden");
						} else {
							$(".send-verification-email").removeClass("hidden");
						}
					}
				});

				$("#sendVerificationEmail").bootstrapSwitch({
					onText : "Yes",
					offText : "No"
				});

				$("#canSeeBillDates").bootstrapSwitch({
					onText : "Yes",
					offText : "No"
				});

				$("button#cancel").click(function() {
					window.history.back();
				});

			},
			refreshDualBox : function() {
				validator.element("#company");
				$('select#roleCodes').bootstrapDualListbox("refresh", true);
			},
			handleChangedDualListBox : function(event) {
				var selectObj = $(this);
				log("EVENT: ", event);
				var allAddedOptions = $(this).children("option:selected");

				var count = 0;
				allAddedOptions.each(function(index, option) {
					if ($(option).val() == 'ROLE_COMPANYADMIN') {
						++count;
						if (count > 1) {
							log("will remove the company admin...");
							selectObj.find("option[value='ROLE_COMPANYADMIN']").prop("selected", false);
						}
					}
					if ($(option).val() == 'ROLE_ADMIN') {
						++count;
						if (count > 1) {
							log("will remove the company admin...2");
							selectObj.find("option[value='ROLE_COMPANYADMIN']").prop("selected", false);
						}
					}
				});
				$(this).children("option[value='ROLE_USER']").prop("selected", true);
				editUser.refreshDualBox();

			}
		};

		editUser.init();

		$("#saveButton").click(function() {
			if ($("#meterForm").valid()) {
				var hasSuperAdminRole = $("select#roleCodes option:selected").filter(function() {
					return this.value == 'ROLE_ADMIN';
				}).length > 0;
				if (hasSuperAdminRole) {
					$("#company").val("");
				}
				$("button").prop("disabled", true);
				$.pageutil.showProcessingMessage("Saving User. Please wait...");
				$("#meterForm").submit();
			}
		});
	});

	function userHasId() {
		return $.trim($("#id").val()) !== '';
	}

	function getEmailValidationRule() {
		var emailValidationRule = {};
		if (userHasId()) {
			$("#userEmail").prop("readonly", true);
			emailValidationRule = {
				required : false
			}
		} else {
			emailValidationRule = {
				required : true,
				email : true,
				remote : {
					url : "${pageContext.request.contextPath}/register/email/validity",
					method : "POST"
				}
			}
		}
		return emailValidationRule;
	}
</script>