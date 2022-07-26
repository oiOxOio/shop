// 确认收货
$('.btn-receive').on('click', function () {

    var orderID = $(this).parents('.item').find('.order-id').text()

    // 发送收货请求
    $.post({
        url: '/user/orders/receipt',
        data: {
            orderid: orderID
        },
        success: function (data) {

            // 状态提示
            if (data == 'success') {
                prompt_box('success', '收货完成', 'reload')
            } else if (data == 'fail') {
                prompt_box('fail', '收货失败', false)
            } else {
                prompt_box('error', '发生错误', false)
            }

        },
        error: function () {

            prompt_box('error', '发生错误', false)

        }

    })

})