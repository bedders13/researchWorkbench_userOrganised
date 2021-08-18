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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script type="text/javascript" src="js/scripts.js"></script>
    <script type="text/javascript">
        function getUserLists(){
            const ajaxRequest = new XMLHttpRequest();
            ajaxRequest.onreadystatechange = function (){
                if (ajaxRequest.readyState == 4) {
                    if (ajaxRequest.status == 200){
                        document.getElementById("userListTable").innerHTML = ajaxRequest.responseText;
                    } else{
                        console.log("Status error: " + ajaxRequest.status);
                    }
                }else {
                    console.log("Ignored ready state: " + ajaxRequest.readyState);
                }
            }
            ajaxRequest.open("GET", "UserListServlet");
            ajaxRequest.send();
        }


    </script>

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
        <div id="loggedInNavBar" >
            <% String userName = (String)(session.getAttribute("user_name"));%>
            <p>Welcome, <%= userName %>!</p>
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Menu
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="index.jsp">Home</a></li>
                    <li><a class="dropdown-item" href="#">User Lists</a></li>
                    <li><a class="dropdown-item" href="#">Read Later</a></li>
                    <li><a class="dropdown-item" href="LogOutServlet">Log out</a></li>
                </ul>
            </div>
        </div>
    </div>
    <input type="hidden" id="hiddenLoggedIn" value="<%= (Integer)(session.getAttribute("logged_in")) %>">
    <input type="hidden" id="hiddenUserId" value="<%= (Integer)(session.getAttribute("user_id")) %>">
</nav>

<!-- Masthead-->
<h1>User Lists</h1>
<!-- Button trigger modal -->
<div>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createNewUserListModal">
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
                <form action="UserListServlet" method="post">
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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                </form>
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

<script type="text/javascript">
    const listContentsModal = document.getElementById('listContentsModal')
    listContentsModal.addEventListener('show.mdb.modal', (event) => {
        console.log("list content modal fired");
        // Button that triggered the modal
        const button = event.relatedTarget
        // Extract info from data-mdb-* attributes
        const userList = button.getAttribute('data-mdb-name');
        // If necessary, you could initiate an AJAX request here
        // and then do the updating in a callback.
        //
        // Update the modal's content.
        const modalTitle = listContentsModal.querySelector('.modal-title');
        // const modalBodyInput = listContentsModal.querySelector('.modal-body input')

        modalTitle.textContent = `${userList} Items`;
        // modalBodyInput.value = recipient
    })

</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>