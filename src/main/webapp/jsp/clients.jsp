<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <h3>Client list</h3>

    <table class="table">
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
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>
