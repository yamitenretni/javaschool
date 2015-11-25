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
    <form action="/clients/add/step1" method="post" accept-charset="utf-8">
        <div class="form-group">
            <label for="firstName">First name</label>
            <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First name">
        </div>
        <div class="form-group">
            <label for="lastName">Last name</label>
            <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last name">
        </div>
        <div class="form-group">
            <label for="birthDate">Date of birth</label>
            <input type="date" class="form-control" id="birthDate" name="birthDate" placeholder="Date of birth">
        </div>
        <div class="form-group">
            <label for="passport">Passport</label>
            <input type="text" class="form-control" id="passport" name="passport" placeholder="Passport data">
        </div>
        <div class="form-group">
            <label for="address">Address</label>
            <input type="text" class="form-control" id="address" name="address" placeholder="Address">
        </div>
        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="E-mail">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>

        <button type="submit" class="btn btn-primary" name="requestType" value="submit">Next step</button>
        <button type="submit" class="btn btn-primary" name="requestType" value="cancel">Cancel</button>

    </form>
</div>

</body>
</html>
