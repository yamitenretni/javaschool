<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariff ${tariff.name}</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <h3>Compare list</h3>
    <table class="table compare-table">
        <thead>
        <tr>
            <th></th>
            <c:forEach items="${compareTariffs}" var="tariff">
                <th>${tariff.name}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Monthly cost</td>
            <c:forEach items="${compareTariffs}" var="tariff">
                <td align="center">${tariff.monthlyCost}</td>
            </c:forEach>
        </tr>
        <c:forEach items="${allOptions}" var="option">
            <tr>
                <td>${option.name}</td>
                <c:forEach items="${compareTariffs}" var="tariff">
                    <td align="center">
                        <c:choose>
                            <c:when test="${tariff.availableOptions.contains(option)}">
                                <span class="check glyphicon glyphicon-ok" aria-hidden="true"></span>
                            </c:when>
                            <c:otherwise>
                                <span class="cross glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:choose>
        <c:when test="${not empty currentClient.activeContracts}">
        <tr>
            <td></td>
            <c:forEach items="${compareTariffs}" var="tariff">
                <c:choose>
                    <c:when test="${currentClient.activeContracts.size() > 1}">
                        <td align="center">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    Activate <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <c:forEach items="${currentClient.activeContracts}" var="contract">
                                        <li>
                                            <a href="/cart/${contract.id}/newtariff?newTariff=${tariff.id}">+${contract.number}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td align="center">
                            <c:forEach items="${currentClient.activeContracts}" var="contract">
                                <a href="/cart/${contract.id}/newtariff?newTariff=${tariff.id}" class="btn btn-primary">Activate</a>
                            </c:forEach>
                        </td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
        </tbody>
    </table>
    </c:when>
    <c:otherwise>
        </tbody>
        </table>
        <h4>Sorry, you can't activate this tariffs because all your contracts are blocked.</h4>
    </c:otherwise>
    </c:choose>
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>