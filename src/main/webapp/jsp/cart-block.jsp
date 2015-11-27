<h3>Your cart</h3>

<c:forEach items="${cartForm.cartContractForms}" var="cartPosition">
    <h4>+${cartPosition.contract.number}</h4>
    <table class="table">
        <caption>Deactivated options:</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Connection cost</th>
            <th>Monthly cost</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${cartPosition.deactivatedOptions}" var="option">
            <tr>
                <td>${option.name}</td>
                <td>${option.connectionCost}</td>
                <td>${option.monthlyCost}</td>
                <td><a href="/cart/${cartPosition.contract.id}/deactivate/${option.id}/cancel">Cancel</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</c:forEach>