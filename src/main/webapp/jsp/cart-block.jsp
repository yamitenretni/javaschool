<c:out value="${cartForm.cartContractForms}"></c:out>
<c:if test="${not empty sessionScope.cartForm.cartContractForms}">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Your unsaved changes:</h3>
        </div>
        <div class="panel-body">


            <ul class="nav nav-tabs">
                <c:forEach items="${cartForm.cartContractForms}" var="cartPosition" varStatus="positionLoop">
                    <li role="presentation" <c:if test="${positionLoop.index == 0}">class="active"</c:if>>
                        <a href="#${cartPosition.contract.number}"
                           data-toggle="tab">+${cartPosition.contract.number}</a>
                    </li>
                </c:forEach>
            </ul>
            <div id="myTabContent" class="tab-content">
                <c:forEach items="${cartForm.cartContractForms}" var="cartPosition" varStatus="positionLoop">
                    <div role="tabpanel"
                            <c:choose>
                                <c:when test="${positionLoop.index == 0}">
                                    class="tab-pane fade active in"
                                </c:when>
                                <c:otherwise>
                                    class="tab-pane fade"
                                </c:otherwise>
                            </c:choose>
                         id="${cartPosition.contract.number}"
                         aria-labelledby="${cartPosition.contract.number}-tab">

                        <table class="table table-condensed cart-table">
                            <tbody>
                            <c:if test="${cartPosition.newTariff != null}">
                                <tr>
                                    <td><span class="label label-warning">Changed</span></td>
                                    <td>${cartPosition.contract.tariff.name} &rightarrow; ${cartPosition.newTariff.name}(${cartPosition.newTariff.monthlyCost}
                                        every month)
                                    </td>
                                    <td><a href="/cart/${cartPosition.contract.id}/newtariff/cancel">Cancel</a></td>
                                </tr>
                            </c:if>
                            <c:forEach items="${cartPosition.unsupportedOptions}" var="option">
                                <tr>
                                    <td><span class="label label-danger">Deleted</span></td>
                                    <td>${option.name} (${option.connectionCost} once + ${option.monthlyCost} every
                                        month)
                                    </td>
                                    <td>Not available for tariff
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:forEach items="${cartPosition.newOptions}" var="option">
                                <tr>
                                    <td><span class="label label-success">New</span></td>
                                    <td>${option.name} (${option.connectionCost} once + ${option.monthlyCost} every
                                        month)
                                    </td>
                                    <td><a href="/cart/${cartPosition.contract.id}/add/${option.id}/cancel">Cancel</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:forEach items="${cartPosition.deactivatedOptions}" var="option">
                                <tr>
                                    <td><span class="label label-danger">Deleted</span></td>
                                    <td>${option.name} (${option.connectionCost} once + ${option.monthlyCost} every
                                        month)
                                    </td>
                                    <td><a href="/cart/${cartPosition.contract.id}/deactivate/${option.id}/cancel">Cancel</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:forEach  items="${cartPosition.dependingOptions}" var="option">
                                <tr>
                                    <td><span class="label label-danger">Deleted</span></td>
                                    <td>${option.name} (${option.connectionCost} once + ${option.monthlyCost} every
                                        month)
                                    </td>
                                    <td>Need mandatory option</td>
                                </tr>
                            </c:forEach>


                            </tbody>
                        </table>
                        <a href="/cart/${cartPosition.contract.id}/save" class="btn btn-success">Save this number</a>

                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>