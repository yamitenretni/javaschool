<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit option ${editedOption.name}</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <form action="/options/edit/${editedOption.id}" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="option_name">Option name</label>
            <c:if test="${errors.contains('notUniqueName')}" >
                <code>must be unique</code>
            </c:if>
            <input type="text" class="form-control" id="option_name" name="option_name" placeholder="Name" value="${editedOption.name}" required>
        </div>
        <div class="form-group">
            <label for="connection_cost">Connection cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="connection_cost" name="connection_cost"
                   placeholder="Connection cost" value="${editedOption.connectionCost}">
        </div>
        <div class="form-group">
            <label for="monthly_cost">Monthly cost</label>
            <input type="number" min="0" step="0.01" class="form-control" id="monthly_cost" name="monthly_cost"
                   placeholder="Monthly cost" value="${editedOption.monthlyCost}">
        </div>

        <p>Select all incompatible options:</p>
        <c:forEach items="${options}" var="option">
            <c:set var="checked" value=""/>
            <c:forEach var="item" items="${editedOption.incompatibleOptions}">
                <c:if test="${item.id eq option.id}">
                    <c:set var="checked" value="checked"/>
                </c:if>
            </c:forEach>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="incompatible_options[]"
                           value="${option.id}" ${checked}> ${option.name} ${option.connectionCost} once
                    + ${option.monthlyCost} every
                    month
                </label>
            </div>
        </c:forEach>

        <p>Select all mandatory options:</p>
        <c:forEach items="${options}" var="option">
            <c:set var="checked" value=""/>
            <c:forEach var="item" items="${editedOption.mandatoryOptions}">
                <c:if test="${item.id eq option.id}">
                    <c:set var="checked" value="checked"/>
                </c:if>
            </c:forEach>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="mandatory_options[]"
                           value="${option.id}" ${checked}> ${option.name} ${option.connectionCost} once
                    + ${option.monthlyCost} every
                    month
                </label>
            </div>
        </c:forEach>
        <p>List of depend option:</p>
        <ul>
            <c:forEach var="option" items="${editedOption.dependOptions}">
                <li>${option.name} ${option.connectionCost} once
                    + ${option.monthlyCost} every
                    month</li>
            </c:forEach>
        </ul>

        <button type="submit" class="btn btn-primary">Save option</button>
    </form>
</div>
</body>
</html>
