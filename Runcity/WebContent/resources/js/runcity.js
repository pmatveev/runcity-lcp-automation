/*
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

function checkEmail(emailId, translations) {
	var element = $("#" + emailId);
	
	if (element.val() == "") {
		setErrorMessageStrict(element, translations['required']);
		return false;
	}

	var re = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
	if (!re.test(element.val())) {
		setErrorMessageStrict(element, translations['invalidEmail']);
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
*/
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
		help.html(help.html() + '<br/>' + message);
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

function checkInput(elemId, translations) {
	var elem = $('#' + elemId);
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

function checkPwdIdent(pwdId, pwdConfId, translations) {
	var pwd1 = $("#" + pwdId);
	var pwd2 = $("#" + pwdConfId);
	
	if (pwd1.val() != pwd2.val()) {
		setErrorMessageStrict(pwd2, translations['passwordMatch']);
		return false;
	}

	return true;
}

function checkPwdInput(pwdId, pwdConfId, translations) {
	var res1 = checkInput(pwdId, translations);
	var res2 = checkInput(pwdConfId, translations);
	var res3 = checkPwdIdent(pwdId, pwdConfId, translations);
	
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