// 向下滑动导航栏固定顶部
document.onscroll = function () {
    if (scrollY >= 200) {
        $(".navbar-comm").addClass('navbar-drop')
        $(".navbar-comm").addClass('fixed-top')
    } else {
        $(".navbar-comm").removeClass('navbar-drop')
        $(".navbar-comm").removeClass('fixed-top')
    }
}

//添加收藏
function addWishlist(product_id, attr_id) {

    $.ajax({
        url: "/user/add_wish",
        method: "post",
        data: {
            productID: product_id,
            attrID: attr_id
        },
        success: function (data) {
            var status
            var text
            if (data == 'success') {
                status = 'success'
                text = '收藏成功'
            } else if (data == 'fail') {
                status = 'fail'
                text = '收藏失败'
            } else if (data == 'exist') {
                status = 'ordinary'
                text = '已经收藏过了'
            } else {
                status = 'friendly'
                text = '请先登录'
            }
            // 弹框提示
            prompt_box(status, text, false)
        }
    })

}

// 提示弹框
function prompt_box(status, text, address) {

    // 修改内容
    $('.prompt-status .modal-body img').attr('src', '/img/prompt/' + status + '.svg')
    $('.prompt-status .modal-body .text').text(text)

    // 触发弹框
    $('.prompt-status > button').click()

    //关闭弹窗操作
    if (address != false) {

        $('#prompt-box').on('click', function () {

            // 刷新页面
            if (address == 'reload') {

                location.reload()

                // 重定向2
            } else {

                location.href = address

            }

        })
    }

}

// 更新导航购物车图标的商品数量
function updateCartIco() {

    var cart = JSON.parse(localStorage.getItem('cart'))
    var cartNumber = cart == null ? 0 : cart.length
    var ico = $('.navbar-comm .cart span')

    // 购物车商品数量大于0显示角标
    if (cartNumber > 0) {
        ico.removeClass('d-none')
        ico.text(cartNumber)
        return
    }

    // 不显示角标
    ico.addClass('d-none')

}

updateCartIco()

// 添加购物车
function addCart(productID, title, attr, price, img, numbers, attrid) {
    // 浏览器是否支持localStorage
    if (window.localStorage) {

        // 获取购物车数据
        var cart = localStorage.getItem('cart')
        // 商品列表
        var products = []
        if (cart) products = JSON.parse(cart)

        // 通过商品ID和属性ID 验证是否已经添加过
        for (var i = 0; i < products.length; i++) {
            if (products[i]['product'] == productID && products[i]['attrid'] == attrid) {
                prompt_box('ordinary', '已经在购物车里了', false)
                return
            }
        }

        // 创建商品对象
        var product = {
            product: productID,
            title: title,
            attr: attr,
            price: price,
            img: img,
            number: numbers,
            attrid: attrid
        }

        // 把商品对象添加到商品列表
        products.push(product)
        // 把商品列表添加到购物车
        localStorage.setItem('cart', JSON.stringify(products))
        // 更新导航购物车图标商品数量
        updateCartIco()
        // 提示完成
        prompt_box('success', '加入购物车成功', false)

    } else {
        prompt_box('error', '您的浏览器不支持此功能', false)
    }
}

// 商品图片高度和宽度一致
function W2H(img) {
    img.height(img.width())
    $(window).resize(function () {
        img.height(img.width())
    })
}