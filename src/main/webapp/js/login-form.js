$(document).ready(function() {
    var $loginForm = $(".page__login");
    var $alert = $(".page__alert");

    var loginAction = $loginForm.action;

    $loginForm.submit(function(event) {
        var data = $loginForm.serialize();
        var login = $("#login").val();

        $.post(loginAction, data, function(loginStatus) {
            if (loginStatus === "success") {
                $alert.text("Hello, " + login + "!")
                    .removeClass("alert-danger")
                    .addClass("alert-success")
                    .show();
            }
            else {
                $alert.text("Invalid login/password")
                    .removeClass("alert-success")
                    .addClass("alert-danger")
                    .show();
            }
        });

        return false;
    });
});