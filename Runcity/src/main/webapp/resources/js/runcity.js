function findParentWithClass(element, cls) {
	var parent = element;

	while (!parent.hasClass(cls) && !(parent.prop("tagName") == "BODY")) {
		parent = parent.parent();
	}
	if (parent.prop("tagName") == "BODY") {
		return null;
	}
	return parent;
}

function removeFormErrorMessage(form) {
	form.find(".errorHolder").html("");
}

function removerFormFieldErrorMessage(form) {
	form.find("input").not('[type="submit"]').each(function() {
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
	var parent = findParentWithClass(element, "form-group");

	if (parent == null) {
		return;
	}
	parent.removeClass("has-error");
	parent.find(".help-block").remove();
}

function setErrorMessage(element, message) {
	var parent = findParentWithClass(element, "form-group");

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
		var par = findParentWithClass(elem, "form-group");
		var filled = 0;
		var total = 0;
		par.find("input").each(function(){
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
	var checkList = elem.attr("jschecks").split(";");
	var result = true;

	removeErrorMessage(elem);
	checkList.forEach(function(item, i, arr) {
		if (!checkElem(elem, item)) {
			result = false;
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

function validateElem(element) {
	var foo = element.attr("onchange");

	if (typeof foo !== 'undefined') {
		return eval(foo);
	} else {
		return true;
	}
}

function validateForm(form) {
	var result = true;

	form.find("input,select").not('[type="submit"]').each(function() {
		if (!validateElem($(this))) {
			result = false;
		}
	});

	return result;
}

function setInputValue(elem, val) {
	if (elem.attr('name') != "_csrf") {
		var dt = elem.attr('display-type');
		
		if (dt === 'colorpicker') {
			if (typeof val !== 'undefined') {
				elem.parent().colorpicker('setValue', val);
			} else {
				elem.parent().colorpicker('setValue', "");
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

function beforeOpenModal(form) {
	removeFormErrorMessage(form);
	form.find("input").not('[type="submit"]').each(function() {
		var elem = $(this);
		var val = elem.attr('default');
		setInputValue(elem, val);

		removeErrorMessage(elem);
	});
	form.find("select").each(function() {
		var elem = $(this);
		var val = elem.attr('default');
		if (typeof val !== 'undefined') {
			$(this).selectpicker('val', val);
		} else {
			$(this).selectpicker('val', "");
		}
		
		removeErrorMessage($(this));
	});
}

function addReloadLink(form, recId) {
	var reload = "<a href='#' onclick='beforeOpenModalFetch($(\"#"
			+ form.attr("id") + "\"), " + recId + ")'>"
			+ translations['reload'] + "</a>";
	setFormErrorMessage(form, reload);
}

function modalFormOpenSuccess(form, data, recId) {
	if (data.responseClass == "INFO") {
		changeModalFormState(form, false, false, false);

		form.find("input").not('[type="submit"]').each(function() {
			var elem = $(this);
			var name = elem.attr("name");
			var index = name.indexOf("['"); 
			if (index == -1) {
				setInputValue(elem, data[name]);
			} else {
				var to = name.substring(index + 2, name.length - 2);
				setInputValue(elem, data[name.substring(0, index)][to]);
			}
		});
		form.find("select").each(function() {
			var elem = $(this);
			var val = data[elem.attr("name")];
			if (typeof val === 'boolean') {
				val = val.toString();
			}

			elem.selectpicker('val', val);
		});

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
	removerFormFieldErrorMessage(form);
	if (data.statusText = "error" && data.status != 0) {
		setFormErrorMessage(form, translations['ajaxErr'].replace("{0}",
				data.status));
	} else {
		setFormErrorMessage(form, translations['ajaxHangGet']);
	}
	addReloadLink(form, recId);
}

function beforeOpenModalFetch(form, recId) {
	beforeOpenModal(form);

	if (typeof recId === 'undefined') {
		return;
	}
	
	changeModalFormState(form, true, true, false);

	var jsonUrl = form.attr("fetchFrom").replace("{0}", recId);

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
	form.find('[autofocus="autofocus"]')[0].focus();
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

	form.find("input").not('[type="submit"]').each(function() {
		$(this).prop("disabled", submitDisabled);
	});

	if (keepOnScreen) {
		form.attr("loading", true);
	} else {
		form.attr("loading", false);
	}
}

function getFormData(form) {
	var res = {};

	form.find("input").not('[type="submit"]').each(function() {
		var elem = $(this);
		var format = elem.attr('format');
		if (format == 'array') {
			res[elem.attr('name')] = elem.val().split(',');
		} else if (format == 'langObj') {
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
			res[elem.attr('name')] = elem.val();			
		}
	});

	form.find("select").each(function() {
		var elem = $(this);
		res[elem.attr('name')] = elem.selectpicker('val');
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
	form.find("input,select").not('[type="submit"]').each(function() {
		var elem = $(this);
		removeErrorMessage(elem);
		var name = elem.attr("name");
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
	removerFormFieldErrorMessage(form);
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

function submitModalForm(form) {
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
	var parent = findParentWithClass($(dt.table().node()), "dataTables_wrapper");

	if (parent == null) {
		return;
	}
	parent.find(".errorHolder").html("");
}

function setTableErrorMessage(dt, message) {
	var parent = findParentWithClass($(dt.table().node()), "dataTables_wrapper");

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

function initDatatables(table) {
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
					beforeOpenModal(form);
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
	initDatatablesWithButtons(table, buttons);
}

function initDatatablesWithButtons(table, buttons) {
	var columnDefs = [];
	table.find("th").each(function() {
		var cd = $(this);
		columnDefs.push({
			data : cd.attr("mapping"),
			name : cd.attr("mapping")
		});
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
				+ "<'row'<'col-sm-12'tr>>"
				+ "<'row'<'col-sm-5'i><'col-sm-7'p>>",
		language : {
		    "decimal":        "",
		    "emptyTable":     translations['datatablesEmptyTable'],
		    "info":           translations['datatablesInfo'],
		    "infoEmpty":      translations['datatablesInfoEmpty'],
		    "infoFiltered":   translations['datatablesInfoFiltered'],
		    "infoPostFix":    "",
		    "thousands":      ",",
		    "lengthMenu":     translations['datatablesLengthMenu'],
		    "loadingRecords": translations['datatablesLoadingRecords'],
		    "processing":     translations['datatablesProcessing'],
		    "search":         translations['datatablesSearch'],
		    "zeroRecords":    translations['datatablesZeroRecords'],
		    "paginate": {
		        "first":      translations['datatablesPFirst'],
		        "last":       translations['datatablesPLast'],
		        "next":       translations['datatablesPNext'],
		        "previous":   translations['datatablesPPrevios']
		    },
		    "aria": {
		        "sortAscending":  translations['datatablesASortA'],
		        "sortDescending": translations['datatablesASortD']
		    }
		},
		order : [],
		select : true
	});

	dataTable.column("id:name").visible(false);
}