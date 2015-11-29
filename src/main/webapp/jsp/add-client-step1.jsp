<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new client and contract</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <form action="/clients/add/step1" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="firstName">First name</label>
            <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First name" value="${firstName}" required>
        </div>
        <div class="form-group">
            <label for="lastName">Last name</label>
            <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last name" value="${lastName}" required>
        </div>
        <div class="form-group">
            <label for="birthDate">Date of birth</label>
            <input type="date" class="form-control" id="birthDate" name="birthDate" placeholder="Date of birth" value="${birthDate}" required>
        </div>
        <div class="form-group">
            <label for="passport">Passport</label>
            <c:if test="${errors.contains('notUniquePassport')}" >
                <code>must be unique</code>
            </c:if>
            <input type="text" class="form-control" id="passport" name="passport" placeholder="Passport data" value="${passport}" required>
        </div>
        <div class="form-group">
            <label for="address">Address</label>
            <input type="text" class="form-control" id="address" name="address" placeholder="Address" value="${address}" required>
        </div>
        <div class="form-group">
            <label for="email">E-mail</label>
            <c:if test="${errors.contains('notUniqueLogin')}" >
                <code>must be unique</code>
            </c:if>
            <input type="email" class="form-control" id="email" name="email" placeholder="E-mail" value="${email}" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
        </div>

        <button type="submit" class="btn btn-primary" name="requestType" value="submit">Next step</button>

    </form>
</div>

</body>
</html>
