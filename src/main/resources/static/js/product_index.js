var product_id = $('#orders .product_id')
var attr_id = $('#orders .attr_id')
var attrs = $('.attr .btn-attr')
var number = $('.input-add-sub input')

// 选择商品属性
attrs.on('click', function () {

    // 如果已经选中
    if ($(this).hasClass('active')) {

        // 删除选中样式
        $(this).removeClass('active')

        // 修改价格
        $('.price span').text($(attrs[0]).attr('price'))

        // 修改表单
        attr_id.val('')

        // 如果未选中
    } else {

        // 添加选中样式
        attrs.removeClass('active')
        $(this).addClass('active')

        // 修改价格
        $('.price span').text($(this).attr('price'))

        // 修改表单
        attr_id.val($(this).attr('value'))

    }
})

// 添加收藏
$('.btn-addwishlist').on('click', function () {

    if (product_id.val()) {
        var attrID = attr_id.val() ? attr_id.val() : $('.details .attr button:first-child').attr('value');
        addWishlist(product_id.val(), attrID)
    } else {
        prompt_box('error', '发生错误', false)
    }

})

// 添加购物车
$('.btn-addcart').on('click', function () {

    var title = $('.title').text();
    var attr = $('.details .attr .active').text()
    var attrID = attr_id.val()
    var price = $('.price span').text();
    var img = $('.product-carousel .carousel-item img')[0].src;
    var numbers = number.val()

    // 是否选择商品属性
    if (!attr) {
        attr = $('.details .attr button:first-child').text()
        attrID = $('.details .attr button:first-child').attr('value')
    }

    addCart(product_id.val(), title, attr, price, img, numbers, attrID)

})

//输入框验证
number.on('input propertychange', function () {

    var text = number.val()
    if (text == "") {
        number.val(1)
        return
    } else if (isNaN(text)) {
        number.val(1)
        return
    } else if (text > 99999) {
        number.val(99999)
        return
    } else if (text < 1) {
        number.val(1)
        return
    }

})

// 商品数量减少不能小于1
$(".input-add-sub #button-addon1").on('click', function () {

    var text = $('.input-add-sub input').val()
    if (text > 1) text--
    $('.input-add-sub input').val(text)

})

// 商品数量增加不能大于99999
$(".input-add-sub #button-addon2").on('click', function () {

    var text = $('.input-add-sub input').val()
    if (text < 99999) text++
    $('.input-add-sub input').val(text)

})

// 购买商品
$('.product-index .btn-buy').on('click', function () {

    var form = $('#product-buy')
    var products = [{'product': product_id.val(), 'attr': attr_id.val(), 'number': number.val()}]
    form.find('input').val(JSON.stringify(products))
    form.submit()

})
