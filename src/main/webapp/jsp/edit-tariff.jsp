<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit tariff ${editedTariff.name}</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <form action="/tariffs/edit/${editedTariff.id}" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="tariff_name">Tariff name</label>
            <c:if test="${errors.contains('notUniqueName')}" >
                <code>must be unique</code>
            </c:if>
            <input type="text" class="form-control" id="tariff_name" name="tariff_name" placeholder="Name"
                   value="${editedTariff.name}" required>
        </div>
        <div class="form-group">
            <label for="monthly_cost">Monthly cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost"
                   placeholder="Monthly cost" value="${editedTariff.monthlyCost}">
        </div>

        <c:forEach items="${options}" var="option">
            <c:set var="checked" value=""/>
            <c:forEach var="item" items="${editedTariff.availableOptions}">
                <c:if test="${item.id eq option.id}">
                    <c:set var="checked" value="checked"/>
                </c:if>
            </c:forEach>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="selected_options[]"
                           value="${option.id}" ${checked}> ${option.name} ${option.connectionCost} once
                    + ${option.monthlyCost} every
                    month
                </label>
            </div>
        </c:forEach>

        <button type="submit" class="btn btn-primary">Save tariff</button>

    </form>
</div>
</body>
</html>
