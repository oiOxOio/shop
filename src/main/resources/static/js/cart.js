// 渲染购物车商品
if (window.localStorage) {

    // 获取购物车商品
    var products = localStorage.getItem('cart')
    products = JSON.parse(products)

    // 购物车内有商品
    if (products && products.length > 0) {

        $('.cart').removeClass('d-none')
        $('.cart-empty').addClass('d-none')

        // 创建商品HTML内容
        var product_list = ''
        for (var i = 0; i < products.length; i++) {
            product_list += '<div class="d-flex item justify-content-between">\n' +
                '                    <div class="d-flex left">\n' +
                '                        <img src="' + products[i]['img'] + '">\n' +
                '                        <div>\n' +
                '                                <input type="hidden" class="product_id" value="' + products[i]['product'] + '">\n' +
                '                            <input type="hidden" class="attr_id" value="' + products[i]['attrid'] + '">\n' +
                '                            <h5><a href="/product/' + products[i]['product'] + '.html" class="d-flex">' + products[i]['title'] + '</a></h5>\n' +
                '                            <P>' + products[i]['attr'] + '&nbsp;<small class="number d-none">x' + products[i]['number'] + '</small></P>\n' +
                '                            <h5><small>￥</small><span class="price">' + products[i]['price'] + '</span></h5>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="d-flex flex-column justify-content-center right">\n' +
                '                        <span class="wish"><ion-icon src="/svg/heart.svg"></ion-icon>收藏</span>\n' +
                '                        <span class="remove"><ion-icon src="/svg/trash.svg"></ion-icon>删除</span>\n' +
                '                    </div>\n' +
                '                </div>'
        }

        // 将商品添加到页面
        $('.cart .content').html(product_list)

    }
} else {

    prompt_box('error', '您的浏览器不支持此功能', false)

}

// 选择商品和取消
$('.cart .item').on('click', function () {

    // 选中商品的价格和数量
    var price = $(this).find('.price').text()
    price = parseFloat(price)

    // 购物车总价格和总数量
    var totalPrice = $('.total .price .val')
    var totalNumber = $('.total .number')
    var totalPriceVal = parseFloat(totalPrice.text())
    var totalNumberVal = parseInt(totalNumber.text())

    // 修改样式
    $(this).toggleClass('active')
    var number = $('.cart .item.active').length
    totalNumber.text(number)

    // 取消选择
    if (!$(this).hasClass('active')) {

        // 计算价格数量
        totalPrice.text((totalPriceVal - price).toFixed(2))

        // 选择商品
    } else {

        // 计算价格数量
        totalPrice.text((totalPriceVal + price).toFixed(2))

    }

})

// 删除购物车商品
$('.cart .item .remove').on('click', function () {

    var cart = localStorage.getItem('cart')
    var products = JSON.parse(cart)
    var product_id = $(this).parents('.item').find('.product_id').val()
    var attr_id = $(this).parents('.item').find('.attr_id').val()

    // 创建新购物车列表
    var new_products = []

    // 排除要删除的商品
    for (var i = 0; i < products.length; i++) {
        if (products[i]['product'] == product_id && products[i]['attrid'] == attr_id) continue
        new_products.push(products[i])
    }

    // 替换购物车列表
    localStorage.setItem('cart', JSON.stringify(new_products))

    // 刷新页面
    location.reload()

    // 阻止事件冒泡
    return false

})


// 点击付款按钮
$('.total .btn-pay').on('click', function () {

    // 修改表单数据
    var products = []
    var productElem = $('.cart .item.active')

    for (var i = 0; i < productElem.length; i++) {

        var product = $(productElem[i])
        var number = product.find('.number').text()
        number = number.substr(1, number.length)

        products.push({
            product: product.find('.product_id').val(),
            attr: product.find('.attr_id').val(),
            number: number
        })

    }

    $('.total form input').val(JSON.stringify(products))

})

// 添加商品收藏
$('.cart .item .wish').on('click', function () {

    var product_id = $(this).parents('.item').find('.product_id').val()
    var attr_id = $(this).parents('.item').find('.attr_id').val()

    // 添加收藏
    if (product_id && attr_id) addWishlist(product_id, attr_id)

    // 阻止事件冒泡
    return false

})

// 页面打开选中所有商品
$('.cart .item').click()

// 商品图片高度和宽度一致
W2H($('.cart .item img'))