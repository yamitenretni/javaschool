<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">eCare</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <c:if test="${currentUser.roleName != 'CLIENT'}">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Tariffs<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/tariffs">Tariff list</a></li>
                            <li><a href="/tariffs/add">New Tariff</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Options<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/options">Option list</a></li>
                            <li><a href="/options/add">New option</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Clients<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/clients">Client list</a></li>
                            <li><a href="/clients/add/step1">New client</a></li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-left" role="search" action="/clients/search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search" pattern="\d+" required>
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </c:if>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Hello, ${currentUser.login} <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/logout">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>