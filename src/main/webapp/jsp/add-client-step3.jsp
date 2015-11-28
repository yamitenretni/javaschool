<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <h3>Check this data and confirm saving:</h3>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Client's data</h3>
        </div>
        <div class="panel-body">
            <p><label>First name:</label> ${newClient.firstName}</p>
            <p><label>Last name:</label> ${newClient.lastName}</p>
            <p><label>Date of birth:</label> <fmt:formatDate pattern="dd.MM.yyyy" value="${newClient.birthDate}" /></p>
            <p><label>Passport data:</label> ${newClient.passportData}</p>
            <p><label>Address:</label> ${newClient.address}</p>
            <p><label>E-mail:</label> ${newClient.user.login}</p>
        </div>
        <div class="panel-heading">
            <h3 class="panel-title">Contract's data</h3>
        </div>
        <div class="panel-body">
            <p><label>Phone number:</label> +${newContract.number}</p>

            <p><label>Tariff:</label> ${newContract.tariff.name}</p>

            <p><label>Activated options:</label></p>
            <ul>
                <c:forEach items="${newContract.activatedOptions}" var="option">
                    <li>${option.name}</li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <form action="/clients/add/step3" method="post">
        <button type="submit" class="btn btn-primary" name="requestType" value="submit">Confirm</button>
    </form>
</div>
</body>
</html>
