// 添加产品属性输入框
$('.product-add .attr-title .add').on('click', function () {

    var attr_list = $('.product-add .attr .form-group')

    // 商品属性小于10添加
    if (attr_list.length <= 10) {

        $('.attr').append('<div class="form-group col-6 col-md-4 col-lg-3">\n' +
            '\t\t\t\t\t\t\t\t<div class="input-group">\n' +
            '\t\t\t\t\t\t\t\t\t<input type="text" placeholder="属性" class="form-control text" required>\n' +
            '\t\t\t\t\t\t\t\t\t<input type="number" placeholder="价格" class="form-control price" min="0" required>\n' +
            '\t\t\t\t\t\t\t\t\t<input type="number" placeholder="参考价" class="form-control ref-price" min="0" required>\n' +
            '\t\t\t\t\t\t\t\t</div>\n' +
            '\t\t\t\t\t\t\t</div>')

        // 超出提示
    } else {

        prompt_box('friendly', '商品属性不能超过10个', false)

    }
})

// 移除产品属性输入框
$('.product-add .attr-title .remove').on('click', function () {

    var attr_list = $('.product-add .attr .form-group')
    var attr = $('.product-add .attr .form-group:last-child')

    if (attr_list.length > 1) attr.remove()

})

// 选择缩略图
$('.product-add #images').change(function () {

    // 缩略图不能超过6个
    if ($(this)[0].files.length > 6) prompt_box('friendly', '缩略图不能超过6个', false)

})

// 添加/修改货品
$('.product-goods form .btn-goods').on('click', function () {

    var form = $('.product-goods form')
    var autosend = form.find('#autosend').is(':checked')
    var type = form.find('input[name=type]:checked').val()
    var productID = form.find('#productid').val()
    var goodsElement = $('.goods')
    var goods = []
    var template = editor.html.get()

    // 模板了必须包含货品标签
    if (template.indexOf('{goods}') < 0) {

        prompt_box('ordinary', '发货模板里必须有 {goods} 字样', false)
        return

    }

    if (!type) {

        prompt_box('ordinary', '请选择货品形式', false)
        return

    }

    for (var i = 0; i < goodsElement.length; i++) {

        var attrID = $(goodsElement[i]).find('.attr_id').val()
        var content = $(goodsElement[i]).find('.content').val()

        if (attrID && content) {

            goods.push({
                'attr_id': attrID,
                'content': type == 'list' ? JSON.stringify(content.split('\n')) : content
            })

        }

    }

    // 货品是否为空
    if (goods.length == 0) {

        prompt_box('ordinary', '请输入货品内容', false)
        return
    }

    goods = JSON.stringify(goods)

    $.post({
        url: '/shop/product/goods',
        data: {
            autosend: autosend,
            type: type,
            goods: goods,
            template: template,
            productID: productID
        },
        success: function (data) {
            if (data == 'success') {
                prompt_box('success', '添加成功', "/shop/product/list.html")
            } else if (data == 'fail') {
                prompt_box('fail', '添加货品失败', false)
            } else {
                prompt_box('error', '发生错误', "/")
            }
        }
    })

})

// 添加商品按钮
$('.product-add .btn-addproduct').on('click', function () {
    var title = $('.product-add #title').val().trim()
    var images = $('.product-add #images')[0].files
    var clazz = $('.product-add #class').val().trim()
    var description = editor.html.get()
    var attrs = []

    // 添加属性
    $('.product-add .attr .input-group').each(function (index, obj) {

        var text = $(obj).children('.text').val().trim()
        var price = $(obj).children('.price').val().trim()
        var refprice = $(obj).children('.ref-price').val().trim()

        if (text && price) attrs.push({'attr': text, 'price': price, 'refprice': refprice})

    })

    // 验证所有数据不为空
    if (title && clazz && images.length > 0 && description && attrs.length > 0) {

        // 缩略图不能超过6个
        if (images.length > 6) {

            prompt_box('friendly', '缩略图不能超过6个', false)

        } else {

            // 添加商品请求
            add_product(title, clazz, images, attrs, description)

        }

    } else {

        prompt_box('friendly', '请填写所有内容', false)

    }
})

// 添加商品请求
function add_product(title, clazz, images, attrs, description) {

    // 构造表单
    var formData = new FormData();
    formData.append('title', title)
    formData.append('clazz', clazz)
    formData.append('attrs', JSON.stringify(attrs))
    formData.append('description', description)

    // 添加图片
    for (var i = 0; i < images.length; i++) formData.append('images', images[i])

    // 发送数据
    $.ajax({
        url: '/shop/product/add',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {

            var status
            var text

            switch (data) {

                case 'success':
                    status = 'success'
                    text = '添加商品成功'
                    break

                case 'fail':
                    status = 'fail'
                    text = '添加商品失败'
                    break

                default:
                    status = 'error'
                    text = '未知错误'
                    break

            }

            prompt_box(status, text, '/shop/product/list.html')

        },
        error: function (data) {

            prompt_box('error', '发生错误', false)

        }
    })

}