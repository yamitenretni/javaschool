<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Contract: ${contract.number}</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <p>
        <label>Phone number:</label> +${contract.number}
        <c:if test="${contract.blocked}">
            <code>
                Blocked since
                <fmt:formatDate pattern="dd.MM.yyyy" value="${contract.blockingDate}" />
            </code>
        </c:if></p>

    <p><label>Tariff:</label> ${contract.tariff.name}</p>

    <c:if test="${not contract.blocked}">
        <form action="/cart/${contract.id}/newtariff" method="post" accept-charset="utf-8">
            <div class="form-group">
                <label for="newTariff">You can change tariff</label>
                <select name="newTariff" id="newTariff" class="form-control">
                    <option>-- Select new tariff --</option>
                    <c:forEach items="${tariffs}" var="tariff">
                        <option value="${tariff.id}">${tariff.name}: ${tariff.monthlyCost}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary" name="requestType" value="submit">Change it</button>

        </form>
    </c:if>

    <p><label>Owner:</label> ${contract.client.firstName} ${contract.client.lastName}</p>

    <c:if test="${not empty contract.activatedOptions}">
        <table class="table">
            <caption>Activated options:</caption>
            <thead>
            <tr>
                <th>Name</th>
                <th>Connection cost</th>
                <th>Monthly cost</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contract.activatedOptions}" var="option">
                <tr>
                    <td>${option.name}</td>
                    <td>${option.connectionCost}</td>
                    <td>${option.monthlyCost}</td>
                    <td>
                        <c:if test="${not contract.blocked}">
                            <a href="/cart/${contract.id}/deactivate/${option.id}" class="btn">Deactivate</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${not empty availableOptions and not contract.blocked}">
        <table class="table">
            <caption>You can also activate this options:</caption>
            <thead>
            <tr>
                <th>Name</th>
                <th>Connection cost</th>
                <th>Monthly cost</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${availableOptions}" var="option">
                <tr>
                    <td>${option.name}</td>
                    <td>${option.connectionCost}</td>
                    <td>${option.monthlyCost}</td>
                    <td><a href="/cart/${contract.id}/add/${option.id}" class="btn">Activate</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>
