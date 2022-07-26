//更换头像
$('#avatarUploader').on('change', function () {

    var file = this.files[0];
    var reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onload = function (ev) {

        $("#avatarImg").attr("src", ev.target.result);

    }

})