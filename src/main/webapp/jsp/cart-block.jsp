<c:if test="${not empty cartForm.cartContractForms}">
    <h2>Your unsaved changes:</h2>

    <c:forEach items="${cartForm.cartContractForms}" var="cartPosition">
        <h3>For the number +${cartPosition.contract.number}</h3>

        <c:if test="${cartPosition.newTariff != null}">
            <div class="panel panel-info">
                <div class="panel-heading">Tariff will change on ${cartPosition.newTariff.name} <a href="/cart/${contract.id}/newtariff/cancel">Cancel</a></div>
            </div>
        </c:if>
        <c:if test="${not empty cartPosition.newOptions}">
            <div class="panel panel-success">
                <div class="panel-heading">Will be added:</div>
                <table class="table">
                    <caption></caption>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Connection cost</th>
                        <th>Monthly cost</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cartPosition.newOptions}" var="option">
                        <tr>
                            <td>${option.name}</td>
                            <td>${option.connectionCost}</td>
                            <td>${option.monthlyCost}</td>
                            <td><a href="/cart/${cartPosition.contract.id}/add/${option.id}/cancel">Cancel</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${not empty cartPosition.deactivatedOptions}">
            <div class="panel panel-danger">
                <div class="panel-heading">Will be deactivated:</div>
                <table class="table">
                    <caption></caption>
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
                            <td><a href="/cart/${cartPosition.contract.id}/deactivate/${option.id}/cancel">Cancel</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <a href="/cart/${cartPosition.contract.id}/save" class="btn btn-primary">Save changes</a>
    </c:forEach>
</c:if>