<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new client and contract</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/page.css">
</head>
<body>
<div class="page">
    <%@include file="/jsp/nav-bar.jsp" %>
    <h3>Add contract for ${newClient.firstName} ${newClient.lastName}</h3>
    <form action="/clients/add/step2" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="contractNumber">Phone number</label>
            <c:if test="${errors.contains('notUniqueNumber')}">
                <code>must be unique</code>
            </c:if>
            <div class="input-group">
                <span class="input-group-addon" id="basic-addon1">+</span>
                <input type="text" class="form-control" id="contractNumber" name="contractNumber"
                       placeholder="Phone number" value="${number}" required>
            </div>
        </div>
        <div class="form-group">
            <label for="contractTariff">Tariff</label>
            <select name="contractTariff" id="contractTariff" class="form-control" required>
                <option value="">-- Select tariff --</option>
                <c:forEach items="${tariffs}" var="tariff">
                    <option value="${tariff.id}"
                            <c:if test="${tariff.id == selectedTariff.id}">selected</c:if> >${tariff.name}: ${tariff.monthlyCost}</option>
                </c:forEach>
            </select>
        </div>

        <div id="availableOptions">
            <c:forEach items="${selectedTariff.availableOptions}" var="option">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="selectedOptions[]" value="${option.id}"
                               <c:if test="${savedOptions.contains(option.id)}">checked</c:if>>
                        <span>${option.name} ${option.connectionCost} once + ${option.monthlyCost} every month</span>
                    </label>
                </div>
            </c:forEach>
        </div>

        <a href="/clients/add/step1" class="btn btn-default">Previous step</a>
        <button type="submit" class="btn btn-primary" name="requestType" value="submit">Next step</button>
        <a href="/clients/add/cancel" class="btn btn-danger">Clear all data</a>
    </form>
    <%@include file="/jsp/cart-block.jsp" %>
</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="/js/contract.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>
