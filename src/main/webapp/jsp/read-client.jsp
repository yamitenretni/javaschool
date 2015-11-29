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

    <table class="table">
        <caption>Contracts:</caption>
        <thead>
        <tr>
            <th>Number</th>
            <th>Tariff</th>
            <th>Options</th>
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
                    <ul>
                        <c:forEach items="${contract.activatedOptions}" var="option">
                            <li>${option.name}</li>
                        </c:forEach>
                    </ul>
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
    <c:choose>
        <c:when test="${not client.blocked}">
            <a href="/clients/${client.id}/block" class="btn btn-danger">Block client</a>
        </c:when>
        <c:otherwise>
                <a href="/clients/${client.id}/unlock" class="btn btn-success">Unlock client</a>
        </c:otherwise>
    </c:choose>

    <a href="/clients" class="btn">Back to clients</a>

</div>
</body>
</html>
