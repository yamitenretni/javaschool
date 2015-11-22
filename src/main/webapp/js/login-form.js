$(document).ready(function() {
    var $loginForm = $(".page__login");
    var $alert = $(".page__alert");

    var loginAction = $loginForm.action;

    $loginForm.submit(function(event) {
        var data = $loginForm.serialize();
        var login = $("#login").val();
        var password = $("#password").val();

        var re = new RegExp("^[a-zA-Z0-9]+$");

        if(login.length < 3 || login.length > 30) {
            $alert.text("Login must be between 3 and 30 symbols!")
                .removeClass("alert-success")
                .addClass("alert-danger")
                .show();
        }
        else if(!(re.test(login))) {
            $alert.text("Login must contain letters and numbers only!")
                .removeClass("alert-success")
                .addClass("alert-danger")
                .show();
        }
        else if(password.length < 3 || password.length > 30) {
            $alert.text("Password must be between 3 and 30 symbols!")
                .removeClass("alert-success")
                .addClass("alert-danger")
                .show();
        }
        else if(!(re.test(password))) {
            $alert.text("Password must contain letters and numbers only!")
                .removeClass("alert-success")
                .addClass("alert-danger")
                .show();
        }
        else {
            $.post(loginAction, data, function (loginStatus) {
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
        }

        return false;
    });
});