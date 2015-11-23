<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new option here</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <form action="/tariffs/add-option" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="option_name">Option name</label>
            <input type="text" class="form-control" id="option_name" name="option_name" placeholder="Name">
        </div>
        <div class="form-group">
            <label for="connection_cost">Connection cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="connection_cost" name="connection_cost"
                   placeholder="Connection cost">
        </div>
        <div class="form-group">
            <label for="monthly_cost">Monthly cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost"
                   placeholder="Monthly cost">
        </div>

        <button type="submit" class="btn btn-primary">Add option</button>
        <a href="/tariffs" class="btn">Back to tariffs</a>

    </form>
    <table class="table">
        <caption>All Options:</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Connection cost</th>
            <th>Monthly cost</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${options}" var="option">
            <tr>
                <td>${option.name}</td>
                <td>${option.connectionCost}</td>
                <td>${option.monthlyCost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
