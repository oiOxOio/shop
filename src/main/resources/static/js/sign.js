// 输入框聚焦
$('.form-group input').on('focus', function () {

    $(this).parents('.form-group').addClass('focus')

})

// 输入框失去焦点
$('.form-group input').on('blur', function () {

    if ($(this).val() === '') $(this).parents('.form-group').removeClass('focus')

})

// 点击下一步按钮
$('.sign .btn-next').on('click', function () {

    $.post({
        url: '/hasemail',
        data: {
            email: $('#email').val()
        },
        success: function (data) {

            if (data == "true") {

                // 登录
                $('#password').removeAttr('disabled')
                $('.sign .left').removeClass('init')
                $('.sign .left').addClass('signin')

            } else {

                // 注册
                $('#security-code').removeAttr('disabled')
                $('#password').removeAttr('disabled')
                $('#agreement').removeAttr('disabled')
                $('.sign .left').removeClass('init')
                $('.sign .left').addClass('signup')

            }

        }

    })
})

// 发送验证码
$('.sign .btn-code').on('click', function () {

    $(this).attr("disabled", "disabled")

    $.post({
        url: "/sendcode",
        data: {
            email: $('#email').val()
        },
        success: function (msg) {
            console.log(msg)
        }
    })

    var seconds = 60;
    var btn_code = $('.sign .btn-code');
    var interval = setInterval(function () {

        if (seconds <= 0) {

            btn_code.text("发送验证码")
            btn_code.removeAttr("disabled")
            clearInterval(interval)

        } else {

            btn_code.text(seconds + "秒后再发");
            seconds--;

        }
    }, 1000)

})

// 监听表单提交
$('.form-sign').on('submit', function () {

    if ($('.left').hasClass('init')) {

        $('.form-sign .btn-next').click()
        return false;

    }

})