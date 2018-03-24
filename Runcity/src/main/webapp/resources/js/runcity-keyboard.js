function isMobileDevice() {
    return (typeof window.orientation !== 'undefined') || (navigator.userAgent.indexOf('IEMobile') !== -1);
};

$(function(){
	$('div.keyboard').bind('enable', function() {
		var div = $(this);
		div.removeClass('disabled');
		div.find('.keyrow').removeClass('hidden');
		div.find('li.switch').html('<span class="glyphicon glyphicon-triangle-top"></span>');
		div.prop('input').prop('readonly', true);
		div.prop('input').closest('form').find('.form-footer').find('button[type="submit"]').addClass('hidden');
	});
	
	$('div.keyboard').bind('disable', function() {
		var div = $(this);
		div.addClass('disabled');
		div.find('.keyrow').addClass('hidden');
		div.find('li.switch').html('<span class="glyphicon glyphicon-triangle-bottom"></span>');
		div.prop('input').prop('readonly', false);
		div.prop('input').closest('form').find('.form-footer').find('button[type="submit"]').removeClass('hidden');
	});
	
	$('div.keyboard').bind('init', function() {
		var div = $(this);
		var input = $('#' + div.attr('for'));
		if (!input.length) {
			return;
		}
		div.prop('input', input);
		
		if (div.prop('keyboard')) {
			return;
		}
		div.prop('keyboard', true);		
		div.find('ul.keys').append('<div class="row"><li class="switch"></li></div>');
		
		var enable = div.attr('enable');

		switch (enable) {
		case 'true':
			div.trigger('enable');
			break;
		case 'false':
			div.trigger('disable');
			break;
		default:
			if (isMobileDevice()) {
				div.trigger('enable');
			} else {
				div.trigger('disable');
			}
		}
		
		div.find("li").click(function() {
			var button = $(this); 
			var input = button.closest('.keyboard').prop('input');
			
			if (button.hasClass('symbol')) {
				var val = input.val() + button.html();
				input.val(val);
				return;
			}
			
			if (button.hasClass('enter')) {
				input.closest('form').submit();
			}
			
			if (button.hasClass('backspace')) {
				var val = input.val();
				val = val.substring(0, val.length - 1);
				input.val(val);
				return;
			}
			
			if (button.hasClass('switch')) {
				if (div.hasClass('disabled')) {
					div.trigger('enable');
				} else {
					div.trigger('disable');
				}
			}
		});
	});

	$('div.keyboard').trigger('init');
});