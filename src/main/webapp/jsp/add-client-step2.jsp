<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <form action="/clients/add/step2" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="contractNumber">Phone number</label>

            <div class="input-group">
                <span class="input-group-addon" id="basic-addon1">+</span>
                <input type="text" class="form-control" id="contractNumber" name="contractNumber"
                       placeholder="Phone number">
            </div>
        </div>
        <div class="form-group">
            <label for="contractTariff">Tariff</label>
            <select name="contractTariff" id="contractTariff" class="form-control">
                <option>-- Select tariff --</option>
                <c:forEach items="${tariffs}" var="tariff">
                    <option value="${tariff.id}">${tariff.name}: ${tariff.monthlyCost}</option>
                </c:forEach>
            </select>
        </div>

        <div id="availableOptions"></div>

        <button type="submit" class="btn btn-primary" name="requestType" value="submit">Next step</button>

    </form>
</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="/js/contract.js"></script>
</body>
</html>
