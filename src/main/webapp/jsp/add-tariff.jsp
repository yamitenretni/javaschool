<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new tariff</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <form action="/tariffs/add" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="tariff_name">Tariff name</label>
            <input type="text" class="form-control" id="tariff_name" name="tariff_name" placeholder="Name">
        </div>
        <div class="form-group">
            <label for="monthly_cost">Monthly cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost"
                   placeholder="Montly cost">
        </div>

        <c:forEach items="${options}" var="option">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="selected_options[]"
                           value="${option.id}"> ${option.name} ${option.connectionCost} once + ${option.monthlyCost}
                    every month
                </label>
            </div>
        </c:forEach>
        <button type="submit" class="btn btn-primary">Add tariff</button>
    </form>
</div>
</body>
</html>
