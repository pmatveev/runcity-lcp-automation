var dtLocalizationLinkConst;

$(function(){
	  var hash = window.location.hash;
	  hash && $('ul.nav a[href="' + hash + '"]').tab('show');

	  $('.nav-tabs a').click(function (e) {
	    $(this).tab('show');
	    var scrollmem = $('body').scrollTop() || $('html').scrollTop();
	    window.location.hash = this.hash;
	    $('html,body').scrollTop(scrollmem);
	  });
	});

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
	container.find(".nav-tabs a").click(function(e) {
		var focus = $($(this).attr("href")).find('[autofocus="autofocus"]')[0];
		if (focus) {
			focus.focus();
		}
	});
}

function showConfirmation(confirmation, callback) {
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
		callback : callback
	});
}

function getHttpError(statusText, status, type) {
	var result;
	
	if (statusText == "parsererror" && status == 200) {
		result = httpError[403];
	} else if (statusText == "error") {
		result = httpError[status];
	} else  {
		result = httpError["hang" + type];
	}
	
	if (typeof result === 'undefined') {
		result = httpError["default"];
	}
	
	return result;
}

function emptyValue(val) {
	return typeof val == 'undefined' || val == null || val == '' || val.length == 0;
}

function removeFormMessage(form) {
	var holder = form.attr("error-holder");
	
	if (typeof holder !== 'undefined') {
		$("#" + holder).html("");
	} else {	
		form.find(".errorHolder").html("");
	}
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
	switch (format) {
	case "DATE":
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableDateFormat'], 'standard'), locale, 'standard');
	case "DATETIME":	
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableDateTimeFormat'], 'standard'), locale, 'standard');
	case "DATETIMESTAMP":	
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableDateTimeStampFormat'], 'standard'), locale, 'standard');
	case "TIMESTAMP":	
		return dpg.formatDate(dateUTC, dpg.parseFormat(translations['tableTimeStampFormat'], 'standard'), locale, 'standard');
	}
}

function removeFormFieldErrorMessage(form) {
	form.find("input,textarea").not('[type="submit"]').each(function() {
		removeMessage($(this));
	});
}

function setFormMessage(form, message, cls) {
	var holder = form.attr("error-holder");
	var errHolder;
	if (typeof holder !== 'undefined') {
		errHolder = $("#" + holder);
	} else {
		errHolder = form.find(".errorHolder");
	}
	var errDiv = errHolder.find(".alert-" + cls);
	if (!errDiv.length) {
		errHolder.append('<div class="alert alert-' + cls + '">' + message + '</div>');
	} else {
		errDiv.append('<br/>' + message);
	}
}

function removeMessage(element) {
	var parent = element.closest(".form-group");

	if (parent == null) {
		return;
	}
	parent.removeClass("has-error");
	parent.removeClass("has-warning");
	var err = parent.find(".help-block");
	if (!err.hasClass("file-help-block")) {
		err.remove();
	}
}

function setMessage(element, message, cls) {
	var parent = element.closest(".form-group");

	if (parent == null) {
		return;
	}
	
	var clazz = "has-" + cls;
	if (cls == "danger") {
		clazz = "has-error";
	} 
	var append = parent.hasClass(clazz);
	parent.removeClass("has-error");
	parent.removeClass("has-warning");
	parent.removeClass("has-success");
	parent.addClass(clazz);
	var help = parent.find(".help-block");
	if (!help.length) {
		parent.append('<span class="help-block">' + message + '</span>');
	} else {
		if (append) {
			help.append('<br/>' + message);
		} else {
			help.html(message);			
		}
	}
}

function setMessageStrict(element, message, cls) {
	var parent = element.parent();
	var clazz = "has-" + cls;
	if (cls == "danger") {
		clazz = "has-error";
	} 
	parent.removeClass("has-error");
	parent.removeClass("has-warning");
	parent.removeClass("has-success");
	parent.addClass(clazz);
	var help = parent.find(".help-block");
	if (!help.length) {
		parent.append('<span class="help-block">' + message + '</span>');
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
			setMessage(elem, translations['required'], "danger");
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
			setMessage(elem, translations['oneRequired'], "danger");
			return false;
		}
		
		if (rule == "allrequired" && filled < total) {
			setMessage(elem, translations['allRequired'], "danger");
			return false;			
		}
	}

	if (rule.indexOf("min=") == 0) {
		var len = Number(rule.substring(4));
		if (getData(elem).length > 0 && getData(elem).length < len) {
			setMessage(elem, translations['minLen'].replace("{0}", len), "danger");
			return false;
		}
	}

	if (rule.indexOf("max=") == 0) {
		var len = Number(rule.substring(4));
		if (getData(elem).length > len) {
			setMessage(elem, translations['maxLen'].replace("{0}", len), "danger");
			return false;
		}
	}

	if (rule.indexOf("minval=") == 0) {
		var val = Number(rule.substring(4));
		if (getData(elem).length > 0 && Number(getData(elem)) < val) {
			setMessage(elem, translations['min'].replace("{0}", len), "danger");
			return false;
		}
	}

	if (rule.indexOf("maxval=") == 0) {
		var val = Number(rule.substring(4));
		if (getData(elem).length > 0 && Number(getData(elem)) > val) {
			setMessage(elem, translations['max'].replace("{0}", len), "danger");
			return false;
		}
	}

	if (rule == "pwd" && !emptyValue(getData(elem))) {
		if (!/^(?=.*[A-Za-z])(?=.*\d).{8,}$/.test(getData(elem))) {
			setMessage(elem, translations['passwordStrength'], "danger");
			return false;
		}
	}

	if (rule == "email" && !emptyValue(getData(elem))) { 
		if (!/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/
				.test(getData(elem))) {
			setMessage(elem, translations['invalidEmail'], "danger");
			return false;
		}
	}

	return true;
}

function checkInput(elem) {
	removeMessage(elem);
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
		setMessageStrict(pwd2, translations['passwordMatch'], "danger");
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
	removeMessage(elem);
	if (data.responseClass == "INFO" || data.responseClass == "WARNING") { 
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
		
		if (data.responseClass == "WARNING" && data.msg) {
			data.msg.forEach(function(item, i, arr) {
				setMessage(elem, item, "warning");
			});		
		}
		
		return;
	}

	elem.attr("loaded-from", null);
	if (data.msg) {
		data.msg.forEach(function(item, i, arr) {
			setMessage(elem, item, "danger");
		});
	}
}

function loadAjaxSourcedError(form, elem, data) {
	elem.attr("loaded-from", null);
	removeFormMessage(form);
	removeMessage(elem);
	setFormMessage(form, getHttpError(data.statusText, data.status, "GET"), "danger");
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
	removeMessage(elem);
	if (data.responseClass == "INFO" || data.responseClass == "WARNING") { 
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
		
		if (data.responseClass == "WARNING" && data.msg) {
			data.msg.forEach(function(item, i, arr) {
				setMessage(elem, item, "warning");
			});		
		}
		
		form.data('loading', form.data('loading') - 1);
		if (form.data('loading') == 0) {
			showConditionalForm(form);
			changeModalFormState(form, false, false, false);			
		}
		return;
	}

	if (data.msg) {
		data.msg.forEach(function(item, i, arr) {
			setMessage(elem, item, "danger");
		});
		form.data('loading', -1);
		changeModalFormState(form, true, false, false);
	}
}

function initAjaxSourcedError(form, elem, data) {
	removeFormMessage(form);
	removeMessage(elem);
	setFormMessage(form, getHttpError(data.statusText, data.status, "GET"), "danger");
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

function setDefaults(form, persistCreate) {
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this);
		if (elem.hasClass("preserve-value")) {
			return;
		}
		if (elem.prop("tagName") === 'SELECT' && elem.attr('force-refresh') == 'true') {
			elem.attr('loaded-from', '');
		}
		
		var val = elem.attr('default');
		
		if (!(persistCreate && elem.hasClass("create-another"))) {
			setInputValue(elem, val);
		}
		
		removeMessage(elem);
	});
}

function beforeOpenModal(form, fetch, createForm, persistCreate) {
	removeFormMessage(form);
	if (createForm) {
		form.find(".create-another-wrapper").removeClass("hidden");
	} else {
		form.find(".create-another-wrapper").addClass("hidden");		
	}
	form.data('uploading', 0);
	setDefaults(form, persistCreate);
	
	if (fetch) {
		return;
	}
	
	changeModalFormState(form, false, false, false);
}

function addReloadLink(form, recId) {
	var reload = "<a href='#' onclick='beforeOpenModalFetch($(\"#"
			+ form.attr("id") + "\"), " + recId + ")'>"
			+ translations['reload'] + "</a>";
	setFormMessage(form, reload, "danger");
}

function modalFormOpenSuccess(form, data, recId) {
	if (data.responseClass == "INFO" || data.responseClass == "WARNING") { 
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

		if (data.responseClass == "WARNING" && data.msg) {
			removeFormMessage(form);
			data.msg.forEach(function(item, i, arr) {
				setFormMessage(form, item, "warning");
			});		
		}
		
		if (form.data('loading') == 0) {
			showConditionalForm(form);
			changeModalFormState(form, false, false, false);
		}
		
		return;
	}

	changeModalFormState(form, true, false, false);
	removeFormMessage(form);
	if (data.msg) {
		data.msg.forEach(function(item, i, arr) {
			setFormMessage(form, item, "danger");
		});
	}
	addReloadLink(form, recId);
}

function modalFormOpenError(form, data, recId) {
	changeModalFormState(form, true, false, false);

	removeFormMessage(form);
	removeFormFieldErrorMessage(form);
	setFormMessage(form, getHttpError(data.statusText, data.status, "GET"), "danger");

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

function autofocus(form) {
	var focus = form.find('[autofocus="autofocus"]')[0];
	if (focus) {
		focus.focus();
	}
}

function scroll(form) {
	var scrollTo = form.attr("scroll");
	if (typeof scrollTo !== 'undefined') {
		$(document).scrollTop($(scrollTo).offset().top - parseInt($('.nav-buffer').css('margin-top'), 10));
	} 
}

function afterOpenModal(form) {
	showConditionalForm(form);
	autofocus(form);
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
		if (!submit.parent().find(".loader").length && !form.hasClass("form-inline")) {
			if (submit.hasClass("loader-right")) {
				submit.parent().append("<div class='loader'></div>");
			} else {
				submit.parent().prepend("<div class='loader'></div>");
			}
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

function getFormData(form, addData) {
	var res = {};
	if (typeof addData != 'undefined') {
		res = addData;
	} 

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
	var modal = $('#modal_' + form.attr("id"));
	
	if (modal.hasClass("modal")) {
		$('#modal_' + form.attr("id")).modal('hide');
		return true;
	} else {
		return false;
	}
}

function modalFormSuccess(form, data) {
	changeModalFormState(form, false, false, false);

	if (data.responseClass == "INFO" || data.responseClass == "WARNING") { 
		var createAnother = form.find("input.create-another").prop("checked");
		
		if (data.responseClass == "INFO") {
			if (data.msg) {
				data.msg.forEach(function(item, i, arr) {
					$.notify({
						message: item
					}, notifySettings['success']);
				});
			}
		} else {
			if (data.msg) {
				data.msg.forEach(function(item, i, arr) {
					$.notify({
						message: item
					}, notifySettings['warning']);
				});
			}			
		}
		

		if (createAnother) {
			beforeOpenModal(form, false, true, true);
			form.prop("cancel-refresh", true);
		} else {
			var refresh = form.attr("related-table");
			if (typeof refresh !== 'undefined') {
				$('#' + refresh).DataTable().ajax.reload(null, false);
			}

			if (!closeModalForm(form)) {
				removeFormMessage(form);
				setDefaults(form, false);
				autofocus(form);
			}
		}
		return;
	}

	removeFormMessage(form);
	
	var printErrors = function() {
		if (data.msg) {
			data.msg.forEach(function(item, i, arr) {
				setFormMessage(form, item, "danger");
			});
		}
		form.find("input,select,textarea").not('[type="submit"]').each(function() {
			var elem = $(this);
			var name = elem.attr("name");
			
			if (typeof name === 'undefined' || elem.attr("type") === "hidden") {
				return;
			}
			removeMessage(elem);
			
			var index = name.indexOf("['"); 
			if (index >= 0) {
				name = name.substring(0, index);
			}
			
			var err = data.colErrors[name];
			if (err) {
				err.forEach(function(item, i, arr) {
					setMessage(elem, item, "danger");
				});
			}
		});
		autofocus(form);
		scroll(form);
	}
	
	if (data.responseClass == "CONFIRMATION") {
		var confirmation = "";

		if (data.msg) {
			data.msg.forEach(function(item, i, arr) {
				confirmation += item + "<br/>";
			});
		}
		
		form.closest(".modal").hide();
		
		showConfirmation(confirmation, function(result) {
			form.closest(".modal").show();
			if (result) {
				submitModalForm(form, null, { confirmationToken : data.confirmationToken })
			} else {
				printErrors();
			}
		});
		return;
	}
	
	printErrors();
}

function modalFormError(form, data) {
	changeModalFormState(form, false, false, false);

	removeFormMessage(form);
	removeFormFieldErrorMessage(form);
	setFormMessage(form, getHttpError(data.statusText, data.status, "POST"), "danger");

	autofocus(form);
	scroll(form);
}

function submitModalForm(form, event, addData) {
	if (event) {
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
	
	var data = getFormData(form, addData);
	var type = "POST";
	if (typeof data._method !== 'undefined') {
		type = data._method;
		delete data._method;
	}
	
	var jsonString = JSON.stringify(data);

	changeModalFormState(form, true, true, true);

	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	if (typeof data._csrf !== 'undefined') {
		csrfToken = data._csrf;
		delete data._csrf;
	} 

	$.ajax({
		type : type,
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

function removeTableMessage(dt) {
	var parent = ($(dt.table().node())).closest(".dataTables_wrapper");

	if (parent == null) {
		return;
	}
	parent.find(".errorHolder").html("");
}

function setTableMessage(dt, message, cls) {
	var parent = ($(dt.table().node())).closest(".dataTables_wrapper");

	if (parent == null) {
		return;
	}
	var errHolder = parent.find(".errorHolder");
	var errDiv = errHolder.find(".alert-" + cls);
	if (!errDiv.length) {
		errHolder.append('<div class="alert alert-' + cls + '">' + message
				+ '</div>');
	} else {
		errDiv.append('<br/>' + message);
	}
}


function dataTablesAjaxSuccess(dt, data) {
	removeTableMessage(dt);
	if (data.responseClass == "INFO" || data.responseClass == "WARNING") { 
		if (data.responseClass == "WARNING" && data.msg) {
			data.msg.forEach(function(item, i, arr) {
				setTableMessage(dt, item, "warning");
			});		
		}
		dt.ajax.reload(null, false);
		return;
	}
	if (data.msg) {
		data.msg.forEach(function(item, i, arr) {
			setTableMessage(dt, item, "danger");
		});
	}
}

function dataTablesAjaxError(dt, data) {
	setTableMessage(dt, getHttpError(data.statusText, data.status, "GET"), "danger");
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
					beforeOpenModalFetch(form, dataTablesSelected(dt, refCol, button.attr("extend")), create);
					showModalForm(form);
				}
			} else {
				func = function(e, dt, node, config) {
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
					removeTableMessage(dt);
					showConfirmation(confirmation, function(result) {
						if (result) {
							dataTablesAjax(dt, ajaxMethod, ajaxAddress, refCol, button.attr("extend"));
						}
					});
				}
			} else {
				func = function(e, dt, node, config) {
					removeTableMessage(dt);
					dataTablesAjax(dt, ajaxMethod, ajaxAddress, refCol, button.attr("extend"));
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
		} else if (action.length > 0 && action[0] == "refresh") {
			func = function(e, dt, node, config) {
				dt.ajax.reload(null, false);
			}
		}

		var init;
		var check = button.attr("jscheck");
		if (typeof check !== 'undefined') {
			init = function(dt, node, config) {
				var that = this;
				that.disable();
				dt.on('select.dt.DT deselect.dt.DT', function() {
					var data = dt.rows({ selected : true }).data();
					
					if (data.length == 0) {
						that.disable();
						return;
					}
					
					var ok = true;
					for (var i = 0; i < data.length; i++) {
						var row = data[i];
						
						if (!eval(check)) {
							ok = false;
							break;
						}
					}
					
					that.enable(ok);
				});
			}
		}
		
		buttons.push({
			action : func,
			className : button.attr("class"),
			extend : button.attr("extend"),
			init: init,
			text : button.html()
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
		if (format === "DATE" || format == "DATETIME" || format == "DATETIMESTAMP" || format == "TIMESTAMP") {
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
		ajax : { 
			url : table.attr("ajaxSource"),
			dataSrc : function(json) {
				removeTableMessage(dataTable);
				if (json.responseClass == "ERROR" && json.msg) {
					json.msg.forEach(function(item, i, arr) {
						setTableMessage(dataTable, item, "danger");
					});		
				}
				if (json.responseClass == "WARNING" && json.msg) {
					json.msg.forEach(function(item, i, arr) {
						setTableMessage(dataTable, item, "warning");
					});		
				}
				return json.data;
			}
		},
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
			processedLink += "&table=" + encodeURIComponent(table.attr('id'));
			
			var divId = "expand" + processedLink.replace(/[\/\?\&\=]/g, '_');
			
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
					if (format === "DATE" || format == "DATETIME" || format == "DATETIMESTAMP" || format == "TIMESTAMP") {
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
						var err = getHttpError(data.statusText, data.status, "GET");
						var div = $('#' + divId);
						div.html('<div class="alert alert-danger">' + err + '</div>');
						div.attr('id', null);						
					}
				});
				return "<div class='container-main container-sub' id='" + divId + "' prefix='" + prefix + "'></div>";
			} else {
				return "<div class='container-main container-sub'>" 
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
	    	var elem = $(this);
	    	if (table.attr("id") != elem.closest("table").attr("id")) {
	    		return;
	    	}
	    	
	        var tr = elem.closest("tr");
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

function processToggleAjax(input, ajaxData, successHandler) {
	if (input.prop('ajax-communication')) {
		return;
	}
	removeMessage(input);
	input.prop('ajax-communication', true);
	input.prop('disabled', true);
	input.parent().addClass("disabled");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	
	var rollback = function() {
		input.prop('disabled', false);
		input.parent().removeClass("disabled");
		input.prop('checked', !input.prop('checked')).change();
		input.prop('ajax-communication', false);
	}
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : input.attr("ajax-target"),
		data : JSON.stringify(ajaxData),
		dataType : "json",
		timeout : 10000,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfToken);
		},
		success : function(data) {
			if (successHandler(data)) {
				return;
			}
			rollback();
			if (data.msg) {
				data.msg.forEach(function(item, i, arr) {
					setMessage(input, item, "danger");
				});
			}
		},
		error : function(data) {
			rollback();
			setMessage(input, getHttpError(data.statusText, data.status, "POST"), "danger");
		}
	});
}

function refreshPageData(elem) {
	if (!elem.parent().hasClass('ajax-refresh-holder')) {
		elem.wrap('<div class="ajax-refresh-holder"></div>');
	}
	
	var jsonUrl = elem.attr('ajax-target');
	if (typeof jsonUrl === 'undefined') {
		return;
	}
	
	elem.prop('disabled', true);

	if (elem.hasClass("loader-right")) {
		elem.parent().append("<div class='loader'></div>");
	} else {
		elem.parent().prepend("<div class='loader'></div>");
	}
	
	var error = function(data) {
		$('[refreshed-by=' + elem.attr('id') + ']').each(function() {
			$(this).html(translations['refreshEmpty']);
		});
		elem.parent().find('.loader').remove();
		elem.prop('disabled', false);
	};
	
	$.ajax({
		type : 'GET',
		contentType : 'application/json',
		url : jsonUrl,
		dataType : 'json',
		timeout : 10000,
		success : function(data) {
			if (data.responseClass == 'INFO') {
				$('[refreshed-by=' + elem.attr('id') + ']').each(function() {
					var that = $(this);
					var val = data.data[that.attr('refresh-key')];
					if (typeof val !== 'undefined') {
						$(this).html(val);
					} else {
						$(this).html(translations['refreshEmpty']);
					}
				});
				elem.prop('disabled', false);
			} else {
				error();
			}
			elem.parent().find('.loader').remove();
		},
		error : error
	});
}