<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new option</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <form action="/options/add" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="option_name">Option name</label>
            <c:if test="${errors.contains('notUniqueName')}" >
                <code>must be unique</code>
            </c:if>
            <input type="text" class="form-control" id="option_name" name="option_name" placeholder="Name" value="${name}" required>
        </div>
        <div class="form-group">
            <label for="connection_cost">Connection cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="connection_cost" name="connection_cost"
                   placeholder="Connection cost" value="${connectionCost}">
        </div>
        <div class="form-group">
            <label for="monthly_cost">Monthly cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost"
                   placeholder="Monthly cost" value="${monthlyCost}">
        </div>

        <button type="submit" class="btn btn-primary">Add option</button>
        <a href="/options" class="btn">Back to options</a>

    </form>
</div>
</body>
</html>
