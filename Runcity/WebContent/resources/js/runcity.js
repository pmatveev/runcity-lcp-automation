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
	element.parent().removeClass("has-error");
	element.parent().find(".help-block").remove();
}

function setErrorMessage(element, message) {
	element.parent().addClass("has-error");
	var help = element.parent().find(".help-block");
	if (!help.length) {
		element.parent().append(
				'<span class="help-block">' + message + '</span>');
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

function checkElem(elem, rule, translations) {
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

function checkInput(elem, translations) {
	var checkList = elem.attr("jschecks").split(";");
	var result = true;
	
	removeErrorMessage(elem);
	checkList.forEach(function(item, i, arr) {
		if (!checkElem(elem, item, translations)) {
			result = false;
		}
	});
	
	return result;
}

function checkPwdIdent(pwd1, pwd2, translations) {
	if (pwd1.val() != pwd2.val()) {
		setErrorMessageStrict(pwd2, translations['passwordMatch']);
		return false;
	}

	return true;
}

function checkPwdInput(pwd, pwdConf, translations) {
	var res1 = checkInput(pwd, translations);
	var res2 = checkInput(pwdConf, translations);
	var res3 = checkPwdIdent(pwd, pwdConf, translations);
	
	return res1 && res2 && res3;
}

function validateElem(element, translations) {
	var foo = element.attr("onchange");
	
	if (typeof foo !== 'undefined') {
		return eval(foo);
	} else {
		return true;
	}
}

function validateForm(form, translations) {
	var result = true;

	form.find("input,select").not('[type="submit"]')
			.each(function() {
				if (!validateElem($(this), translations)) {
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

function afterOpenModal(form) {
	form.find('[autofocus="autofocus"]')[0].focus();
}

function beforeCloseModal(form) {
	if (form.attr("loading") == "true") {
		return false;
	}
	return true;
}

function changeModalFormState(form, submitDisabled, loading) {
	var submit = form.find('[type="submit"]');
	submit.prop("disabled", submitDisabled);
	form.find('[data-dismiss="modal"]').prop("disabled", submitDisabled);
	
	if (submitDisabled) {
		submit.parent().html("<div class='loader'></div>" + submit.parent().html());
	} else {
		submit.parent().find(".loader").remove();		
	}
	
	if (loading) {
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
	changeModalFormState(form, false, false);
	
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
	console.log("ERROR: ", data);
	changeModalFormState(form, false, false);

	removeFormErrorMessage(form);
	removerFormFieldErrorMessage(form);
	if (data.statusText = "error" && data.status != 0) {
		setFormErrorMessage(form, translations['ajaxErr'].replace("{0}", data.status));
	} else {
		setFormErrorMessage(form, translations['ajaxHang']);
	}
}

function submitModalForm(form, translations) {
	event.preventDefault();
	if (!validateForm(form, translations)) {
		return;
	}

	changeModalFormState(form, true, true);
	
	var csrfToken = $("meta[name='_csrf']").attr("content"); 
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	
	console.log("Sending JSON:\n" + JSON.stringify(getFormData(form)));
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: form.attr("action"),
		data: JSON.stringify(getFormData(form)),
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