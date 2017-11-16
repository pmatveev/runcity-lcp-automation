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
	form.find("input").not('[type="submit"]')
		.each(function() {
			removeErrorMessage($(this));
		});
}

function setFormErrorMessage(form, message) {
	var errHolder = form.find(".errorHolder");
	var errDiv = errHolder.find(".alert-danger");
	if (!errDiv.length) {
		errHolder.append('<div class="alert alert-danger">' + message + '</div>');
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
	
	if (rule == "email") {	// 
		if (!/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/.test(elem.val())) {
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

	form.find("input,select").not('[type="submit"]')
			.each(function() {
				if (!validateElem($(this))) {
					result = false;
				}
			});

	return result;
}

function beforeOpenModal(form) {
	removeFormErrorMessage(form);
	form.find("input").not('[type="submit"]')
			.each(function() {
				if (($(this)).attr('name') != "_csrf") {
						$(this).val("");
				}
				removeErrorMessage($(this));
			});
}

function addReloadLink(form, recId) {
	var reload = "<a href='#' onclick='beforeOpenModalFetch($(\"#" + form.attr("id") + "\"), " + recId + ")'>" + translations['reload'] + "</a>";
	setFormErrorMessage(form, reload);
}

function modalFormOpenSuccess(form, data, recId) {
	console.log("SUCCESS: ", data);
	if (data.responseClass == "INFO") {
		changeModalFormState(form, false, false, false);
		
		form.find("input").not('[type="submit"]')
				.each(function() {
					var elem = $(this);
					elem.val(data[elem.attr("name")]);
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
	console.log("ERROR: ", data);
	changeModalFormState(form, true, false, false);

	removeFormErrorMessage(form);
	removerFormFieldErrorMessage(form);
	if (data.statusText = "error" && data.status != 0) {
		setFormErrorMessage(form, translations['ajaxErr'].replace("{0}", data.status));
	} else {
		setFormErrorMessage(form, translations['ajaxHangGet']);
	}
	addReloadLink(form, recId);
}

function beforeOpenModalFetch(form, recId) {
	removeFormErrorMessage(form);
	form.find("input").not('[type="submit"]')
			.each(function() {
				if (($(this)).attr('name') != "_csrf") {
						$(this).val("");
				}
				removeErrorMessage($(this));
			});
	
	changeModalFormState(form, true, true, false);

	var jsonUrl = form.attr("fetchFrom"); 
	if (jsonUrl.indexOf('{0}') >= 0) {
		jsonUrl.replace("{0}", recId);
	}
	
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: jsonUrl,
		dataType: "json",
		timeout: 10000,
		success: function(data) {
			modalFormOpenSuccess(form, data, recId);
		},
		error: function(data) {
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
			submit.parent().html("<div class='loader'></div>" + submit.parent().html());
		}		
	} else {
		submit.parent().find(".loader").remove();			
	}
	
	form.find("input").not('[type="submit"]')
		.each(function() {
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
	
	$.map(form.serializeArray(), function(n, i){
		res[n['name']] = n['value'];
	});
	
	return res;
}

function closeModalForm(form) {
	$('#modal_' + form.attr("id")).modal('hide');
}

function modalFormSuccess(form, data) {
	changeModalFormState(form, false, false, false);
	
	if (data.responseClass == "INFO") {
		closeModalForm(form);
		return;
	}
	
	removeFormErrorMessage(form);
	if (data.errors) {
		data.errors.forEach(function(item, i, arr) {
			setFormErrorMessage(form, item);
		});
	}
	form.find("input").not('[type="submit"]')
			.each(function() {
				var elem = $(this);
				removeErrorMessage(elem);
				var err = data.colErrors[elem.attr("name")];
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
			setFormErrorMessage(form, translations['ajaxErr'].replace("{0}", data.status));
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
		type: "POST",
		contentType: "application/json",
		url: form.attr("action"),
		data: jsonString,
		dataType: "json",
		timeout: 10000,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
		success: function(data) {
			modalFormSuccess(form, data);
		},
		error: function(data) {
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

function sortListbox(listbox) {
	console.log("Sorting: " + listbox.text());
	var options = listbox.find(".text");
	console.log(options);
	var arr = options.map(function(_, o) {
		return {
			t : $(o).text(),
			v : o.value
		};
	}).get();
	console.log(arr);
	arr.sort(function(o1, o2) {
		if (o1.t == "") { return -1; }
		if (o2.t == "") { return 1; }
		
		return o1.t.toUpperCase() > o2.t.toUpperCase() ? 1
				: o1.t.toUpperCase() < o2.t.toUpperCase() ? -1 : 0;
	});
	console.log("Sorted: ", arr);
	options.each(function(i, o) {
		o.value = arr[i].v;
		$(o).text(arr[i].t);
	});
}

function initDatatables(table) {
	var buttonsAjax = table.attr('ajaxButtonSource');
	
	if (typeof buttonsAjax !== 'undefined') {
		$.ajax({
			type: "GET",
			contentType: "application/json",
			url: buttonsAjax,
			dataType: "json",
			timeout: 10000,
			success: function(data) {
				initDatatablesWithButtons(table, data);
			},
			error: function(data) {
				console.log(data);
				initDatatablesWithButtons(table, { buttons : [] });
			}
		});
	} else {
		initDatatablesWithButtons(table, { buttons : [] });
	}
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
		dom : "<'row'<'col-sm-6'l><'col-sm-6'f>>" 
				+ "<'row'<'col-sm-12'B>>"
				+ "<'row'<'col-sm-12'tr>>"
				+ "<'row'<'col-sm-5'i><'col-sm-7'p>>",
		ajaxSource : table.attr("ajaxSource"),
		columns : columnDefs,
		buttons : {
			dom : {
				container: {
		            tag: 'div',
		            className: 'button-item'
		        }
			},
			buttons : buttons.buttons
		},
		select : true
	});
	
	dataTable.column("id:name").visible(false);
}