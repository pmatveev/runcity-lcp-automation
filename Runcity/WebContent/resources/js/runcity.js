function removeErrorMessage(element) {
	element.parent().removeClass("has-error");
	element.parent().find(".help-block").remove();
}

function setErrorMessage(element, message) {
	element.parent().addClass("has-error");
	if (!element.parent().find(".help-block").length) {
		element.parent().append(
				'<span class="help-block">' + message + '</span>');
	}
}

function setErrorMessageStrict(element, message) {
	element.parent().addClass("has-error");
	var help = element.parent().find(".help-block");
	if (!help.length) {
		element.parent().append(
				'<span class="help-block">' + message + '</span>');
	} else {
		help.text(message);
	}
}

function checkPwdComplex(pwdId, translations) {
	var element = $("#" + pwdId);

	if (element.val() == "") {
		setErrorMessageStrict(element, translations['required']);
		return false;
	}

	var re = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;
	if (!re.test(element.val())) {
		setErrorMessageStrict(element, translations['passwordStrength']);
		return false;
	}
	removeErrorMessage(element);
	return true;
}

function checkPwd2(pwdId, pwd2Id, translations) {
	var pwd1 = $("#" + pwdId);
	var pwd2 = $("#" + pwd2Id);

	if (pwd2.val() == "") {
		setErrorMessageStrict(pwd2, translations['required']);
		return false;
	}

	if (pwd1.val() != pwd2.val()) {
		setErrorMessageStrict(pwd2, translations['passwordMatch']);
		return false;
	}
	removeErrorMessage(pwd2);
	return true;
}

function validateElem(element, translations) {
	if (element.attr("checkpassword") == 1) {
		return checkPwdComplex(element.attr("id"), translations);
	}

	var checkPwdEqual = element.attr("checkpwdequal");
	if (typeof checkPwdEqual !== typeof undefined && checkPwdEqual !== false) {
		return checkPwd2(element.attr("checkpwdequal"), element.attr("id"),
				translations);
	}

	if (element.attr("checkrequired") == 1 && element.val() == "") {
		setErrorMessage(element, translations['required']);
		return false;
	}

	removeErrorMessage(element);
	return true;
}

function validateForm(formId, translations) {
	var result = true;

	$("form[id='" + formId + "']").find("input,select").not('[type="submit"]')
			.each(function() {
				if (!validateElem($(this), translations)) {
					result = false;
				}
			});

	return result;
}