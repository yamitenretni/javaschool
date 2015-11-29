<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page center-block">
    <c:if test="${errors.contains('noSuchUser')}">
        <div class="alert alert-danger" role="alert">Invalid login/password</div>
    </c:if>

    <form class="page__login" action="/login" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="login">Login</label>
            <input type="text" class="form-control" id="login" name="login" placeholder="Login">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>
        <input type="hidden" name="refpath" value="${refpath}">
        <button type="submit" class="btn btn-primary">Login</button>
    </form>
</div>
</body>
</html>