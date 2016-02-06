"use strict";
$(function() {
	var configController = {
		db : $("#config-local-db"),
		validator : {
			dataConfig : null,
			serviceConfig : null,
			databaseAction : null,
			emailAction : null
		},
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
				// DB
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
		addActionBehaviour : function(actionType, actionRow) {
			switch (actionType) {
			case "EMAIL":
				break;
			case "DB":
			default:
				// actionRow.find("[type='text']")
				break;
			}
		},
		createModalId : function() {
			return new Date().getTime() + "_" + Math.floor((Math.random() * 100) + 1);
		},
		editDataConfig : function(event) {
			event.preventDefault();
			var modalid = $(event.currentTarget).data("modalid");
			var config = configController.db.data("dataConfigs")[modalid];
			configController.copyDataConfigToControls(config);
			$('#data-config-modal').modal('show');
		},
		editAction : function(event) {
			event.preventDefault();
			var modalid = $(event.currentTarget).data("modalid");
			var config = configController.db.data("actions")[modalid];
			configController.copyActionToControls(config);
			$('#db-action-modal').modal('show');
		},
		callAddDataConfigModal : function(event) {
			$("[name='configType']").val($(event.currentTarget).data("configtype"));
			$("input[name='modalId']").val(configController.createModalId);
			$('#data-config-modal').modal("show");
		},
		callAddDatabaseActionModal : function(event) {
			$("[name='dbActionType']").val("DB");
			$("input[name='dbActionModalId']").val(configController.createModalId);
			$('#db-action-modal').modal("show");
		},
		callAddEmailActionModal : function(event) {
			$("[name='emailActionType']").val("EMAIL");
			$("input[name='modalId']").val(configController.createModalId);
			$('#data-config-modal').modal("show");
		},
		copyDataConfigToControls : function(config) {
			$("[name='configName']").val(config.name);
			$("[name='configType']").val(config.configType);
			$("[name='databaseType']").val(config.databaseType);
			$("[name='databaseUrl']").val(config.url)
			$("[name='username']").val(config.username);
			$("[name='password']").val(config.password);
			$("[name='active']").bootstrapSwitch('state', config.active);
			$("[name='sql']").val(config.sql);
			$("[name='id']").val(config.id);
			$("[name='modalId']").val(config.modalId);
		},
		copyActionToControls: function(config) {
			$("[name='dbActionName']").val(config.name);
			$("[name='dbActionType']").val(config.type);
			$("[name='dbActionDatabaseType']").val(config.databaseType);
			$("[name='dbActionDatabaseUrl']").val(config.url)
			$("[name='dbActionUsername']").val(config.username);
			$("[name='dbActionPassword']").val(config.password);
			$("[name='dbActionActive']").bootstrapSwitch('state', config.active);
			$("[name='dbActionSql']").val(config.sql);
			$("[name='dbActionId']").val(config.id);
			$("[name='dbActionModalId']").val(config.modalId);
		},
		copyControlsToDataConfig : function() {
			return {
				name : $("[name='configName']").val(),
				configType : $("[name='configType']").val(),
				databaseType : $("[name='databaseType']").val(),
				url : $("[name='databaseUrl']").val(),
				username : $("[name='username']").val(),
				password : $("[name='password']").val(),
				active : $("[name='active']").prop("checked"),
				sql : $("[name='sql']").val(),
				id : $("[name='id']").val(),
				modalId : $("[name='modalId']").val()
			};
		},
		copyControlsToDatabaseAction : function() {
			return {
				name : $("[name='dbActionName']").val(),
				type : $("[name='dbActionType']").val(),
				databaseType : $("[name='dbActionDatabaseType']").val(),
				url : $("[name='dbActionDatabaseUrl']").val(),
				username : $("[name='dbActionUsername']").val(),
				password : $("[name='dbActionPassword']").val(),
				active : $("[name='dbActionActive']").prop("checked"),
				sql : $("[name='dbActionSql']").val(),
				id : $("[name='dbActionId']").val(),
				modalId : $("[name='dbActionModalId']").val()
			};
		},
		copyControlsToEmailAction: function(){
			return {
				name : $("[name='emailActionName']").val(),
				type : $("[name='dbActionType']").val(),
				active : $("[name='dbActionActive']").prop("checked"),
				email: $("[name='emailActionEmail']").val(),
				id : $("[name='dbActionId']").val(),
				modalId : $("[name='dbActionModalId']").val()
			};
		},
		cleanup : function() {
			$('.modal').find("input").val("");
			$('.modal').find("textarea").val("");
			$(".modal .switch").bootstrapSwitch('state', true);
			$(".config-income-data .sn").each(function(index, td) {
				$(td).html(++index);
			});
			$(".config-action .sn").each(function(index, td) {
				$(td).html(++index);
			});
			configController.validator.dataConfig.resetForm();
			configController.validator.databaseAction.resetForm();
			$(".has-error").removeClass("has-error");
		},
		removeDataConfig : function(event) {
			var row = $(event.currentTarget).closest(".income-data-row");
			var modalid = row.data("modalid");

			log("before delete: ", configController.db.data("dataConfigs"));
			delete configController.db.data("dataConfigs")[modalid];
			log("after delete: ", configController.db.data("dataConfigs"));

			row.remove();
			var incomeRowsLength = $(".config-income-data .income-data-row").length;
			if (incomeRowsLength === 0) {
				$(".table-data-config > thead > tr").addClass("hidden");
				$(".no-income-data").removeClass("hidden");
			}
			configController.cleanup();
		},
		removeAction : function(event) {
			var row = $(event.currentTarget).closest(".action-row");
			var modalid = row.data("modalid");

			log("before delete: ", configController.db.data("actions"));
			delete configController.db.data("actions")[modalid];
			log("after delete: ", configController.db.data("actions"));

			row.remove();
			var incomeRowsLength = $(".config-actions .action-row").length;
			if (incomeRowsLength === 0) {
				$(".table-action > thead > tr").addClass("hidden");
				$(".no-action").removeClass("hidden");
			}
			configController.cleanup();
		},
		updateRowWithDataConfigValues : function(dataRow, dataConfig) {
			dataRow.find(".sn").html(1);
			dataRow.find(".config-active").html(dataConfig.active === true ? "<i class='fa fa-check'></i>" : "<i class='fa fa-times'></i>");
			dataRow.find(".name").html(dataConfig.name);
			dataRow.find(".config-type").html(dataConfig.configType);
			dataRow.find(".database-type").html(dataConfig.databaseType);
			dataRow.find(".url").html(dataConfig.url);
			dataRow.find("button").data("modalid", dataConfig.modalId).attr("data-modalid", dataConfig.modalId);
			dataRow.data("modalid", dataConfig.modalId).attr("data-modalid", dataConfig.modalId);
		},
		updateRowWithActionValues : function(dataRow, action) {
			dataRow.find(".sn").html(1);
			dataRow.find(".config-active").html(action.active === true ? "<i class='fa fa-check'></i>" : "<i class='fa fa-times'></i>");
			dataRow.find(".name").html(action.name);
			dataRow.find(".action-type").html(action.type);
			dataRow.find("button").data("modalid", action.modalId).attr("data-modalid", action.modalId);
			dataRow.data("modalid", action.modalId).attr("data-modalid", action.modalId);
		},
		addDataConfig : function(event) {
			event.preventDefault();

			// TODO check if form.data-config-form is valid...then go on.

			var formObj = $(".data-config-form");
			if (!formObj.valid()) {
				return false;
			}
			var dataConfig = configController.copyControlsToDataConfig();
			var addTableRow = configController.addDataToDb(dataConfig, dataConfig.modalId, "dataConfigs");
			if (addTableRow) {
				$(".table-data-config > thead > tr").removeClass("hidden");
				$(".no-income-data").addClass("hidden");
				var dataRow = $("#page-templates > .table-row-holder > tbody > .income-data-row").clone(false);
				configController.updateRowWithDataConfigValues(dataRow, dataConfig);
				dataRow.find(".btn-edit-row").click(configController.editDataConfig);
				dataRow.find(".btn-remove-row").click(configController.removeDataConfig);
				dataRow.appendTo(".config-income-data");
			} else {
				var dataRow = $(".config-income-data").find("tr[data-modalid='" + dataConfig.modalId + "']");
				configController.updateRowWithDataConfigValues(dataRow, dataConfig);

			}
			$('#data-config-modal').modal('hide');
			configController.cleanup();
		},
		addDatabaseAction : function(event) {
			event.preventDefault();
			if (!$(".db-action-form").valid()) {
				return false;
			}
			var action = configController.copyControlsToDatabaseAction();
			var addTableRow = configController.addDataToDb(action, action.modalId, "actions");
			if (addTableRow) {
				$(".table-action > thead > tr").removeClass("hidden");
				$(".no-action").addClass("hidden");
				var dataRow = $("#page-templates > .table-row-holder > tbody > .action-row").clone(false);
				configController.updateRowWithActionValues(dataRow, action);
				dataRow.find(".btn-edit-row").click(configController.editAction);
				dataRow.find(".btn-remove-row").click(configController.removeAction);
				dataRow.appendTo(".config-action");
			} else {
				var dataRow = $(".config-action").find("tr[data-modalid='" + action.modalId + "']");
				configController.updateRowWithActionValues(dataRow, action);

			}
			$('#db-action-modal').modal('hide');
			configController.cleanup();
		},
		addEmailAction : function(event) {
			//email-action-modal
			event.preventDefault();
			if (!$(".email-action-form").valid()) {
				return false;
			}
			
			var action = configController.copyControlsToEmailAction();			
			var addTableRow = configController.addDataToDb(action, action.modalId, "actions");
			if (addTableRow) {
				$(".table-action > thead > tr").removeClass("hidden");
				$(".no-action").addClass("hidden");
				var dataRow = $("#page-templates > .table-row-holder > tbody > .action-row").clone(false);
				configController.updateRowWithActionValues(dataRow, action);
				dataRow.find(".btn-edit-row").click(configController.editAction);
				dataRow.find(".btn-remove-row").click(configController.removeAction);
				dataRow.appendTo(".config-action");
			} else {
				var dataRow = $(".config-action").find("tr[data-modalid='" + action.modalId + "']");
				configController.updateRowWithActionValues(dataRow, action);

			}
			$('#email-action-modal').modal('hide');
			configController.cleanup();
		},
		addDataToDb: function(data, key, groupKey){
			var db = configController.db;
			var groupData = db.data(groupKey);			
			if (typeof groupData === "undefined" || groupData === null) {
				groupData = {};
			}			
			var addTableRow = true;
			if (typeof groupData[key] !== "undefined" && groupData[key] !== null) {
				addTableRow = false;
			}
			groupData[key] = data;
			db.data(groupKey, groupData);			
			return addTableRow;			
		},
		initValidation : function() {
			$("#serviceConfigForm").validate();
			configController.validator.dataConfig = $(".data-config-form").validate({
				rules : {
					confirmPassword : {
						equalTo : "[name='password']"
					}
				},
				messages : {
					configName : "Config name required",
					configType : "Config type required",
					databaseType : "Database type required",
					databaseUrl : {
						required : "Database URL required"
					},
					username : "Username required",
					password : "Password required",
					sql : "SQL required"
				}
			});

			configController.validator.databaseAction = $(".db-action-form").validate({
				rules : {
					dbActionConfirmPassword : {
						equalTo : "[name='dbActionPassword']"
					}
				},
				messages : {
					dbActionName : "Config name required",
					dbActionType : "Config type required",
					dbActionDatabaseType : "Database type required",
					dbActionDatabaseUrl : {
						required : "Database URL required"
					},
					dbActionUsername : "Username required",
					dbActionPassword : "Password required",
					dbActionSql : "SQL required"
				}
			});
		},
		init : function() {
			this.initValidation();
			$(".switch").bootstrapSwitch({
				onText : "Yes",
				offText : "No"
			});
			$("#saveButton").click(this.submitForm);
			// $(".btn-add-action").click(this.addAction);
			$(".btn-add-income-data-config").click(this.callAddDataConfigModal);
			$(".btn-save-config").click(this.addDataConfig);

			$(".btn-db-action").click(this.callAddDatabaseActionModal);
			$(".btn-save-db-action").click(this.addDatabaseAction);

			$(".btn-email-action").click(this.callAddEmailActionModal);
			$(".btn-save-email-action").click(this.addEmailAction);

			$('#data-config-modal, #db-action-modal, #email-action-modal').modal({
				show : false
			});

			$('#data-config-modal, #db-action-modal, #email-action-modal').on('hidden.bs.modal', function(e) {
				configController.cleanup();
			});
		}
	};
	configController.init();
});