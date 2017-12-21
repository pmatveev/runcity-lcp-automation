function removeFormErrorMessage(form) {
	form.find(".errorHolder").html("");
}

function parseDate(datestr) {
	var dpg = $.fn.datetimepicker.DPGlobal;
	return dpg.parseDate(datestr, dpg.parseFormat('yyyy mm dd', 'standard'));
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
	parent.find(".help-block").remove();
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

function checkElem(elem, rule) {
	if (rule == "required") {
		if (elem.val().length == 0) {
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
			if ($(this).val().length > 0) {
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
		if (elem.val().length > 0 && elem.val().length < len) {
			setErrorMessage(elem, translations['minLen'].replace("{0}", len));
			return false;
		}
	}

	if (rule.indexOf("max=") == 0) {
		var len = Number(rule.substring(4));
		if (elem.val().length > len) {
			setErrorMessage(elem, translations['maxLen'].replace("{0}", len));
			return false;
		}
	}

	if (rule == "pwd") {
		if (!/^(?=.*[A-Za-z])(?=.*\d).{8,}$/.test(elem.val())) {
			setErrorMessage(elem, translations['passwordStrength']);
			return false;
		}
	}

	if (rule == "email") { // 
		if (!/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/
				.test(elem.val())) {
			setErrorMessage(elem, translations['invalidEmail']);
			return false;
		}
	}

	return true;
}

function checkInput(elem) {
	var checkList = elem.attr("jschecks");
	if (typeof checkList === 'undefined') {
		return true;
	}
	checkList = checkList.split(";");
	var result = true;

	removeErrorMessage(elem);
	checkList.forEach(function(item, i, arr) {
		if (!checkElem(elem, item)) {
			result = false;
		}
	});

	return result;
}

function onColChange(elem) {
	checkInput(elem);
	var form = elem.closest("form");
	
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var inp = $(this);
		if ((":" + inp.attr('ajax-parms') + ":").indexOf(":" + elem.attr("id") + ":") > -1) {
			setInputValue(inp, "");
		}
	});
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

	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		if (!checkInput($(this))) {
			result = false;
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
			
			if (dt === 'colorpicker') {
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

	removeFormErrorMessage(form);
	removeErrorMessage(elem);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
	}
}

function loadAjaxSourcedError(form, elem, data) {
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
		return;
	}
	
	var val = elem.selectpicker('val');
	elem.html("").selectpicker("refresh");
	
	if (!proceed) {
		return;
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
}

function initAjaxSourcedSuccess(form, elem, jsonUrl, val, data) {
	if (data.responseClass == "INFO") {
		optionsHtml = "";
		
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
		
		form['loading']--;
		if (form['loading'] == 0) {
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
		form['loading'] = -1;
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
	form['loading'] = -1;
	changeModalFormState(form, true, false, false);
}


function initAjaxSourced(form, elem, dataIn, val) {
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

	form['loading']++;
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

function beforeOpenModal(form, fetch) {
	removeFormErrorMessage(form);
	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this);
		var val = elem.attr('default');
		setInputValue(elem, val);

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
		form['loading'] = 0;

		form.find("input,select,textarea").not('[type="submit"]').each(function() {
			var elem = $(this);
			var name = elem.attr("name");
			
			if (typeof name === 'undefined') {
				return;
			}
			
			var val = data[name];
			
			if (typeof elem.attr("ajax-data-init") !== 'undefined') {
				initAjaxSourced(form, elem, data, val);
				return;
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

		if (form['loading'] == 0) {
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

function beforeOpenModalFetch(form, recId) {
	beforeOpenModal(form, true);

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
	var focus = form.find('[autofocus="autofocus"]')[0];
	if (focus) {
		focus.focus();
	}
}

function beforeCloseModal(form) {
	if (form.attr("loading") == "true") {
		return false;
	}
	return true;
}

function changeModalFormState(form, submitDisabled, loader, keepOnScreen) {
	var submit = form.find('[type="submit"]');
	submit.prop("disabled", submitDisabled);
	form.find('[data-dismiss="modal"]').prop("disabled", keepOnScreen);

	if (loader) {
		if (!submit.parent().find(".loader").length) {
			submit.parent().html(
					"<div class='loader'></div>" + submit.parent().html());
		}
	} else {
		submit.parent().find(".loader").remove();
	}

	form.find("input,select,textarea").not('[type="submit"]').each(function() {
		var elem = $(this); 
		elem.prop("disabled", submitDisabled);
		if (elem.prop("tagName") === "SELECT") {
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

	form.find("input,select,textarea").not('[type="submit"]').not(".ignore-value").each(function() {
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
		closeModalForm(form);

		var refresh = form.attr("related-table");
		if (typeof refresh !== 'undefined') {
			$('#' + refresh).DataTable().ajax.reload(null, false);
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

function submitModalForm(form, event) {
	event.preventDefault();
	if (!validateForm(form)) {
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

function initDatatables(table, loc, lang) {
	var buttonDiv = $('#' + table.attr('id') + '_buttons');
	var buttons = [];
	buttonDiv.find('button').each(function() {
		var button = $(this);

		var func = function(e, dt, node, config) {
			console.warn("Unknown action: " + button.attr("action"));
		}
		var action = button.attr("action").split(":");
		if (action.length > 1 && action[0] == "form") {
			var form = $('#' + action[1]);
			var refCol = action[2];

			if (typeof form.attr("id") == 'undefined') {
				console.warn("Form not found: " + action[1]);
			}

			if (typeof form.attr("fetchFrom") !== 'undefined') {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					beforeOpenModalFetch(form, dataTablesSelected(dt, refCol, button.attr("extend")));
					showModalForm(form);
				}
			} else {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					beforeOpenModal(form, false);
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
			} else {
				func = function(e, dt, node, config) {
					removeTableErrorMessage(dt);
					dataTablesAjax(dt, ajaxMethod, ajaxAddress, button.attr("extend"));
				}
			}
		}

		buttons.push({
			text : button.text(),
			className : button.attr("class"),
			extend : button.attr("extend"),
			action : func
		});
	});
	buttonDiv.remove();
	initDatatablesWithButtons(table, buttons, loc, lang);
}

function initDatatablesWithButtons(table, buttons, loc, lang) {
	var columnDefs = [];
	var expand = false
	table.find("th").each(function() {
		var cd = $(this);
		
		var format = cd.attr("format");
		if (format === "date") {
			columnDefs.push({
				data : cd.attr("mapping"),
				name : cd.attr("mapping"),
				render : function ( data, type, row, meta ) {
					if (type == "sort" || type == 'type') {
				        return data;
					}
					var dpg = $.fn.datetimepicker.DPGlobal;
					return dpg.formatDate(parseDate(data), dpg.parseFormat(translations['tableDateFormat'], 'standard'), lang, 'standard');
			    }
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
                width:          '16px'
            });
		} else {
			columnDefs.push({
				data : cd.attr("mapping"),
				name : cd.attr("mapping")
			});
		}
	});

	var dataTable = table.DataTable({
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
		language : {
			url : loc
		},
		order : [],
		rowId : 'id',
		select : true
	});
	
	dataTable.column("id:name").visible(false);

	table.find("th").each(function() {
		var cd = $(this);
		var idt = cd.attr("mapping") + ":name";
		if (typeof cd.attr("sort") !== 'undefined') {
			dataTable.column(idt).order(cd.attr("sort"));
		}
	});
	if (expand) {
		var expandTemplate = $('#' + table.attr("id") + "_extension");
		dataTable['formatExpand'] = function(data) {
			expandTemplate.find("td.dynamic").each(function() {
				var elem = $(this);
				var mapping = elem.attr("mapping").split(".");
				var val = data;
				
				mapping.forEach(function(item, i, arr) {
					val = val[item];
				});
				
				elem.html(val);
			});
			return expandTemplate.html();
		};
		
		dataTable['expanded'] = {};
		
	    $('#' + table.attr("id") + ' tbody').on('click', 'td.details-control', function () {
	        var tr = $(this).closest('tr');
	        var tdi = tr.find("span.glyphicon");
	        var row = dataTable.row(tr);
	
	        if (row.child.isShown()) {
	            // This row is already open - close it
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

function initDatePicker(elem, loc) {
	elem.datetimepicker({
		language : loc,
		autoclose: true,
		container: elem.attr('data-date-container'),
		todayHighlight: true,
		startView: 'month',
		minView: 'month',
		forceParse: true
	});
}

function initFileInput(elem, loc) {
	var csrfToken = $("meta[name='_csrf']").attr("content") + "1";
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var headers = {};
	headers[csrfHeader] = csrfToken;
	elem.fileinput({
		ajaxSettings : {
			headers : headers
		},
		container : elem.closest('.form-group'),
		deleteUrl : '', // TODO
		elErrorContainer : '#err_' + elem.attr('id'),
		errorCloseButton : '',
		initialPreview : "<img class='kv-preview-data file-preview-image' src='http://lorempixel.com/800/460/nature/1'>",
		initialPreviewShowDelete : true,
		language : loc,
		layoutTemplates : {
			main1 : '<label class="control-label" for="'
					+ elem.attr('id') + '">' + elem.attr('placeholder')
					+ '</label>' + '{preview}\n'
					+ '<div class="input-group {class}">\n'
					+ '  {caption}\n'
					+ '  <div class="input-group-btn">\n'
					+ '    {remove}\n' + '    {cancel}\n'
					+ '    {upload}\n' + '    {browse}\n'
					+ '  </div>\n' + '</div>' + '<div id="err_'
					+ elem.attr('id') + '"></div>\n',
			actions : '<div class="file-actions">\n'
					+ '    <div class="file-footer-buttons">\n'
					+ '        {upload} {download} {delete} {zoom} {other}'
					+ '    </div>\n'
					+ '    <div class="clearfix"></div>\n' + '</div>',
		},
		msgErrorClass : "help-block",
		showClose : false,
		uploadUrl: elem.attr("upload-to")
	});
	
	elem.on('fileuploaded', function(event, data, previewId, index) {
	    elem['fileref'] = data.response.idt;
	});
	
	elem.on('fileremoved', function(event, data, previewId, index) {
	    elem['fileref'] = 'clear';
	});
}