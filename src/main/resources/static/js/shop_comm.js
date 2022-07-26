//点击展开按钮后导航栏展开
var flag = false;
var topsvg = $("#topbar svg");

$("#mobile-menu .nav>li>a>span").fadeTo(10, 0)
$("#topbar").click(function () {
    var width = document.body.clientWidth;
    var mobileMenu = $("#mobile-menu");
    if (mobileMenu.hasClass("show-nav")) {
        $(".right-main").removeClass("right-main-show");
        $("#topbar").addClass("hide-btn").removeClass("show-btn");
        $("#topbar a").addClass("d-none");
        $("#mobile-menu .nav>li>a>span").fadeTo(0, 0);
        mobileMenu.addClass("hide-nav").removeClass("show-nav");
        setTimeout(function () {
            $(".nav-item div").removeClass("show");
            $("#collapseExample").removeClass("show");
        }, 150);
        flag = false
    } else {
        flag = true;
        $(".right-main").addClass("right-main-show");
        $("#topbar").addClass("show-btn").removeClass("hide-btn");
        $("#topbar a").removeClass("d-none");
        mobileMenu.addClass("show-nav").removeClass("hide-nav");
        $("#mobile-menu .nav>li>a>span").fadeTo(300, 1);
    }
})

//鼠标放到导航栏自动展开
$("#mobile-menu").hover(function () {
    var width = document.body.clientWidth;
    var mobileMenu = $("#mobile-menu");
//	if(width>991){
    if (mobileMenu.hasClass("show-nav") && !flag) {
        $(".right-main").removeClass("right-main-show");
        $("#topbar").addClass("hide-btn").removeClass("show-btn");
        $("#topbar a").addClass("d-none");
        $("#mobile-menu .nav>li>a>span").fadeTo(0, 0);
        mobileMenu.addClass("hide-nav").removeClass("show-nav");
        setTimeout(function () {
            $(".nav-item div").removeClass("show");
            $("#collapseExample").removeClass("show");
        }, 150);
    } else {
        $(".right-main").addClass("right-main-show");
        $("#topbar").addClass("show-btn").removeClass("hide-btn");
        $("#topbar a").removeClass("d-none");
        mobileMenu.addClass("show-nav").removeClass("hide-nav");
        $("#mobile-menu .nav>li>a>span").fadeTo(300, 1);
    }
//	}
})

//窗口大小改变自动收回导航栏
window.onresize = function () {
    var width = document.body.clientWidth;
    if (width < 991) {
        var mobileMenu = $("#mobile-menu");
        if (mobileMenu.hasClass("show-nav")) {
            $("#topbar a").addClass("d-none");
            $(".nav-item div").removeClass("show");
            $("#collapseExample").removeClass("show");
            $(".right-main").removeClass("right-main-show");
            $("#topbar").addClass("hide-btn").removeClass("show-btn");
            $("#mobile-menu .nav>li>a>span").fadeTo(300, 1);
            mobileMenu.addClass("hide-nav").removeClass("show-nav");
            flag = false;
        }
    }
}

$('.custom-file-input').on('change', function () {
    $(".custom-file-label").html(this.files);
})