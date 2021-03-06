<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="ResearchWorkbench.Models.User"  %>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>ResearchWorkbench</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <!-- Bootstrap icons-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Google fonts-->
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="css/styles.css" rel="stylesheet" />
    <link href="js/bootstrap.bundle.js" rel="script"/>
    <script src="js/main.js"></script>
</head>
<body>
<!-- Navigation-->
<nav class="navbar navbar-light bg-light static-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Research Workbench</a>

        <div id="signUpAndLogInBtn">
            <a class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createProfileModal" data-mdb-name="">Create profile </a>
            <a class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#signInModal" data-mdb-name="">Log in  </a>
        </div>
        <div id="loggedInNavBar" class="hideP">
            <p id="loggedInMessage"></p>

            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Menu
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="index.jsp">Search</a></li>
                    <li><a class="dropdown-item" href="user_list.html">Reading Lists</a></li>
                    <li><a class="dropdown-item" href="read_later.html">Bookmarks</a></li>
                    <li><a class="dropdown-item" onclick="logOut()">Log out</a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>
<!-- Masthead-->
<header class="masthead">
    <div class="container position-relative">
        <div class="row justify-content-center">
            <div class="col-xl-6">
                <div class="text-center text-white">
                    <!-- Page heading-->
                    <h1 class="mb-5">Global ETD Search</h1>
                    <form action="search.html" method="get" class="form-subscribe" id="searchForm">
                        <!-- Email address input-->
                        <div class="row">
                            <div class="col">
                                <input class="form-control form-control-lg" id="searchField" type="text" name="q" placeholder="Type something to start searching... " />
                            </div>
                            <div class="col-auto"><button class="btn btn-primary btn-lg" id="submitSearchButton" type="submit">Search</button></div>
                        </div>
                        <div class="d-none" id="submitSuccessMessageEmail">
                            <div class="text-center mb-3">
                                <div class="fw-bolder">Form submission successful!</div>
                                <p>To activate this form, sign up at</p>
                                <a class="text-white" href="https://startbootstrap.com/solution/contact-forms">https://startbootstrap.com/solution/contact-forms</a>
                            </div>
                        </div>
                        <div class="d-none" id="submitErrorMessageEmail"><div class="text-center text-danger mb-3">Error sending message!</div></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</header>
<div class="modal fade" id="signInModal" tabindex="-1" aria-labelledby="signInModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="signInModalLabel;">Log In</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
                <div class="modal-body">
                    <div class="collapse" id="log-in-alert-pane">
                        <div class="alert alert-warning d-flex align-items-center" role="alert">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-exclamation-triangle-fill" viewBox="0 0 16 16">
                                <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                            </svg>
                            <div>
                                That profile does not exist.
                            </div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="logInEmailAddress" class="form-label">Email Address</label>
                        <input type="text" class="form-control" id="logInEmailAddress" name="logInEmailAddress" >
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button  class="btn btn-primary" onclick="logIn(logInEmailAddress.value)">Log in</button>
                </div>
        </div>
    </div>
</div>

<div class="modal fade" id="createProfileModal" tabindex="-1" aria-labelledby="createProfileModal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createProfileModal;">Create a User Profile</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="createUserName" class="form-label">User Name</label>
                    <input type="text" class="form-control" id="createUserName" name="createUserName" >
                    <label for="createEmailAddress" class="form-label">Email Address</label>
                    <input type="text" class="form-control" id="createEmailAddress" name="createEmailAddress" >
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button  class="btn btn-primary" onclick="createProfile(createUserName.value, createEmailAddress.value)">Create</button>
            </div>
        </div>
    </div>
</div>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
</body>
</html>
