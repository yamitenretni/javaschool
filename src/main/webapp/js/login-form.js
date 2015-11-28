$(document).ready(function () {
    var $loginForm = $(".page__login");
    var $alert = $(".page__alert");

    var loginAction = $loginForm.action;
    var refpath = $("#refpath").val();

    $loginForm.submit(function (event) {
        var data = $loginForm.serialize();
        var login = $("#login").val();
        var password = $("#password").val();

        $.post(loginAction, data, function (loginStatus) {
            if (loginStatus === "success") {
                window.location = refpath;
            }
            else {
                $alert.text("Invalid login/password")
                    .removeClass("alert-success")
                    .addClass("alert-danger")
                    .show();

                return false;
            }
        });
    });
});