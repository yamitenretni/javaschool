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
    <h3>What will you choose?</h3>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Monthly cost</th>
            <th>Available options</th>
            <th>
                <c:if test="${compareTariffs.size() > 1}">
                    <a href="/my/tariffs/compare">Compare list</a>
                </c:if>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tariffs}" var="tariff">
            <tr>
                <td>${tariff.name}</td>
                <td>${tariff.monthlyCost}</td>
                <td>
                    <c:forEach items="${tariff.availableOptions}" var="tariffOption">
                        ${tariffOption.name}<br/>
                    </c:forEach>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${compareTariffs.contains(tariff)}">
                            <a href="/my/tariffs/${tariff.id}/cancel">Remove from compare</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/my/tariffs/${tariff.id}/compare">Add to compare</a>
                        </c:otherwise>
                    </c:choose>

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
