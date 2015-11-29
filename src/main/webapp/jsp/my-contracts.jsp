<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome to your personal area</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <h3>Welcome to your personal area</h3>

    <table class="table">
        <caption>Here's all your contracts:</caption>
        <thead>
        <tr>
            <th>Number</th>
            <th>Tariff</th>
            <th>Activated options</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${contracts}" var="contract">
            <tr>
                <td>+${contract.number}</td>
                <td>${contract.tariff.name}</td>
                <td>
                    <ul>
                        <c:forEach items="${contract.activatedOptions}" var="option">
                            <li>${option.name}</li>
                        </c:forEach>
                    </ul>
                </td>
                <td>
                    <a href="/my/contracts/${contract.id}" class="btn">Contract info</a><br/>
                    <a href="/my/contracts/block/${contract.id}" class="btn">Block contract</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
