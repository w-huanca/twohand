(function($) {

	'use strict';
	// Mean Menu
    // ����߶���վ��ܣ�http://www.bootstrapmb.com
	$('.mean-menu').meanmenu({
		meanScreenWidth: "991"
	});
	
	// Sticky, Go To Top JS
	$(window).on('scroll', function() {
		// Header Sticky JS
		if ($(this).scrollTop() >150){  
			$('.navbar-area, .src-map').addClass("is-sticky");
		}
		else{
			$('.navbar-area, .src-map').removeClass("is-sticky");
		};

		// Go To Top JS
		var scrolled = $(window).scrollTop();
		if (scrolled > 300) $('.go-top').addClass('active');
		if (scrolled < 300) $('.go-top').removeClass('active');
	});
	
	// Click Event JS
	$('.go-top').on('click', function() {
		$("html, body").animate({ scrollTop: "0" }, 50);
	});

	// // Count Time JS
	function makeTimer() {
		var endTime = new Date("november  30, 2022 17:00:00 PDT");			
		var endTime = (Date.parse(endTime)) / 1000;
		var now = new Date();
		var now = (Date.parse(now) / 1000);
		var timeLeft = endTime - now;
		var days = Math.floor(timeLeft / 86400); 
		var hours = Math.floor((timeLeft - (days * 86400)) / 3600);
		var minutes = Math.floor((timeLeft - (days * 86400) - (hours * 3600 )) / 60);
		var seconds = Math.floor((timeLeft - (days * 86400) - (hours * 3600) - (minutes * 60)));
		if (hours < "10") { hours = "0" + hours; }
		if (minutes < "10") { minutes = "0" + minutes; }
		if (seconds < "10") { seconds = "0" + seconds; }
		$("#days").html(days + "<span>Day</span>");
		$("#hours").html(hours + "<span>Hours</span>");
		$("#minutes").html(minutes + "<span>Minutes</span>");
		$("#seconds").html(seconds + "<span>Seconds</span>");
	}
	setInterval(function() { makeTimer(); }, 300);

	// Preloader
	$(window).on('load', function() {
		$('.preloader').addClass('preloader-deactivate');
	}) 

	// Others Option For Responsive JS
	$(".others-option-for-responsive .dot-menu").on("click", function(){
		$(".others-option-for-responsive .container .container").toggleClass("active");
	});

	// Hero Slide
	$('.hero-slide').owlCarousel({
		items: 1,
		loop: true,
		margin: 0,
		nav: true,
		dots: false,
		autoHeight: true,
		autoplay: true,
		smartSpeed: 1000,
		autoplayHoverPause: true,
		mouseDrag: false,
		animateOut: "fadeOut",
		navText: [
			"<i class='ri-arrow-left-line'></i>",
			"<i class='ri-arrow-right-line'></i>",
		],
	});

	// Partner Slide JS
	$('.partner-slide').owlCarousel({
		loop: true,
		margin: 30,
		nav: false,
		dots: false,
		autoplay: true,
		smartSpeed: 1000,
		autoplayHoverPause: true,
		navText: [
			"<i class='ri-arrow-left-s-line'></i>",
			"<i class='ri-arrow-right-s-line'></i>",
		],
		responsive: {
			0: {
				items: 2,
			},
			414: {
				items: 3,
			},
			576: {
				items: 4,
			},
			768: {
				items: 5,
			},
			992: {
				items: 6,
			},
			1200: {
				items: 7,
			},
		},
	});

	// Partner Slide Two JS
	$('.partner-slide-two').owlCarousel({
		loop: true,
		margin: 30,
		nav: false,
		dots: false,
		autoplay: true,
		smartSpeed: 1000,
		autoplayHoverPause: true,
		navText: [
			"<i class='ri-arrow-left-s-line'></i>",
			"<i class='ri-arrow-right-s-line'></i>",
		],
		responsive: {
			0: {
				items: 2,
			},
			414: {
				items: 3,
			},
			576: {
				items: 4,
			},
			768: {
				items: 5,
			},
			992: {
				items: 6,
			},
			1200: {
				items: 6,
			},
		},
	});

	// Solutions Slide JS
	$('.solutions-slide').owlCarousel({
		loop: true,
		margin: 30,
		nav: false,
		dots: true,
		autoplay: true,
		smartSpeed: 1000,
		autoplayHoverPause: true,
		responsive: {
			0: {
				items: 1,
			},
			768: {
				items: 1,
			},
			992: {
				items: 2,
			},
			1200: {
				items: 2,
			},
		},
	});

	// Solutions Slide JS
	$('.testimonial-slide').owlCarousel({
		loop: true,
		margin: 30,
		nav: false,
		dots: true,
		autoplay: true,
		smartSpeed: 1000,
		autoplayHoverPause: true,
		responsive: {
			0: {
				items: 1,
			},
			768: {
				items: 1,
			},
			992: {
				items: 2,
			},
			1200: {
				items: 2,
			},
		},
	});

	// Subscribe form JS
	$(".newsletter-form").validator().on("submit", function (event) {
		if (event.isDefaultPrevented()) {
		// handle the invalid form...
			formErrorSub();
			submitMSGSub(false, "Please enter your email correctly.");
		} else {
			// everything looks good!
			event.preventDefault();
		}
	});
	function callbackFunction (resp) {
		if (resp.result === "success") {
			formSuccessSub();
		}
		else {
			formErrorSub();
		}
	}
	function formSuccessSub(){
		$(".newsletter-form")[0].reset();
		submitMSGSub(true, "Thank you for subscribing!");
		setTimeout(function() {
			$("#validator-newsletter, #validator-newsletter-2").addClass('hide');
		}, 4000)
	}
	function formErrorSub(){
		$(".newsletter-form").addClass("animated shake");
		setTimeout(function() {
			$(".newsletter-form").removeClass("animated shake");
		}, 1000)
	}
	function submitMSGSub(valid, msg){
		if(valid){
			var msgClasses = "validation-success";
		} else {
			var msgClasses = "validation-danger";
		}
		$("#validator-newsletter, #validator-newsletter-2").removeClass().addClass(msgClasses).text(msg);
	}
	
	// AJAX MailChimp JS
	$(".newsletter-form").ajaxChimp({
		url: "https://HiboTheme.us20.list-manage.com/subscribe/post?u=60e1ffe2e8a68ce1204cd39a5&amp;id=42d6d188d9", // Your url MailChimp
		callback: callbackFunction
	});

	// Search Popup JS
	$('.close-btn').on('click', function() {
		$('.search-overlay').fadeOut();
		$('.search-btn').show();
		$('.close-btn').removeClass('active');
	});
	$('.search-btn').on('click', function() {
		$(this).hide();
		$('.search-overlay').fadeIn();
		$('.close-btn').addClass('active');
	});

	// TweenMax JS
	// $('.tween-animation').mousemove(function(e){
	// 	var wx = $(window).width();
	// 	var wy = $(window).height();
	// 	var x = e.pageX - this.offsetLeft;
	// 	var y = e.pageY - this.offsetTop;
	// 	var newx = x - wx/2;
	// 	var newy = y - wy/2;
	// 	$('.only-shape, .banner-img').each(function(){
	// 		var speed = $(this).attr('data-speed');
	// 		if($(this).attr('data-revert')) speed *= -.4;
	// 		TweenMax.to($(this), 1, {x: (1 - newx*speed), y: (1 - newy*speed)});
	// 	});
	// });

	// Odometer JS
	$('.odometer').appear(function(e) {
		var odo = $(".odometer");
		odo.each(function() {
			var countNumber = $(this).attr("data-count");
			$(this).html(countNumber);
		});
	});

	// Popup Video JS
	$('.popup-youtube, .popup-vimeo').magnificPopup({
		disableOn: 300,
		type: 'iframe',
		mainClass: 'mfp-fade',
		removalDelay: 160,
		preloader: false,
		fixedContentPos: false,
	});

	// Input Plus & Minus Number JS
	$('.input-counter').each(function() {
		const spinner = jQuery(this),
			// 类型为text的input元素，命名为input
			input = spinner.find('input[type="text"]'),
			// 类名为plus-btn的元素，命名为btnUp
			btnUp = spinner.find('.plus-btn'),
			// 类名为minus-btn的元素，命名为btnDown
			btnDown = spinner.find('.minus-btn'),
			// 获取input元素的min属性值
			min = input.attr('min'),
			// 获取input元素的max属性值
			max = input.attr('max');

		// 为btnUp元素添加click事件监听器, 当用户点击btnUp时，将执行该函数
		btnUp.on('click', function() {
			let newVal;
			// 将input元素的值转换为浮点数, 命名为oldValue
			const oldValue = parseFloat(input.val());
			// 判断oldValue是否大于或等于max。如果是，则将newVal设为oldValue；否则，将newVal设为oldValue + 1
			if (oldValue >= max) {
				newVal = oldValue;
			} else {
				newVal = oldValue + 1;
			}
			// 将input元素的值设为newVal
			spinner.find("input").val(newVal);
			// 触发input元素的change事件
			spinner.find("input").trigger("change");
		});
		// 为btnDown元素添加click事件监听器, 当用户点击btnDown时，将执行该函数
		btnDown.on('click', function() {
			let newVal;
			const oldValue = parseFloat(input.val());
			if (oldValue < min) {
				newVal = oldValue;
			} else {
				newVal = oldValue - 1;
			}
			spinner.find("input").val(newVal);
			spinner.find("input").trigger("change");
		});
	});
})(jQuery);