<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Client List</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>

    <div class="btn-group" role="group" aria-label="...">
        <a href="/clients/add/step1" class="btn btn-primary" role="button">Add Client</a>
    </div>


    <table class="table">
        <caption>All clients:</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Contracts</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${clients}" var="client">
            <tr>
                <td><a href="/clients/${client.id}">${client.firstName} ${client.lastName}</a>
                    <c:if test="${client.blocked}">
                        <code>Blocked</code>
                    </c:if></td>
                <td>
                    <c:forEach items="${client.contracts}" var="contract">
                        <a href="/contracts/${contract.id}">+${contract.number}</a>
                        <c:if test="${client.blocked}">
                            <code>Blocked</code>
                        </c:if> <br/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
