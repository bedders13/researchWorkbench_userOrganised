<%--
  Created by IntelliJ IDEA.
  User: hughbedford
  Date: 2021/08/09
  Time: 22:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/userLists.js"></script>

    <!-- Google fonts-->
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="css/styles.css" rel="stylesheet" />
    <link href="js/bootstrap.bundle.js" rel="script"/>
</head>
<body onload="getUserLists()">
<!-- Navigation-->
<nav class="navbar navbar-light bg-light static-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Research Workbench</a>
        <div id="signUpAndLogInBtn">

        </div>
        <div id="loggedInNavBar" class="hideP">
            <p id="loggedInMessage"></p>

            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Menu
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="index.jsp">Home</a></li>
                    <li><a class="dropdown-item" href="user_lists.jsp">User Lists</a></li>
                    <li><a class="dropdown-item" href="read_later.html">Read Later</a></li>
                    <li><a class="dropdown-item" onclick="logOut()">Log out</a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<!-- Masthead-->
<h1 style="margin-top: 16px; margin-left: 15px">User Lists</h1>
<!-- Button trigger modal -->
<div>
    <button type="button" class="btn btn-primary btn-sm" style="margin-left: 16px" data-bs-toggle="modal" data-bs-target="#createNewUserListModal">
        Create new list
    </button>
</div>
<div id="userListTable">

</div>


<!-- Modal -->
<%--<div class="modal fade" id="createNewUserListModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">--%>
<%--  <div class="modal-dialog">--%>
<%--    <div class="modal-content">--%>
<%--      <div class="modal-header">--%>
<%--        <h5 class="modal-title" id="newUserListModalList">Create a new user list</h5>--%>
<%--        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>--%>
<%--      </div>--%>
<%--      <div class="modal-body">--%>
<%--        <form action="NewListServlet" method="post">--%>
<%--          <div class="mb-3">--%>
<%--            <label for="userListName" class="form-label">List Name</label>--%>
<%--            <input type="text" class="form-control" id="userListName" >--%>
<%--          </div>--%>

<%--          <div class="mb-3 form-check">--%>
<%--            <input type="checkbox" class="form-check-input" id="userListIsPrivate">--%>
<%--            <label class="form-check-label" for="userListIsPrivate">Make this list private</label>--%>
<%--          </div>--%>
<%--          <button type="submit" class="btn btn-primary">Submit</button>--%>
<%--        </form>--%>
<%--      </div>--%>
<%--      <div class="modal-footer">--%>
<%--        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>--%>
<%--        <button type="button" class="btn btn-primary">Save changes</button>--%>
<%--      </div>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--</div>--%>
<!-- Modal -->
<!-- Modal -->
    <div class="modal fade" id="createNewUserListModal" tabindex="-1" aria-labelledby="newUserListModalListLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="newUserListModalListLabel;">Create a new user list</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="userListName" class="form-label">List Name</label>
                            <input type="text" class="form-control" id="userListName" name="userListName" >
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" name="userListIsPrivate" id="userListIsPrivate">
                            <label class="form-check-label" for="userListIsPrivate">Make this list private</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button id="closeModalBtn" type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button class="btn btn-primary" onclick="createUserList(userListName.value, userListIsPrivate.checked)">Create</button>
                    </div>
            </div>
        </div>
    </div>
<div class="modal fade" id="listContentsModal" tabindex="-1" aria-labelledby="listContentsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="listContentsModalLabel;">List Items</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
                <div class="modal-body">
                    list items
                </div>
<%--                <div class="modal-footer">--%>
<%--                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>--%>
<%--                    <button type="submit" class="btn btn-primary">Create</button>--%>
<%--                </div>--%>
        </div>
    </div>
</div>


</body>
</html>