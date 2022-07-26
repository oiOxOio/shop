//		水平滚动
var outDiv = document.getElementsByClassName("header1")[0].getElementsByTagName("div")[0];
outDiv.onwheel = function (event) {
    //禁止事件默认行为（此处禁止鼠标滚轮行为关联到"屏幕滚动条上下移动"行为）
    event.preventDefault();
    //设置鼠标滚轮滚动时屏幕滚动条的移动步长
    var step = 24;
    if (event.deltaY < 0) {
        //向上滚动鼠标滚轮，屏幕滚动条左移
        this.scrollLeft -= step;
    } else {
        //向下滚动鼠标滚轮，屏幕滚动条右移
        this.scrollLeft += step;
    }
}
//按钮点击,展开与关闭
var but = document.getElementsByClassName("but")[0];
var chatclose = document.getElementsByClassName("chat-body-close")[0];
var zhuangtai = 1;
but.onclick = function () {
    if (zhuangtai == 1) {
        chatclose.className = "chat-body-open";
        zhuangtai = 2;
    } else {
        chatclose.className = "chat-body-close";
        zhuangtai = 1;
    }
}