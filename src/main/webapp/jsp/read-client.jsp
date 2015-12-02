<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Client: ${client.firstName} ${client.lastName}</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <h3>Personal information:</h3>
    <p><label>Name:</label> ${client.firstName} ${client.lastName}
        <c:if test="${client.blocked}">
            <code>Blocked since
                <fmt:formatDate pattern="dd.MM.yyyy" value="${client.blockingDate}"/>
            </code>
        </c:if>
    </p>

    <p><label>Date of birth:</label> <fmt:formatDate pattern="dd.MM.yyyy"
                                                     value="${client.birthDate}"/></p>

    <p><label>Passport data:</label> ${client.passportData}</p>

    <p><label>Address:</label> ${client.address}</p>

    <p><label>E-mail:</label> ${client.user.login}</p>

    <h3>Contracts:</h3>
    <table class="table">
        <thead>
        <tr>
            <th>Number</th>
            <th>Tariff</th>
            <th>Active Options</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${client.contracts}" var="contract">
            <tr>
                <td>
                    +${contract.number}
                    <c:if test="${contract.blocked}">
                        <br/>
                        <code>
                            Blocked since
                            <fmt:formatDate pattern="dd.MM.yyyy" value="${contract.blockingDate}"/>
                        </code>
                    </c:if>
                </td>
                <td>${contract.tariff.name}</td>
                <td>
                        <c:forEach items="${contract.activatedOptions}" var="option">
                            ${option.name}<br />
                        </c:forEach>
                </td>
                <td>
                    <a href="/contracts/${contract.id}" class="btn">Contract info</a><br/>
                    <c:choose>
                        <c:when test="${not contract.blocked}">
                            <a href="/contracts/${contract.id}/block" class="btn">Block contract</a>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not client.blocked}">
                                <a href="/contracts/${contract.id}/unlock" class="btn">Unlock contract</a>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="btn-toolbar" role="toolbar" aria-label="...">
        <div class="btn-group" role="group" aria-label="...">
            <a href="/clients/${client.id}/add-contract" class="btn btn-default" role="button">Add contract</a>
            <c:choose>
                <c:when test="${not client.blocked}">
                    <a href="/clients/${client.id}/block" class="btn btn-default" role="button">Block client</a>
                </c:when>
                <c:otherwise>
                    <a href="/clients/${client.id}/unlock" class="btn btn-default" role="button">Unlock client</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="btn-group" role="group" aria-label="...">
            <a href="/clients" class="btn btn-default" role="button">Back to clients</a>
        </div>
    </div>


</div>
</body>
</html>
