<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariff List</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <h3>Active tariffs: </h3>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Monthly cost</th>
            <th>Available options</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tariffs}" var="tariff">
            <tr>
                <td>${tariff.name}</td>
                <td>${tariff.monthlyCost}</td>
                <td>
                    <ul>
                        <c:forEach items="${tariff.availableOptions}" var="tariffOption">
                            <li>${tariffOption.name}</li>
                        </c:forEach>
                    </ul>
                </td>
                <td><a href="/tariffs/edit/${tariff.id}">Edit</a></td>
                <td><a href="/tariffs/delete/${tariff.id}">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>
