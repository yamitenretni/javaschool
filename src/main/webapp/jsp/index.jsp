<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page center-block">
    <div class="page__alert alert" role="alert"></div>

    <form class="page__login" action="/s" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="login">Login</label>
            <input type="text" class="form-control" id="login" name="login" placeholder="Login">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
    </form>

    <table class="page__users table">
        <caption>Users:</caption>
        <thead>
            <tr>
                <th>Login</th>
                <th>Password</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.login}</td>
                    <td>${user.password}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<script src="http://code.jquery.com/jquery-latest.js" ></script>
<script src="/js/login-form.js" ></script>
</body>
</html>