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
    <div class="clearfix">
        <h3 class="pull-left contract-title">Phone number: +${contract.number}
            <c:if test="${contract.blocked}">
                <code>
                    Blocked since
                    <fmt:formatDate pattern="dd.MM.yyyy" value="${contract.blockingDate}"/>
                </code>
            </c:if>
        </h3>

        <c:if test="${not client.blocked}">
            <div class="btn-group pull-right" role="group" aria-label="...">
                <c:choose>
                    <c:when test="${not contract.blocked and currentUser.roleName == 'EMPLOYEE'}">
                        <a href="/contracts/${contract.id}/block" class="btn btn-danger" role="button">Block
                            contract</a>
                    </c:when>
                    <c:when test="${not contract.blocked and currentUser.roleName == 'CLIENT'}">
                        <a href="/my/contracts/${contract.id}/block" class="btn btn-danger" role="button">Block
                            contract</a>
                    </c:when>
                    <c:when test="${contract.blocked and currentUser.roleName == 'CLIENT' and contract.blockingUser.id == currentUser.id}">
                        <a href="/my/contracts/${contract.id}/unlock" class="btn btn-success">Unlock contract</a>
                    </c:when>
                    <c:when test="${contract.blocked and currentUser.roleName == 'EMPLOYEE'}">
                        <a href="/contracts/${contract.id}/unlock" class="btn btn-success">Unlock contract</a>
                    </c:when>
                </c:choose>
            </div>
        </c:if>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                <label>Owner:</label> <a
                    href="/clients/${contract.client.id}">${contract.client.firstName} ${contract.client.lastName}</a>
            </h3>
        </div>
        <div class="panel-body">
            Current tariff and options:
        </div>
        <table class="table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Monthly cost</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr class="info">
                <td><label>Tariff:</label> ${contract.tariff.name}</td>
                <td>${contract.tariff.monthlyCost}</td>
                <td></td>
            </tr>
            <c:if test="${not empty contract.activatedOptions}">

                <c:forEach items="${contract.activatedOptions}" var="option">
                    <tr>
                        <td>${option.name}</td>
                        <td>${option.monthlyCost}</td>
                        <td>
                            <c:choose>
                                <c:when test="${currentCartPosition.deactivatedOptions.contains(option) or currentCartPosition.dependingOptions.contains(option) or currentCartPosition.unsupportedOptions.contains(option)}">
                                    Deactivated
                                </c:when>
                                <c:when test="${not contract.blocked}">
                                    <a href="/cart/${contract.id}/deactivate/${option.id}">Deactivate</a>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <tr>
                <td><label>Total:</label></td>
                <td><fmt:setLocale value="en_US"/><fmt:formatNumber value="${contract.totalMonthlyCost}" type="number"
                                                                    maxFractionDigits="2"/></td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div>

    <c:if test="${empty availableOptions and empty incompatibleOptions}">
        <p>All available options for selected tariff are activated yet.</p>

        <p></p>
    </c:if>

    <c:if test="${not contract.blocked}">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">You can change tariff and activate new options</h3>
            </div>
            <div class="panel-body">
                <form action="/cart/${contract.id}/newtariff" method="get" accept-charset="utf-8">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="input-group">
                                <select name="newTariff" id="newTariff" class="form-control" required>
                                    <option value="">-- Select new tariff --</option>
                                    <c:forEach items="${tariffs}" var="tariff">
                                        <option value="${tariff.id}">${tariff.name}: ${tariff.monthlyCost} every month
                                        </option>
                                    </c:forEach>
                                </select>
                                <span class="input-group-btn">
                                    <button type="submit" class="btn btn-default" name="requestType" value="submit">
                                        Change
                                    </button>
                                </span>
                            </div>
                        </div>
                        <c:if test="${currentUser.roleName == 'CLIENT'}">
                            <a href="/my/tariffs" class="btn btn-default"> Learn more about tariffs</a>
                        </c:if>
                    </div>
                </form>
            </div>

            <c:if test="${not contract.blocked and (not empty availableOptions or not empty incompatibleOptions or not empty dependingOptions)}">
                <table class="table">
                    <caption>Available options for selected tariff:</caption>
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
                            <td><a href="/cart/${contract.id}/add/${option.id}">Activate</a></td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${incompatibleOptions}" var="entry">
                        <tr style="color: #ccc;">
                            <td>${entry.key.name}
                            </td>
                            <td>${entry.key.connectionCost}</td>
                            <td>${entry.key.monthlyCost}</td>
                            <td>Incompatible with: <br/>
                                <c:forEach items="${entry.value}" var="incOption">
                                    ${incOption.name} <br/>
                                </c:forEach></td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${dependOptions}" var="entry">
                        <tr style="color: #ccc;">
                            <td>${entry.key.name}
                            </td>
                            <td>${entry.key.connectionCost}</td>
                            <td>${entry.key.monthlyCost}</td>
                            <td>Depend on: <br/>
                                <c:forEach items="${entry.value}" var="incOption">
                                    ${incOption.name} <br/>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </c:if>
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>
