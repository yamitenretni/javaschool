<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Options list</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <a href="/options/add" class="btn btn-primary">Add Option</a>
    <a href="/tariffs" class="btn">Manage Tariffs</a>
    <table class="table">
        <caption>All active options:</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Connection cost</th>
            <th>Monthly cost</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${options}" var="option">
            <tr>
                <td>${option.name}</td>
                <td>${option.connectionCost}</td>
                <td>${option.monthlyCost}</td>
                <td><a href="/options/edit/${option.id}">Edit</a></td>
                <td><a href="/options/delete/${option.id}">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
