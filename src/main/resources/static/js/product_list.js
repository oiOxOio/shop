// 排序选项
$('.sort button').on('click', function () {

    $('#s').val($(this).val())
    $('#filters').submit()

})

// 价格筛选
$('.btn-price').on('click', function () {

    var max_price = $('#max-price').val()
    var min_price = $('#min-price').val()

    if (max_price >= min_price) {
        $('#p').val(min_price + '-' + max_price)
        $('#filters').submit()
    }

})

//添加收藏
$('.shops .wish').on('click', function () {

    var item = $(this).parents('.box')
    var attrID = item.find('.cart').attr('value')

    addWishlist($(this).attr('value'), attrID)

})

//添加购物车
$('.shops .cart').on('click', function () {

    var item = $(this).parents('.box')
    var title = item.find('.title').text()
    var productid = item.find('.wish').attr('value')
    var attr = $(this).attr('attr')
    var attrid = $(this).attr('value')
    var price = item.find('.price span').text();
    var img = item.find('img').attr('src');

    addCart(productid, title, attr, price, img, 1, attrid)

})

// 商品图片高度和宽度一致
W2H($('.shops .box img'));