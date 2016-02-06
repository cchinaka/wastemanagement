(function($) {
	$.pageutil = {
		settings : {
			context : "/",
			loadingMessage : null,
			defaultLoadingMessage : "Loading. Please wait...",
			captcha : {
				url : "/guest/captcha/validate",
				defaultErrorMessage : "Problem verifying your challenge text.",
				errorSelector : ".recaptcha_error",
				loaderRefresh : "span.load-refresh i.fa-refresh",
				loaderDiv : "div.loaderinfo",
				refreshButton : "span.input-group-addon > button.refresh",
				buttons : "span.input-group-addon > button",
				rh : null,
				input : null
			},
			loaders : {
				inline : "<img src='/static/custom/images/inlineloader.gif' class='loader' />",
				inlineCss : "<small><i class='fa fa-spin fa-circle-o-notch'></i></small>",
				page : "<img src='/static/custom/images/loading.gif' class='loader' />"
			},
			dataTable : {
				serverOptions : {
					"processing" : true,
					"serverSide" : true,
					"lengthMenu" : [ [ 10, 25, 50, 100 ], [ 10, 25, 50, 100 ] ],
					"bLengthChange" : true,
					"bFilter" : false,
					"bSort" : false,
					"bInfo" : true,
					"bAutoWidth" : true
				}
			}
		},
		init : function(context) {
			this.settings.context = context;
			this.initializeValidationDefaults();
			this.addValidationMethods();
			this.initModals();
			this.initAlerts();
			this.initOrderViewClicker();
			this.initializeAjaxSetup();
		},
		initializeTicketCounter: function(){
			$.ajax({
				url: "/user/tickets/unclosed",
				beforeSend: function(){},
				error: function(err){},
				success: function(data){
					$(".menu-unclosed-tickets").html(data).removeClass("hidden");
				}
			});
		},
		initializeAjaxSetup : function() {
			$.ajaxSetup({
				beforeSend : function() {
					if ($.pageutil.settings.loadingMessage !== undefined && $.pageutil.settings.loadingMessage !== null && $.pageutil.settings.loadingMessage !== '') {
						$.pageutil.showProcessingMessage($.pageutil.settings.loadingMessage);
					} else {
						$.pageutil.showProcessingMessage($.pageutil.settings.defaultLoadingMessage);
					}
				},
				complete : function() {
					$.pageutil.removeProcessingMessage();
				}
			});
		},
		showProcessingMessage : function(message) {
			$("div.messages-loading span.span-loading").html(message);
			$("div.messages-loading").removeClass("hidden");
		},
		removeProcessingMessage : function() {
			$.pageutil.settings.loadingMessage = null;
			$("div.messages-loading span.span-loading").html("");
			$("div.messages-loading").addClass("hidden");
		},
		initializeValidationDefaults : function() {
			$.validator.setDefaults({
				errorElement : 'span',
				errorClass : 'has-error',
				errorPlacement : function(error, element) {
					if (element.parent('.input-group').length) {
						error.insertAfter(element.parent());
					} else {
						error.insertAfter(element);
					}
				},
				highlight : function(element) {
					$(element).closest('.form-group').addClass('has-error');
				},
				unhighlight : function(element) {
					$(element).closest('.form-group').removeClass('has-error');
				},
				onkeyup : function(element, event) {
					for ( var rule in $(element).rules()) {
						if (rule.indexOf("remote") > 0) {
							return false;
						}
					}
					return true;
				}
			});

			/*
			 * $(window).keydown(function(event) { if (event.keyCode == 13) {
			 * event.preventDefault(); return false; } });
			 */
		},
		addValidationMethods : function() {
			$.validator.addMethod("notEqualCurrentPassword", function(value, element, param) {
				return this.optional(element) || value.trim() != $(param).val().trim();
			}, $.validator.format("You cannot reuse your current password"));
		},
		loadDiv : function(id, url) {
			$(id).load(url);
		},
		initAlerts : function() {
			$(".alert").alert();
		},
		setupToggleCheckers : function() {

			$.each($("input[type='checkbox'].toggle-checker"), function(index, toggler) {
				try {
					$(toggler).click(function() {
						if ($(this).is(":checked")) {
							$("input[type='checkbox'].toggle-checkee").prop("checked", true);
						} else {
							$("input[type='checkbox'].toggle-checkee").prop("checked", false);
						}
					});
				} catch (err) {
					// debug(err);
				}
			});

			$("input[type='checkbox'].toggle-checkee").click(function(index, checkbox) {
				if ($(this).not(":checked")) {
					$("input[type='checkbox'].toggle-checker").prop("checked", false);
				}
			});
		},
		setupRecordToggleCheckers : function(checkerClass, checkeeClass) {

			$.each($("input[type='checkbox'].toggle-checker"), function(index, toggler) {
				try {
					$(toggler).click(function() {
						if ($(this).is(":checked")) {
							$("input[type='checkbox'].toggle-checkee").prop("checked", true);
						} else {
							$("input[type='checkbox'].toggle-checkee").prop("checked", false);
						}
					});
				} catch (err) {
					// debug(err);
				}
			});

			$("input[type='checkbox'].toggle-checkee").click(function(index, checkbox) {
				if ($(this).not(":checked")) {
					$("input[type='checkbox'].toggle-checker").prop("checked", false);
				}
			});
		},
		loadContent : function(selector, url) {
			return this.postContent(selector, url, {
				method : "GET"
			});
		},
		postContent : function(selector, url, params) {
			var deferred = $.ajax($.extend({
				method : "POST",
				url : url,
				success : function(page) {
					log("setting page...");
					$(selector).html(page);
				},
				error : function(err) {
					log(err);
				},
				cache : false
			}, params));
			return deferred.promise();
		},
		callConfirmModal : function(title, messageContent, callback, cancelCallback) {
			$("#app-modal-confirm h4.modal-title").html(title);
			$("#app-modal-confirm div.modal-body").html(messageContent);
			$("#app-modal-confirm").modal("show");
			log("show...", $("#app-modal-confirm"));
			$("#app-modal-confirm div.modal-footer").show();
			$("#app-modal-confirm button.btn-primary").off("click").click(function() {
				$("#app-modal-confirm").modal("hide");
				try {
					log("callback called: ", callback);
					if (callback !== undefined) {
						callback();
					}
				} catch (err) {
					log("problem calling callback", err);
				}
			});
			$("#app-modal-confirm button.btn-default,#app-modal-confirm button.close").off("click").click(function() {
				log("cancelling request...");
				$("#app-modal-confirm").modal("hide");
				try {
					log("cancel callback called: ", callback);
					if (cancelCallback !== undefined) {
						cancelCallback();
					}
				} catch (err) {
					log("problem calling cancelCallback", err);
				}
			});
		},
		callAlertModal : function(messageContent) {
			$("#app-modal-confirm h4.modal-title").html("<i class='text-danger fa fa-exclamation-circle'></i> Alert");
			$("#app-modal-confirm div.modal-body").html(messageContent);
			$("#app-modal-confirm").modal("show");
			$("#app-modal-confirm div.modal-footer").hide();
			$("#app-modal-confirm button.close").off("click").click(function() {
				$("#app-modal-confirm").modal("hide");
			});
		},
		initModals : function() {
			$("#app-modal-confirm").modal({
				show : false
			});
		},

		createRecaptcha : function(options) {
			try {
				Recaptcha.create(options.publicKey, options.element, {
					theme : "custom",
					custom_theme_widget : 'recaptcha_widget'
				});
			} catch (err) {
				log("Can't create captcha", err);
			}
		},
		veryfiyRecaptcha : function(options) {
			var deferred = $.ajax({
				data : {
					recaptcha_challenge_field : Recaptcha.get_challenge(),
					recaptcha_response_field : Recaptcha.get_response()
				},
				beforeSend : function() {
					var captchaSettings = $.pageutil.settings.captcha;
					$(captchaSettings.errorSelector).addClass("hidden");
					$(captchaSettings.loaderDiv).removeClass("hidden").html("Validating captcha. Please wait...");
					$(captchaSettings.loaderRefresh).addClass("fa-spin");
					$(captchaSettings.refreshButton).addClass("btn-warning");
					$(captchaSettings.buttons).prop("disabled", true);
					$(options.caller).prop("disabled", true);
				},
				url : $.pageutil.settings.captcha.url,
				method : "post",
				error : function(err) {
					$.pageutil.showCaptchaError();
					$.pageutil.reloadCaptcha();
					$.publish("captcha_error", [ err ]);
				},
				success : function(data) {
					$.publish("captcha_success", [ data ]);
				},
				complete : function() {
					var captchaSettings = $.pageutil.settings.captcha;
					$(captchaSettings.loaderRefresh).removeClass("fa-spin");
					$(captchaSettings.loaderDiv).addClass("hidden").html("");
					$(captchaSettings.refreshButton).removeClass("btn-warning");
					$(captchaSettings.buttons).prop("disabled", false);
					$(options.caller).prop("disabled", false);
				},
				cache : false
			});
			return deferred.promise();
		},
		showCaptchaError : function(message) {
			if (message === undefined || message === null || $.trim(message) === '') {
				message = $.pageutil.settings.captcha.defaultErrorMessage;
			}
			$($.pageutil.settings.captcha.errorSelector).html(message).removeClass("hidden");
		},
		reloadCaptcha : function() {
			log('reloading captcha');
			$.publish("captcha_reloaded", [ "reloaded" ]);
			Recaptcha.reload();
			Recaptcha.focus_response_field();
		},
		getValue : function(value, defaultValue) {
			return value === undefined || value === null || $.trim(value) === '' ? defaultValue : value;
		},
		callItemsDeletion : function(callback) {
			if ($("input.toggle-checkee:checked").length > 0) {
				$.pageutil.callConfirmModal("Confirm Deletion", "Are you sure you want to delete the selected records?", function() {
					try {
						callback();
					} catch (err) {
						log("problem calling callback", err);
					}
				});
			} else {
				$.pageutil.callAlertModal("Please ensure you've selected at least one record.");
			}
		},
		createDotCaptcha : function(captchaOptions) {
			try {
				$.pageutil.settings.captcha.input = $(captchaOptions.control);
				$.pageutil.settings.captcha.url = captchaOptions.url;
				$.subscribe("__rh", $.pageutil.addDotRh);
				$.pageutil.settings.captcha.input.realperson({
					length : captchaOptions.length
				});
				$(captchaOptions.refreshButton).click($.pageutil.reloadDotCaptcha);
				$(captchaOptions.textCollector).blur(function() {
					$.pageutil.settings.captcha.input.val(this.value);
				});
			} catch (err) {
				log("Can't create dot captcha", err);
			}
		},
		addDotRh : function(data) {
			$.pageutil.settings.captcha.rh = data;
		},
		reloadDotCaptcha : function(event) {
			$(".realperson-regen").click();
			$(".captcha-input").val("");

		},
		verifyDotCaptcha : function(options) {
			var deferred = $.ajax({
				data : {
					rh : $.pageutil.settings.captcha.rh,
					challengeResponse : $.pageutil.settings.captcha.input.val()
				},
				beforeSend : function() {
					var captchaSettings = $.pageutil.settings.captcha;
					$(captchaSettings.errorSelector).html("Validating captcha. Please wait...");
					$(captchaSettings.loaderRefresh).addClass("fa-spin");
					$(captchaSettings.refreshButton).addClass("btn-warning");
					$(captchaSettings.buttons).prop("disabled", true);
					$(options.caller).prop("disabled", true);
				},
				url : $.pageutil.settings.captcha.url,
				method : "post",
				error : function(err) {
					$.pageutil.showCaptchaError();
					$.pageutil.reloadDotCaptcha();
					$.publish("captcha_error", [ err ]);
				},
				success : function(data) {
					$($.pageutil.settings.captcha.errorSelector).html("");
					$.publish("captcha_success", [ data ]);
				},
				complete : function() {
					var captchaSettings = $.pageutil.settings.captcha;
					$(captchaSettings.loaderRefresh).removeClass("fa-spin");
					$(captchaSettings.loaderDiv).addClass("hidden").html("");
					$(captchaSettings.refreshButton).removeClass("btn-warning");
					$(captchaSettings.buttons).prop("disabled", false);
					$(options.caller).prop("disabled", false);
				},
				cache : false
			});
			return deferred.promise();
		},
		showDotCaptchaError : function(message) {
			$.pageutil.showCaptchaError(message);
			$.pageutil.reloadDotCaptcha();
		},
		loadJson : function(options) {
			var deferred = $.ajax($.extend({
				method : "get",
				cache : false
			}, options));
			return deferred.promise();
		},
		initOrderViewClicker : function() {
			$(".view-order-event").click(function() {
				alert("view order" + $(this).data('orderid'));
			});
		},
		applyCountTo : function(selector, amount, format) {
			$(selector).countTo({
				from : 0,
				to : amount,
				speed : 1000,
				formatter : function(value, options) {
					return value.toFixed(options.decimals);
				},
				onComplete : function(value) {
					$(this).html(moneyutil.getFormattedMoney(amount, $.extend({
						format : "#,##0"
					}, format)));
				}
			});
		},
		createAjaxDisplayTable : function(options) {
			var pageObj = {
				url : options.url,
				getUrl : function() {
					return this.url;
				},
				table : null,
				init : function() {
					pageObj.table = $(options.tableId).DataTable($.extend($.pageutil.settings.dataTable.serverOptions, {
						"ajax" : {
							"url" : pageObj.getUrl(),
							"method" : "post",
							"dataSrc" : options.resolveData
						}
					}, options.settings));
				}
			};
			pageObj.init();
			return pageObj;
		},
		initializeCrudOperations : function(options) {
			$.pageutil.setupToggleCheckers();
			$(options.addSelector).click(function() {
				window.location.href = options.addUrl;
			});

			$(options.deleteSelector).click(function() {
				if ($(".toggle-checkee:checked").length > 0) {
					$.pageutil.callConfirmModal("Delete Item(s)", "Are you sure you want to delete the selected item(s)", function() {
						$(options.formSelector).prop("action", options.deleteUrl).submit();
					});
				}
			});
		},
		initializeExporter : function(params, beforeExportCallback) {
			log("INITIALIZING EXPORTER...");
			$(".btn-export").click(function() {
				try {
					beforeExportCallback();
				} catch (error) {
					log("couldn't call the before export callback...", error);
				}
				params.control = this;
				$.pageutil.exportData(params);
			});

		},
		exportData : function(params) {
			var path = $(params.control).data("path");
			if (path === undefined || path === null || $.trim(path) === '') {
				path = "pdf";
			}


			var reportTitle = params.reportTitle;
			var footerRow = $(params.control).closest("tr.export-data-row");
			var footerReportTitle = footerRow.data('reporttitle');
			if (footerReportTitle !== undefined && footerReportTitle !== null && $.trim(footerReportTitle) === '') {
				reportTitle = footerReportTitle;
			}

			var rootAction = params.rootAction;
			var footerRootAction = footerRow.data("rootaction");
			if (footerRootAction !== undefined && footerRootAction !== null && $.trim(footerRootAction) !== '') {
				rootAction = footerRootAction;
			}

			log("rootAction: ", rootAction);

			var exportKey = params.exportKey;
			var footerExportKey = footerRow.data("exportkey");
			if (footerExportKey !== undefined && footerExportKey !== null && $.trim(footerExportKey) !== '') {
				exportKey = footerExportKey;
			}



			$("<input>").prop({
				type : "hidden",
				name : "reportTitle",
				value : reportTitle
			}).appendTo($(params.formSelector));

			$("<input>").prop({
				type : "hidden",
				name : "exportKey",
				value : exportKey
			}).appendTo($(params.formSelector));

			try {
				$("<input>").prop({
					type : "hidden",
					name : "order[0][column]",
					value : params.table.order()[0][0]
				}).appendTo($(params.formSelector));

				$("<input>").prop({
					type : "hidden",
					name : "order[0][dir]",
					value : params.table.order()[0][1]
				}).appendTo($(params.formSelector));
			} catch (err) {
				log(err);
			}

			$(params.formSelector).prop({
				"action" : rootAction + "/print/" + path,
				"target" : "_blank"
			}).submit();

			$(params.formSelector).find("input[name='reportTitle']").remove();
			$(params.formSelector).find("input[name='exportKey']").remove();
			$(params.formSelector).find("input[name='order[0][column]']").remove();
			$(params.formSelector).find("input[name='order[0][dir]']").remove();

		},
		loadBranches : function(companyId, pageContext) {
			// var companyId = event.currentTarget.value;
			if (companyId === '') {
				return false;
			}

			$.ajax({
				url : pageContext + "/user/orderpayable/deduction/company/" + companyId + "/branches",
				beforeSend : function() {
					$.pageutil.showProcessingMessage("Loading Branches. Please wait...");
					$(".branch-id option:not(.no-value)").remove();
				},
				success : function(branches) {
					if (branches.length > 0) {
						$.each(branches, function(index, branch) {
							$("<option>").prop({
								text : branch.name,
								value : branch.id
							}).appendTo($(".branch-id"));
						});
					}
				},
				error : function(err) {
					log("error: ", err);
				}
			});
		},
		disableControls : function(formSelector) {
			if (formSelector !== undefined && formSelector !== null) {
				$(formSelector).find("input").prop("disabled", true);
				$(formSelector).find("select").prop("disabled", true);
				$(formSelector).find("textarea").prop("disabled", true);
			} else {
				$("input,select,textarea").prop("disabled", true);
			}
		},
		handleAdvancedSearchToggle : function() {
			$(".show-advanced-search").click(function() {
				$(this).addClass("hidden");
				$("#search-div").removeClass("hidden");
			});
			$(".hide-search-div").click(function() {
				$(".show-advanced-search").removeClass("hidden");
				$("#search-div").addClass("hidden");
			});
		},

		loadAddressComponent : function(pageContext, theControl) {
			// pageContext, control, data-childselector, data-addressfieldtype

			var control = $(theControl);
			var controlValue = control.val();
			var valueDontExist = (controlValue === undefined || controlValue === null || $.trim(controlValue) === '');
			var currentValueExist = (control.data('currentvalue') !== undefined && control.data('currentvalue') !== null && control.data('currentvalue') !== '');

			if (!control.hasClass("address-dirty") && valueDontExist && currentValueExist) {
				controlValue = control.data('currentvalue');
			}

			valueDontExist = (controlValue === undefined || controlValue === null || $.trim(controlValue) === '');

			var childControl = $(control.data('childselector'));

			if (control.hasClass("address-street")) {
				var isOtherSelected = control.find("option:selected").hasClass("is-other");
				var otherCtrl = $(".address-street-other");
				if (isOtherSelected || ($.trim(otherCtrl.val()) !== '' && !control.hasClass("address-dirty"))) {
					control.find("option[class='is-other']").prop("selected", true);
					$(".address-street-other").closest("div.form-group").removeClass("hidden");
				} else if (!control.hasClass("address-dirty") && currentValueExist) {
					control.val(control.data('currentvalue'));
					$(".address-street-other").closest("div.form-group").addClass("hidden");
					$(".address-street-other").val('');
				} else {
					$(".address-street-other").closest("div.form-group").addClass("hidden");
					$(".address-street-other").val('');
				}

				childControl.change();
				$.pageutil.removeProcessingMessage();
			} else {
				$.ajax({
					url : pageContext + "/guest/avs/lookup/" + control.data('addressfieldtype') + "/" + controlValue,
					beforeSend : function() {
						$.pageutil.showProcessingMessage("Preparing address. Please wait...");
						childControl.find("option:not(.no-value)").remove();
						if (!control.hasClass("address-dirty")) {
							control.val(controlValue);
						} else {
							childControl.addClass("address-dirty");
						}
					},
					success : function(result) {
						var children = result.data;
						if (children.length > 0) {
							$.each(children, function(index, child) {
								$("<option>").prop({
									text : child.name,
									value : child.id
								}).appendTo(childControl);
							});
						}
					},
					error : function(err) {
						//log("error: ", err);
					},
					complete : function(response) {
						if (childControl.data('hasother') !== undefined && childControl.data('hasother') !== null && $.trim(childControl.data('hasother')) !== '' && childControl.data('hasother') === 'yes') {
							$("<option>").prop({
								text : "Other",
								value : "",
								class : "is-other"
							}).appendTo(childControl);
						}

						if (control.data('hasother') !== undefined && control.data('hasother') !== null && $.trim(control.data('hasother')) !== '' && control.data('hasother') === 'yes') {
							var controlClasses = control.attr("class").split(/\s+/);
							var otherDiv = null;
							var otherCtrl = null;
							$.each(controlClasses, function(index, element) {
								if (element.indexOf("address-") >= 0 && element !== 'address-dirty') {
									otherCtrl = $("." + element + "-other");
									otherDiv = otherCtrl.closest("div.form-group");
									return false;
								}
							});
							// || control.val() == "" &&
							// $.trim(otherCtrl.val())!=='';
							var isOtherSelected = control.find("option:selected").hasClass("is-other");
							log("otherCtrl.val(): ", otherCtrl.val());
							if (isOtherSelected || ($.trim(otherCtrl.val()) !== '' && !control.hasClass("address-dirty"))) {
								log('control.find("option.is-other"): ', control.find("option.is-other"));
								control.find("option[class='is-other']").prop("selected", true);
								otherDiv.removeClass("hidden");
							} else {
								otherDiv.addClass("hidden");
								otherCtrl.val('');
							}
						}

						childControl.change();
						$.pageutil.removeProcessingMessage();
					}
				});
			}
		},
		saveAddressComponent : function() {
			$("#addressForm").ajaxSubmit();
		},
		initializeAjaxFormSubmit : function(formSelector, shouldValidate, callback, errorCallback) {
			if (shouldValidate) {
				if (!$(formSelector).valid()) {
					return false;
				}
			}

			$(formSelector).submit(function(e) {
				e.preventDefault(); // STOP default action
				// e.unbind(); // unbind. to stop multiple form submit.
				var postData = $(this).serializeArray();
				var formURL = $(this).prop("action");
				$.ajax({
					url : formURL,
					type : "POST",
					data : postData,
					success : function(data, textStatus, jqXHR) {
						if (callback !== undefined) {
							callback(data);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						if (errorCallback !== undefined) {
							errorCallback();
						}
					}
				});
			});
		},
		initializeComments: function(options){
			if(options.ownerId!==undefined && options.ownerId!== null && parseInt(options.ownerId) > 0){
				$.ajax({
					url: options.url,
					method: "get",
					data:{
						commentType: options.commentType,
						ownerId: options.ownerId
					},
					success: function(comments){
						log("comments: ", comments);
					}
				});
			}
		}
	};
	$.pageutil.init();
}(jQuery));

function log(obj, obj2) {
	if (window.console && console.log) {
		if (obj2 !== null) {
			console.log(obj, obj2);
		} else {
			console.log(obj);
		}
	}
}
