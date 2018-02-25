var dtLocalizationLinkConst;

function initHtml(container, dtLocalizationLink) {
	if (typeof dtLocalizationLink != 'undefined') {
		dtLocalizationLinkConst = dtLocalizationLink;
	}
	
	container.find(".modal").each(function() {
		initModal($(this));
	});
	container.find(".colorpicker-component").colorpicker({
		format : 'hex6', 
		useAlpha : false,
		useHashPrefix : false
	});
	container.find(".datepicker-component").each(function() {
		initDatePicker($(this), 'month');
	});
	container.find(".datetimepicker-component").each(function() {
		initDatePicker($(this), 'hour'); 
	});
	container.find("table.datatables").each(function() {
		initDatatables($(this), dtLocalizationLinkConst);
	});
	container.find('.selectpicker.ajax-sourced').on('show.bs.select', function(e) {
		loadAjaxSourced($(this));
	});
	container.find('.fileinput').each(function() {
		initFileInput($(this), '', false);
	});
}

function emptyValue(val) {
	return typeof val == 'undefined' || val == null || val == '' || val.length == 0;
}

function removeFormErrorMessage(form) {
	form.find(".errorHolder").html("");
}

function parseDate(datestr) {
	var d = datestr.split(" ");
	
	for (i = 0; i < 6; i++) {
		if (typeof d[i] === 'undefined') {
			d[i] = 0;
		}
	}
	
	d[1] = Number(d[1]) - 1;
	var date = new Date(d[0], d[1], d[2], d[3], d[4], d[5]);
	return date;
}

function formatDate(date, format, locale) {
	var dateUTC = new Date(date.valueOf() - date.getTimezoneOffset() * 60000);
	var dpg = $.fn.datetimepicker.DPGlobal;
	if (format === "DATE") {
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableDateFormat'], 'standard'), locale, 'standard');
	} else {
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableDateTimeFormat'], 'standard'), locale, 'standard');
	}	
}

function removeFormFieldErrorMessage(form) {
	form.find("input,textarea").not('[type="submit"]').each(function() {
		removeErrorMessage($(this));
	});
}

function setFormErrorMessage(form, message) {
	var errHolder = form.find(".errorHolder");
	var errDiv = errHolder.find(".alert-danger");
	if (!errDiv.length) {
		errHolder.append('<div class="alert alert-danger">' + message
				+ '</div>');
	} else {
		errDiv.append('<br/>' + message);
	}
}

function removeErrorMessage(element) {
	var parent = element.closest(".form-group");

	if (parent == null) {
		return;
	}
	parent.removeClass("has-error");
	var err = parent.find(".help-block");
	if (!err.hasClass("file-help-block")) {
		err.remove();
	}
}

function setErrorMessage(element, message) {
	var parent = element.closest(".form-group");

	if (parent == null) {
		return;
	}
	parent.addClass("has-error");
	var help = parent.find(".help-block");
	if (!help.length) {
		parent.append('<span class="help-block">' + message + '</span>');
	} else {
		help.append('<br/>' + message);
	}
}

function setErrorMessageStrict(element, message) {
	element.parent().addClass("has-error");
	var help = element.parent().find(".help-block");
	if (!help.length) {
		element.parent().append(
				'<span class="help-block">' + message + '</span>');
	} else {
		help.html(message);
	}
}

function showConditional(elem) {
	var cond = elem.attr("show-if");
	if (typeof cond == 'undefined') {
		return;
	}
	var show = eval(cond);
	if (show) {
		elem.removeClass("ignore-value");
		elem.closest(".form-group").removeClass("hidden");
	} else {
		elem.addClass("ignore-value");
		elem.closest(".form-group").addClass("hidden");
	}
}

function showConditionalForm(form) {
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		showConditional($(this));
	});
}

function checkElem(elem, rule) {
	if (rule == "required") {
		if (emptyValue(getData(elem))) {
			setErrorMessage(elem, translations['required']);
			return false;
		}
	}
	
	if (rule == "onerequired" || rule == "allrequired") {
		var par = elem.closest(".form-group");
		var filled = 0;
		var total = 0;
		par.find("input,textarea").each(function(){
			total++;
			if (getData($(this)).length > 0) {
				filled++;
			}
		});
		
		if (rule == "onerequired" && filled == 0) {
			setErrorMessage(elem, translations['oneRequired']);
			return false;
		}
		
		if (rule == "allrequired" && filled < total) {
			setErrorMessage(elem, translations['allRequired']);
			return false;			
		}
	}

	if (rule.indexOf("min=") == 0) {
		var len = Number(rule.substring(4));
		if (getData(elem).length > 0 && getData(elem).length < len) {
			setErrorMessage(elem, translations['minLen'].replace("{0}", len));
			return false;
		}
	}

	if (rule.indexOf("max=") == 0) {
		var len = Number(rule.substring(4));
		if (getData(elem).length > len) {
			setErrorMessage(elem, translations['maxLen'].replace("{0}", len));
			return false;
		}
	}

	if (rule.indexOf("minval=") == 0) {
		var val = Number(rule.substring(4));
		if (getData(elem).length > 0 && Number(getData(elem)) < val) {
			setErrorMessage(elem, translations['min'].replace("{0}", len));
			return false;
		}
	}

	if (rule.indexOf("maxval=") == 0) {
		var val = Number(rule.substring(4));
		if (getData(elem).length > 0 && Number(getData(elem)) > val) {
			setErrorMessage(elem, translations['max'].replace("{0}", len));
			return false;
		}
	}

	if (rule == "pwd" && !emptyValue(getData(elem))) {
		if (!/^(?=.*[A-Za-z])(?=.*\d).{8,}$/.test(getData(elem))) {
			setErrorMessage(elem, translations['passwordStrength']);
			return false;
		}
	}

	if (rule == "email" && !emptyValue(getData(elem))) { 
		if (!/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/
				.test(getData(elem))) {
			setErrorMessage(elem, translations['invalidEmail']);
			return false;
		}
	}

	return true;
}

function checkInput(elem) {
	removeErrorMessage(elem);
	var checkList = elem.attr("jschecks");
	if (typeof checkList === 'undefined') {
		return true;
	}
	checkList = checkList.split(";");
	var result = true;

	checkList.forEach(function(item, i, arr) {
		if (!checkElem(elem, item)) {
			result = false;
		}
	});

	return result;
}

function onColChange(elem) {
	var result = checkInput(elem);
	var form = elem.closest("form");
	showConditionalForm(form);
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var inp = $(this);
				
		if ((":" + inp.attr('ajax-parms') + ":").indexOf(":" + elem.attr("id") + ":") > -1) {
			setInputValue(inp, "");
		}
	});
	
	return result;
}

function checkPwdIdent(pwd1, pwd2) {
	if (pwd1.val() != pwd2.val()) {
		setErrorMessageStrict(pwd2, translations['passwordMatch']);
		return false;
	}

	return true;
}

function checkPwdInput(pwd, pwdConf) {
	var res1 = checkInput(pwd);
	var res2 = checkInput(pwdConf);
	var res3 = checkPwdIdent(pwd, pwdConf);

	return res1 && res2 && res3;
}

function validateForm(form, event) {
	var result = true;

	form.find("input,select,textarea").not(".ignore-value").not('[type="submit"]').not('[type="hidden"]').each(function() {
		var elem = $(this);
		if (elem.attr('type') != 'password') {
			if (!checkInput(elem)) {
				result = false;
			}
		} else {
			if (!eval(elem.attr("onchange"))) {
				result = false;
			}
		}
	});

	return result;
}

function setInputValue(elem, val) {
	if (elem.attr('name') != "_csrf") {
		if (elem.prop('tagName') === 'SELECT') {
			if (typeof val !== 'undefined') {
				elem.selectpicker('val', val);
			} else {
				elem.selectpicker('val', "");
			}
			return;
		}
		
		if (elem.prop('tagName') === 'INPUT' || elem.prop('tagName') === 'TEXTAREA') {
			var dt = elem.attr('display-type');
			
			if (dt === 'checkbox') {
				if (typeof val !== 'undefined') {
					elem.prop("checked", val);
				} else {
					elem.prop("checked", false);
				}
			} else if (dt === 'colorpicker') {
				if (typeof val !== 'undefined') {
					elem.parent().colorpicker('setValue', val);
				} else {
					elem.parent().colorpicker('setValue', "");
				}
			} else if (dt === 'datepicker') {
				var picker = elem.closest('.form-group').find('.datepicker-component');
				if (typeof val !== 'undefined') {
					picker.datetimepicker('update', parseDate(val));
				} else {
					picker.datetimepicker('update', "");
				}
			} else if (dt === 'datetimepicker') {
				var picker = elem.closest('.form-group').find('.datetimepicker-component');
				if (typeof val !== 'undefined') {
					picker.datetimepicker('update', parseDate(val));
				} else {
					picker.datetimepicker('update', "");
				}
			} else if (dt === 'filepicker') { 
				var initial;
				if (val) {
					initial = elem.attr('initial');
					var parms = elem.attr('initial-parms');
					if (typeof parms !== 'undefined') {
						parms = parms.split(":");
						parms.forEach(function(item, i, arr) {
							var from = $("#" + item);
							initial = initial.replace("{" + i + "}", encodeURIComponent(getData(from)));
						});
					}
				}
				initFileInput(elem, initial, true);
			} else {
				if (typeof val !== 'undefined') {
					elem.val(val);
				} else {
					elem.val("");
				}
			}
		}
	}
}

function loadAjaxSourcedSuccess(form, elem, jsonUrl, val, data) {
	if (data.responseClass == "INFO") {
		optionsHtml = "";

		if (!elem.prop('multiple')) {
			optionsHtml += "<option value=''></option>"
		}
		
		data.options.forEach(function(item, i, arr) {
			optionsHtml += "<option value='" + item['key'] + "'>" + item['value'] + "</option>";
		});
		
		if (data.search) {
			elem.closest(".form-group").find(".bs-searchbox").removeClass("hidden");
		} else {
			elem.closest(".form-group").find(".bs-searchbox").addClass("hidden");
		}
		elem.html(optionsHtml).selectpicker("refresh");
		elem.selectpicker('val', val);
		elem.attr("loaded-from", jsonUrl);
		return;
	}

	elem.attr("loaded-from", null);
	removeFormErrorMessage(form);
	removeErrorMessage(elem);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
	}
}

function loadAjaxSourcedError(form, elem, data) {
	elem.attr("loaded-from", null);
	removeFormErrorMessage(form);
	removeErrorMessage(elem);
	if (data.statusText = "error" && data.status != 0) {
		if (data.status == 403) {
			setFormErrorMessage(form, translations['forbidden']);
		} else {
			setFormErrorMessage(form, translations['ajaxErr'].replace("{0}",
					data.status));
		}
	} else {
		setFormErrorMessage(form, translations['ajaxHangPost']);
	}
}

function loadAjaxSourced(elem) {
	var form = elem.closest("form");
	var jsonUrl = elem.attr('ajax-data');
	var proceed = true;
	if (typeof elem.attr('ajax-parms') !== 'undefined') {
		var jsonParms = elem.attr('ajax-parms').split(':');
		jsonParms.forEach(function(item, i, arr) {
			var from = $("#" + item);
			if (!checkInput(from)) {
				proceed = false;
			}
			jsonUrl = jsonUrl.replace("{" + i + "}", encodeURIComponent(getData(from)));
		});
	}
	
	if (jsonUrl === elem.attr("loaded-from")) {
		return false;
	}
	
	var val = elem.selectpicker('val');
	elem.html("").selectpicker("refresh");
	
	if (!proceed) {
		return false;
	}

	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : jsonUrl,
		dataType : "json",
		timeout : 10000,
		success : function(data) {
			loadAjaxSourcedSuccess(form, elem, jsonUrl, val, data);
		},
		error : function(data) {
			loadAjaxSourcedError(form, elem, data);
		}
	});
	
	return true;
}

function initAjaxSourcedSuccess(form, elem, jsonUrl, val, data) {
	if (data.responseClass == "INFO") {
		optionsHtml = "";
		
		if (!elem.prop('multiple')) {
			optionsHtml += "<option value=''></option>"
		}
		
		data.options.forEach(function(item, i, arr) {
			optionsHtml += "<option value='" + item['key'] + "'>" + item['value'] + "</option>";
		});
		
		if (data.search) {
			elem.closest(".form-group").find(".bs-searchbox").removeClass("hidden");
		} else {
			elem.closest(".form-group").find(".bs-searchbox").addClass("hidden");
		}
		elem.html(optionsHtml).selectpicker("refresh");
		elem.attr("loaded-from", jsonUrl);

		elem.selectpicker('val', val);
		
		form.data('loading', form.data('loading') - 1);
		if (form.data('loading') == 0) {
			showConditionalForm(form);
			changeModalFormState(form, false, false, false);			
		}
		return;
	}

	removeFormErrorMessage(form);
	removeErrorMessage(elem);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
		form.data('loading', -1);
		changeModalFormState(form, true, false, false);
	}
}

function initAjaxSourcedError(form, elem, data) {
	removeFormErrorMessage(form);
	removeErrorMessage(elem);
	if (data.statusText = "error" && data.status != 0) {
		if (data.status == 403) {
			setFormErrorMessage(form, translations['forbidden']);
		} else {
			setFormErrorMessage(form, translations['ajaxErr'].replace("{0}",
					data.status));
		}
	} else {
		setFormErrorMessage(form, translations['ajaxHangPost']);
	}
	form.data('loading', -1);
	changeModalFormState(form, true, false, false);
}


function initAjaxSourced(form, elem, dataIn, val) {
	if (!val) {
		return;
	}
	var jsonUrl = elem.attr('ajax-data-init');
	
	var urlVal = val;
	if (Array.isArray(urlVal)) {
		urlVal = urlVal.join(',');
	}
	
	jsonUrl = jsonUrl.replace("{0}", encodeURIComponent(urlVal));
	
	if (typeof elem.attr('init-parms') !== 'undefined') {
		var jsonParms = elem.attr('ajax-parms').split(':');
		jsonParms.forEach(function(item, i, arr) {
			var name = $('#' + item).attr('name');
			jsonUrl = jsonUrl.replace("{" + (i + 1) + "}", encodeURIComponent(dataIn[name]));
		});
	}
	
	if (jsonUrl === elem.attr("loaded-from")) {
		return;
	}

	form.data('loading', form.data('loading') + 1);
	elem.html("").selectpicker("refresh");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : jsonUrl,
		dataType : "json",
		timeout : 10000,
		success : function(data) {
			initAjaxSourcedSuccess(form, elem, jsonUrl, val, data);
		},
		error : function(data) {
			initAjaxSourcedError(form, elem, data);
		}
	});
}

function beforeOpenModal(form, fetch, createForm, persistCreate) {
	removeFormErrorMessage(form);
	if (createForm) {
		form.find(".create-another-wrapper").removeClass("hidden");
	} else {
		form.find(".create-another-wrapper").addClass("hidden");		
	}
	form.data('uploading', 0);
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this);
		if (elem.prop("tagName") === 'SELECT' && elem.attr('force-refresh') == 'true') {
			elem.attr('loaded-from', '');
		}
		var val = elem.attr('default');
		
		if (!(persistCreate && elem.hasClass("create-another"))) {
			setInputValue(elem, val);
		}
		
		removeErrorMessage(elem);
	});
	
	if (fetch) {
		return;
	}
	
	changeModalFormState(form, false, false, false);
}

function addReloadLink(form, recId) {
	var reload = "<a href='#' onclick='beforeOpenModalFetch($(\"#"
			+ form.attr("id") + "\"), " + recId + ")'>"
			+ translations['reload'] + "</a>";
	setFormErrorMessage(form, reload);
}

function modalFormOpenSuccess(form, data, recId) {
	if (data.responseClass == "INFO") {
		form.data('loading', 0);

		form.find("input,select,textarea").not('[type="submit"]').each(function() {
			var elem = $(this);
			var name = elem.attr("name");
			
			if (typeof name === 'undefined') {
				return;
			}
			
			var val = data[name];
			
			if (typeof elem.attr("ajax-data-init") !== 'undefined') {
				if (initAjaxSourced(form, elem, data, val)) {
					return;
				}
			}
			
			if (typeof val === 'boolean') {
				val = val.toString();
			}
			var index = name.indexOf("['"); 
			if (index == -1) {
				setInputValue(elem, val);
			} else {
				var to = name.substring(index + 2, name.length - 2);
				setInputValue(elem, data[name.substring(0, index)][to]);
			}
		});

		if (form.data('loading') == 0) {
			showConditionalForm(form);
			changeModalFormState(form, false, false, false);
		}
		
		
		return;
	}

	changeModalFormState(form, true, false, false);
	removeFormErrorMessage(form);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
	}
	addReloadLink(form, recId);
}

function modalFormOpenError(form, data, recId) {
	changeModalFormState(form, true, false, false);

	removeFormErrorMessage(form);
	removeFormFieldErrorMessage(form);
	if (data.statusText = "error" && data.status != 0) {
		setFormErrorMessage(form, translations['ajaxErr'].replace("{0}",
				data.status));
	} else {
		setFormErrorMessage(form, translations['ajaxHangGet']);
	}
	addReloadLink(form, recId);
}

function beforeOpenModalFetch(form, recId, createForm) {
	beforeOpenModal(form, true, createForm);

	if (typeof recId === 'undefined') {
		changeModalFormState(form, false, false, false);
		return;
	}
	
	changeModalFormState(form, true, true, false);

	var jsonUrl = form.attr("fetchFrom").replace("{0}", encodeURIComponent(recId));

	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : jsonUrl,
		dataType : "json",
		timeout : 10000,
		success : function(data) {
			modalFormOpenSuccess(form, data, recId);
		},
		error : function(data) {
			modalFormOpenError(form, data, recId);
		}
	});
}

function afterOpenModal(form) {
	showConditionalForm(form);
	
	var focus = form.find('[autofocus="autofocus"]')[0];
	if (focus) {
		focus.focus();
	}
}

function beforeCloseModal(form) {
	if (form.attr("loading") == "true") {
		return false;
	}
	
	if (form.prop("cancel-refresh")) {
		form.prop("cancel-refresh", null);

		var refresh = form.attr("related-table");
		if (typeof refresh !== 'undefined') {
			$('#' + refresh).DataTable().ajax.reload(null, false);
		}
	}
	
	return true;
}

function changeModalFormState(form, submitDisabled, loader, keepOnScreen) {
	var submit = form.find('[type="submit"]');
	submit.prop("disabled", submitDisabled);
	form.find('[data-dismiss="modal"]').prop("disabled", keepOnScreen);

	if (loader) {
		if (!submit.parent().find(".loader").length) {
			submit.parent().prepend("<div class='loader'></div>");
		}
	} else {
		submit.parent().find(".loader").remove();
	}

	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this); 

		elem.prop("disabled", submitDisabled);
		if (elem.prop("tagName") === "INPUT" && elem.attr("type") == "file") {
			if (submitDisabled) {
				elem.fileinput('disable');
			} else {
				elem.fileinput('enable').fileinput('refresh').fileinput('reset');
			}
		} else if (elem.prop("tagName") === "SELECT") {
			elem.selectpicker('refresh');
		}
	});

	if (keepOnScreen) {
		form.attr("loading", true);
	} else {
		form.attr("loading", false);
	}
}

function getData(elem) {
	var tag = elem.prop("tagName");
	if (tag === 'INPUT' || tag === 'TEXTAREA') {
		var format = elem.attr('format');
		if (format === 'array') {
			return elem.val().split(',');
		} else if (format === 'file') {
			return elem.data('fileref');
		} else {
			return elem.val();			
		}
	}
	if (tag === 'SELECT') {
		return elem.selectpicker('val');
	}
}

function getFormData(form) {
	var res = {};

	form.find("input,select,textarea").not('[type="submit"]').not(".ignore-value,.file-caption-name").each(function() {
		var elem = $(this);
		var format = elem.attr('format');
		
		if (format == 'langObj') {
			var name = elem.attr('name');
			var index = name.indexOf("['"); 
			if (index >= 0) {
				var obj = name.substring(0, index);
				var to = name.substring(index + 2, name.length - 2);
				
				if (typeof res[obj] === 'undefined') {
					res[obj] = {};
				}
				
				res[obj][to] = elem.val();
			} else {
				res[elem.attr('name')] = elem.val();
			}
		} else {
			res[elem.attr('name')] = getData(elem);
		}
	});

	return res;
}

function showModalForm(form) {
	$('#modal_' + form.attr("id")).modal('toggle');
}

function closeModalForm(form) {
	$('#modal_' + form.attr("id")).modal('hide');
}

function modalFormSuccess(form, data) {
	changeModalFormState(form, false, false, false);

	if (data.responseClass == "INFO") {
		var createAnother = form.find("input.create-another").prop("checked");
		

		if (data.errors) {
			data.errors.forEach(function(item, i, arr) {
				$.notify({
					message: item
				}, notifySettings['success']);
			});
		}
		

		if (createAnother) {
			beforeOpenModal(form, false, true, true);
			form.prop("cancel-refresh", true);
		} else {
			closeModalForm(form);
			var refresh = form.attr("related-table");
			if (typeof refresh !== 'undefined') {
				$('#' + refresh).DataTable().ajax.reload(null, false);
			}
		}
		return;
	}

	removeFormErrorMessage(form);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
	}
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this);
		var name = elem.attr("name");
		
		if (typeof name === 'undefined' || elem.attr("type") === "hidden") {
			return;
		}
		removeErrorMessage(elem);
		
		var index = name.indexOf("['"); 
		if (index >= 0) {
			name = name.substring(0, index);
		}
		
		var err = data.colErrors[name];
		if (err) {
			err.forEach(function(item, i, arr) {
				setErrorMessage(elem, item);
			});
		}
	});
}

function modalFormError(form, data) {
	changeModalFormState(form, false, false, false);

	removeFormErrorMessage(form);
	removeFormFieldErrorMessage(form);
	
	if (data.statusText == "parsererror" && data.status == 200) {
		data.statusText = "error";
		data.status = 403;
	}
	
	if (data.statusText == "error" && data.status != 0) {		
		if (data.status == 403) {
			setFormErrorMessage(form, translations['forbidden']);
		} else {
			setFormErrorMessage(form, translations['ajaxErr'].replace("{0}",
					data.status));
		}
	} else {
		setFormErrorMessage(form, translations['ajaxHangPost']);
	}
}

function submitModalForm(form, event) {
	if (typeof event !== 'undefined') {
		event.preventDefault();		
	}
	
	if (!validateForm(form)) {
		return;
	}

	if (typeof form.data('uploading') === 'undefined') {
		form.data('uploading', 0);
	}
	
	form.find("input[type='file']").each(function() {
		var elem = $(this);
		if (elem.fileinput('getFileStack').length > 0) {
			form.data('uploading', form.data('uploading') + elem.fileinput('getFileStack').length);
			elem.fileinput('upload');
		}
	});

	if (form.data('uploading') > 0) {
		// wait for upload
		return;
	}
	
	var jsonString = JSON.stringify(getFormData(form));

	changeModalFormState(form, true, true, true);

	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");

	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : form.attr("action"),
		data : jsonString,
		dataType : "json",
		timeout : 10000,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfToken);
		},
		success : function(data) {
			modalFormSuccess(form, data);
		},
		error : function(data) {
			modalFormError(form, data);
		}
	});
}

function initModal(modal) {
	var item = $('#' + modal.attr("id").substring(6));

	modal.on('shown.bs.modal', function(e) {
		afterOpenModal($(item));
	});

	modal.on('hide.bs.modal', function(e) {
		beforeCloseModal($(item));
	});
}

function removeTableErrorMessage(dt) {
	var parent = ($(dt.table().node())).closest(".dataTables_wrapper");

	if (parent == null) {
		return;
	}
	parent.find(".errorHolder").html("");
}

function setTableErrorMessage(dt, message) {
	var parent = ($(dt.table().node())).closest(".dataTables_wrapper");

	if (parent == null) {
		return;
	}
	var errHolder = parent.find(".errorHolder");
	var errDiv = errHolder.find(".alert-danger");
	if (!errDiv.length) {
		errHolder.append('<div class="alert alert-danger">' + message
				+ '</div>');
	} else {
		errDiv.append('<br/>' + message);
	}
}


function dataTablesAjaxSuccess(dt, data) {
	if (data.responseClass == "INFO") {
		dt.ajax.reload(null, false);
		return;
	}
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setTableErrorMessage(dt, item);
		});
	}
}

function dataTablesAjaxError(dt, data) {
	if (data.statusText = "error" && data.status != 0) {
		if (data.status == 403) {
			setTableErrorMessage(dt, translations['forbidden']);
		} else {
			setTableErrorMessage(dt, translations['ajaxErr'].replace("{0}",
					data.status));
		}
	} else {
		setTableErrorMessage(dt, translations['ajaxHangPost']);
	}
}

function dataTablesSelected(dt, refCol, selector) {
	if (typeof refCol === 'undefined') {
		return undefined;
	}
	
	var refData = [];
	dt.rows({
		selected : true
	}).data().each(function(d) {
		refData.push(d[refCol]);
	});
	
	if (selector == 'selectedSingle') {
		return refData[0];
	}
	
	return refData;
}

function dataTablesAjax(dt, ajaxMethod, ajaxAddress, refCol, selector) {
	var refData = dataTablesSelected(dt, refCol, selector);
	
	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type : ajaxMethod,
		contentType : "application/json",
		url : ajaxAddress,
		data : JSON.stringify(refData),
		dataType : "json",
		timeout : 10000,
		beforeSend : function(xhr) {
			if (ajaxMethod !== 'GET') {
				xhr.setRequestHeader(csrfHeader, csrfToken);
			}
		},
		success : function(data) {
			dataTablesAjaxSuccess(dt, data);
		},
		error : function(data) {
			dataTablesAjaxError(dt, data);
		}
	});
}

function initDatatables(table, loc) {
	var buttonDiv = $('#' + table.attr('id') + '_buttons');
	var buttons = [];
	buttonDiv.find('button').each(function() {
		var button = $(this);

		var func = function(e, dt, node, config) {
			console.warn("Unknown action: " + button.attr("action"));
		}
		var action = button.attr("action").split(":");
		if (action.length > 1 && (action[0] == "form" || action[0] == "createform")) {
			var prefix = table.attr("prefix");
			var create = (action[0] == "createform");
			var formId = action[1];
			
			if (typeof prefix !== 'undefined') {
				formId = prefix + formId;
			}
			
			var form = $('#' + formId);
			var refCol = action[2];

			if (typeof form.attr("id") == 'undefined') {
				console.warn("Form not found: " + action[1]);
			}

			if (typeof form.attr("fetchFrom") !== 'undefined') {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					beforeOpenModalFetch(form, dataTablesSelected(dt, refCol, button.attr("extend")), create);
					showModalForm(form);
				}
			} else {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					beforeOpenModal(form, false, create);
					if (typeof refCol !== 'undefined') {
						var refData = dataTablesSelected(dt, refCol, button.attr("extend"));
						form.find('input[name="' + refCol + '"]').val(refData);
					}
					showModalForm(form);
				}
			}
		} else if (action.length > 1 && action[0] == "ajax") {
			var ajaxMethod = action[1];
			var ajaxAddress = action[2];
			var refCol = action[3];
			if (typeof refCol === 'undefined') {
				refCol = "id";
			}

			var confirmation = button.attr("confirmation");
			if (typeof confirmation !== 'undefined') {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					bootbox.confirm({
						animate : false,
						title : translations['confTitle'],
						message : confirmation,
						buttons : {
							confirm : {
								label : translations['modalOK'],
								className : 'btn btn-primary'
							},
							cancel : {
								label : translations['modalCancel'],
								className : 'btn btn-link'
							}
						},
						backdrop : true,
						closeButton : false,
						callback : function(result) {
							if (result) {
								dataTablesAjax(dt, ajaxMethod, ajaxAddress, refCol, button.attr("extend"));
							}
						}
					});
				}
			} 
		} else if (action.length > 1 && action[0] == "link") {
			var link = action[1];
			var ref = action.slice(2);

			func = function(e, dt, node, config) {
				var processedLink = link;
				ref.forEach(function(currentValue, index, array) {
					processedLink = processedLink.replace("{" + index + "}", dataTablesSelected(dt, currentValue, button.attr("extend")));
				});
				
				node.attr('href', processedLink);
				if (e.which == 2) {
					window.open(processedLink, '_blank')
				}
			}
		} else {
			func = function(e, dt, node, config) {
				removeTableErrorMessage(dt);
				dataTablesAjax(dt, ajaxMethod, ajaxAddress, button.attr("extend"));
			}
		}

		buttons.push({
			action : func,
			className : button.attr("class"),
			extend : button.attr("extend"),
			text : button.text()
		});
	});
	buttonDiv.remove();
	initDatatablesWithButtons(table, buttons, loc);
}

function displayDtImage(flag, url, row) {
	if (flag) {
		var val = url;
		
		var attr = val.split(":");
		val = attr[0];
		attr = attr.slice(1);
		attr.forEach(function(currentValue, index, array) {
			val = val.replace("{" + index + "}", row[currentValue]);
		});
		
		return "<img src='" + val + "' class='img-thumbnail'>"
	} else {
		return "";
	}
}

function initDatatablesWithButtons(table, buttons, loc) {
	var columnDefs = [];
	var expand = false
	table.find("th").each(function() {
		var cd = $(this);
		
		var format = cd.attr("format");
		if (format === "DATE" || format == "DATETIME") {
			columnDefs.push({
				data : cd.attr("mapping"),
				name : cd.attr("mapping"),
				render : function ( data, type, row, meta ) {
					if (type == "sort" || type == "type") {
				        return data;
					}
					return formatDate(parseDate(data), format, locale);			
			    },
				visible : cd.attr("td-visible") == 'true'
			});
		} else if (format === "IMAGE") {
			columnDefs.push({
				data : cd.attr("mapping"),
				name : cd.attr("mapping"),
				orderable : false,
				render : function ( data, type, row, meta ) {
					if (type == "sort" || type == "type") {
				        return data;
					}
					return displayDtImage(data, cd.attr("image-url"), row);
			    },
				visible : cd.attr("td-visible") == 'true'
			});
		} else if (format === "expand") {
			expand = true;
			columnDefs.push({
                className:      'details-control',
                orderable:      false,
                data:           null,
                defaultContent: '',
                render: function () {
                	return '<span class="glyphicon glyphicon-chevron-right"></span>';
                },
				visible :       true,
                width:          '16px'
            });
		} else {
			columnDefs.push({
				data : cd.attr("mapping"),
				name : cd.attr("mapping"),
				visible : cd.attr("td-visible") == 'true'
			});
		}
	});

	var index = 0;
	var sort = {};
	table.find("th").each(function() {
		var cd = $(this);
		var idt = cd.attr("mapping") + ":name";
		if (typeof cd.attr("sort") !== 'undefined' && typeof cd.attr("sort-index") !== 'undefined') {
			sort[cd.attr("sort-index")] = [index, cd.attr("sort")];
		}
		index++;
	});
	
	var order = [];
	for (var property in sort) {
	    if (sort.hasOwnProperty(property)) {
	        order.push(sort[property]);
	    }
	}

	var dataTable = {};
	dataTable = table.DataTable({
		ajaxSource : table.attr("ajaxSource"),
		buttons : {
			dom : {
				container : {
					tag : 'div',
					className : 'button-item'
				}
			},
			buttons : buttons
		},
		columns : columnDefs,
		dom : "<'row'<'col-sm-12 errorHolder'>>"
				+ "<'row'<'col-sm-6'l><'col-sm-6'f>>"
				+ "<'row'<'col-sm-12'B>>"
				+ "<'row'<'col-sm-12 table-responsive'tr>>"
				+ "<'row'<'col-sm-5'i><'col-sm-7'p>>",
		fnDrawCallback: function( oSettings ) {
			for (var property in dataTable.expanded) {
			    if (dataTable.expanded.hasOwnProperty(property)) {
			    	table.find('tr#' + property).find('td.details-control').click();
			    }
			}
		},
		initComplete: function(settings, json) {
			var api = new $.fn.dataTable.Api( settings );
			api.buttons(".dt-link").each(function(button, index) {
				var elem = $(button.node);
				var btn = api.button(button.node);
				var action = btn.action();
				btn.action(function(e, dt, node, config) {
					window.location = node.attr('href');
				});
				
				elem.on('mousedown', function(e) {
					action(e, api, elem, button.c);
				});
			});
		},
		language : {
			url : loc
		},
		order : order,
		rowId : 'id',
		select : true
	});
	
	if (expand) {
		var frame = table.attr("expand-frame");
		
		dataTable['formatExpand'] = function(data) {
			var frameAction = frame.split(":");
			var link = frameAction[0];
			var ref = frameAction.slice(1);
			var reference = "";
			
			ref.forEach(function(currentValue, index, array) {
				link = link.replace("{" + index + "}", encodeURIComponent(data[currentValue]));
				reference += "_" + data[currentValue];
			});
			
			var prefix = reference.replace("_", table.attr('id'));
			var processedLink;

			if (link.indexOf('?') == -1) {
				processedLink = link + "?referrer=" + encodeURIComponent(prefix);
			} else {
				processedLink = link + "&referrer=" + encodeURIComponent(prefix);
			}
			
			var divId = "expand" + processedLink.replace(/[\/\?\=]/g, '_');
			
			var expandData, expandRef;
			var expandTemplate = $('#' + table.attr("id") + "_extension");
			if (expandTemplate.length) {
				expandTemplate.find("td.dynamic").each(function() {
					var elem = $(this);
					var mapping = elem.attr("mapping").split(".");
					var val = data;
					
					mapping.forEach(function(item, i, arr) {
						val = val[item];
					});
					
					var format = elem.attr("format");
					if (format === "DATE" || format === "DATETIME") {
						val = formatDate(parseDate(val), format, locale);
					} else if (format === "IMAGE") {
						val = displayDtImage(val, elem.attr("image-url"), data);
					}
					elem.html(val);
				});
				var detailsHref = table.attr("id");
				expandData = expandTemplate.html();
				expandRef = prefix + "_details_";
			}
			
			if (link.length > 0) {
				$.ajax({
					type : "GET",
					contentType : "text/html",
					url : processedLink,
					timeout : 10000,
					success : function(data) {
						var div = $('#' + divId);
						div.html(data);
						
						if (typeof expandData !== 'undefined') {
							div.find("ul.nav.nav-tabs li").first().before("<li><a data-toggle='tab' href='#" 
									+ expandRef
									+ "'>"
									+ translations['extnTabName']
									+ "</a></li>");
							div.find("div.tab-content div.tab-pane").first().before("<div id='"
									+ expandRef
									+ "' class='tab-pane'>"
									+ expandData
									+ "</div>"
									);
						}
						
						div.attr('id', null);	
						initHtml(div);					
						div.find(".div-modal").detach().appendTo("div#modalContainer");
						var tab = div.find("ul.nav.nav-tabs li a").first().tab('show');
					},
					error : function(data) {
						var err;
						if (data.statusText = "error" && data.status != 0) {
							if (data.status == 403) {
								err = translations['forbidden'];
							} else {
								err = translations['ajaxErr'].replace("{0}", data.status);
							}
						} else {
							err = translations['ajaxHangPost'];
						}
						var div = $('#' + divId);
						div.html('<div class="alert alert-danger">' + err + '</div>');
						div.attr('id', null);						
					}
				});
				return "<div class='container container-full container-main' id='" + divId + "' prefix='" + prefix + "'></div>";
			} else {
				return "<div class='container container-full container-main'>" 
				    + "<ul class='nav nav-tabs'><li class='active'><a data-toggle='tab' href='#" 
					+ expandRef
					+ "'>"
					+ translations['extnTabName']
					+ "</a></li></ul>"
					+ "<div class='tab-content'><div id='"
					+ expandRef
					+ "' class='tab-pane active'>"
					+ expandData
					+ "</div></div></div>";
			}
		}
			
		dataTable['expanded'] = {};
		
	    $('#' + table.attr("id") + ' tbody').on('click', 'td.details-control', function () {
	        var tr = $(this).closest('tr');
	        var tdi = tr.find("span.glyphicon");
	        var row = dataTable.row(tr);
	
	        if (row.child.isShown()) {
	            // This row is already open - close it
	        	var prefix = row.child().find("div.container-main").attr("prefix");
	        	if (typeof prefix !== 'undefined') {
	        		$("div#modalContainer").find("#" + prefix + "modalForms").remove();
	        	}
	            row.child.hide();
	            tr.removeClass('shown');
	            tdi.first().removeClass('glyphicon-chevron-down');
	            tdi.first().addClass('glyphicon-chevron-right');
	            delete dataTable.expanded[row.id()];
	        } else {
	            // Open this row
	            row.child(dataTable.formatExpand(row.data())).show();
	            tr.addClass('shown');
	            tdi.first().removeClass('glyphicon-chevron-right');
	            tdi.first().addClass('glyphicon-chevron-down');
	            dataTable.expanded[row.id()] = true;
	        }
	    });
	
	    dataTable.on("user-select", function (e, dt, type, cell, originalEvent) {
	        if ($(cell.node()).hasClass("details-control")) {
	            e.preventDefault();
	        }
	    });
	}
}

function initDatePicker(elem, minView) {
	elem.datetimepicker({
		language : locale,
		autoclose: true,
		container: elem.attr('data-date-container'),
		todayHighlight: true,
		startView: 'month',
		minView: minView,
		forceParse: true
	});
}

function initFileInput(elem, initial, destroy) {
	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var headers = {};
	headers[csrfHeader] = csrfToken;
		
	if (destroy) {
		elem.fileinput('destroy');
	}
	elem.fileinput({
		ajaxSettings : {
			headers : headers
		},
		ajaxDeleteSettings : {
			headers : headers
		},
		container : elem.closest('.form-group'),
		elErrorContainer : '#err_' + elem.attr('id'),
		errorCloseButton : '',
		initialPreview : initial,
		initialPreviewAsData : true,
		language : locale,
		layoutTemplates : {
			main1 : '<label class="control-label" for="'
					+ elem.attr('id') + '">' + elem.attr('placeholder')
					+ '</label>' + '{preview}\n'
					+ '<div class="input-group {class}">\n'
					+ '  {caption}\n'
					+ '  <div class="input-group-btn">\n'
					+ '    {remove}\n' + '    {browse}\n'
					+ '  </div>\n' + '</div>\n' + '<div id="err_'
					+ elem.attr('id') + '"></div>\n',
			actions : '<div class="file-actions">\n'
					+ '    <div class="file-footer-buttons">\n'
					+ '        {download} {zoom} {other}' + '    </div>\n'
					+ '    <div class="clearfix"></div>\n' + '</div>'
		},
		msgErrorClass : "help-block file-help-block",
		showClose : false,
		uploadUrl: elem.attr("upload-to")
	});
	
	elem.data('fileref', '');
	
	elem.on('fileuploaded', function(event, data, previewId, index) {
		elem.data('fileref', data.response.idt);
		elem.fileinput('updateStack', index, undefined);
		var form = elem.closest("form");
		if (typeof form.data('uploading') !== 'undefined') {
			form.data('uploading', form.data('uploading') - 1);
			if (form.data('uploading') == 0) {
				submitModalForm(form);
			}
		}
	});

	elem.on('filecleared', function(event, id, index) {
	    elem.closest('.file-input').find('.file-preview-thumbnails').find('.file-preview-frame').each(function() {
	    	$(this).remove();
	    });
		elem.data('fileref', 'clear');
	}); 
	
	elem.on('fileloaded', function(event, file, previewId, index, reader) {
	    elem.closest('.file-input').find('.file-preview-thumbnails').find('.file-preview-frame').not('[id=' + previewId + ']').each(function() {
	    	$(this).remove();
	    });
	});
}