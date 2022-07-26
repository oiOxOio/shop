// 删除收藏商品
$('.item .btns .delete').on('click', function () {

    $.ajax({
        url: "/user/remove_wish",
        method: "post",
        data: {
            id: $(this).attr('value')
        },
        success: function (data) {

            var status
            var text

            if (data == 'success') {
                location.reload()
                return
            } else if (data == 'fail') {
                status = 'fail'
                text = '删除失败'
            } else {
                status = 'error'
                text = '删除错误'
            }

            // 弹框提示
            prompt_box(status, text, 'reload')

        }
    })

})

//添加购物车
$('.item .cart').on('click', function () {

    var item = $(this).parents('.item')
    var title = item.find('.title').text()
    var productID = $(this).attr('value')
    var attr = item.find('.attr').text()
    var attrID = item.find('.attr').attr('value')
    var price = item.find('.price').text();
    var img = item.find('img').attr('src');

    addCart(productID, title, attr, price, img, 1, attrID)

})