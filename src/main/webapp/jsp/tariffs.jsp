<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Лена
  Date: 15.11.2015
  Time: 18:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariffs List</title>
    <meta charset="utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<form action="/tariffs" method="post" accept-charset="utf-8">
    <div class="form-group">
        <label for="tariff_name">Tariff name</label>
        <input type="text" class="form-control" id="tariff_name" name="tariff_name" placeholder="Name">
    </div>
    <div class="form-group">
        <label for="monthly_cost">Monthly cost</label>
        <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost" placeholder="Montly cost">
    </div>

    <c:forEach items="${options}" var="option">
        <div class="checkbox">
            <label>
                <input type="checkbox" name="selected_options[]" value="${option.id}"> ${option.name} ${option.connectionCost} once + ${option.monthlyCost} every month
            </label>
        </div>
    </c:forEach>
    <a href="/tariffs/add-option" class="btn">Add new option</a>

    <button type="submit" class="btn btn-primary">Add tariff</button>

</form>
<table class="table">
    <caption>Tariffs:</caption>
    <thead>
    <tr>
        <th>Name</th>
        <th>Monthly cost</th>
        <th>Avaliable options</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${tariffs}" var="tariff">
        <tr>
            <td>${tariff.name}</td>
            <td>${tariff.monthlyCost}</td>
            <td>
                <ul>
                    <c:forEach items="${tariff.availableOptions}" var="tariffOpion">
                        <li>${tariffOpion.name}</li>
                    </c:forEach>
                </ul>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
