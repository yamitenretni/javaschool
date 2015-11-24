<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariffs List</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <a href="/tariffs/add" class="btn btn-primary">Add Tariff</a>
    <a href="/options" class="btn">Manage Options</a>
    <table class="table">
        <caption>All active tariffs:</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Monthly cost</th>
            <th>Avaliable options</th>
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
                        <c:forEach items="${tariff.availableOptions}" var="tariffOpion">
                            <li>${tariffOpion.name}</li>
                        </c:forEach>
                    </ul>
                </td>
                <td><a href="/tariffs/edit/${tariff.id}">Edit</a></td>
                <td><a href="/tariffs/delete/${tariff.id}">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
