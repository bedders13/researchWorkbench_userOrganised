<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.84.0">
    <title>Sign in</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">



    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>


    <!-- Custom styles for this template -->
    <link href="css/signin.css" rel="stylesheet">
</head>
<body class="text-center">

<main class="form-signin">
    <form action="ProfileServlet" method="post">
        <img class="mb-4" src="assets/img/ndltd.gif" alt="" width="72" height="57">
        <h1 class="h3 mb-3 fw-normal">Create a user profile</h1>

        <div class="form-floating">
            <input type="name" name="userName" class="form-control" id=floatingUserName placeholder="User Name">
            <label for="floatingUserName">User Name</label>
        </div>
        <div class="form-floating">
            <input type="email" name="emailAddress" class="form-control" id="floatingInput" placeholder="name@example.com" data-sb-validations="required,email">
            <div class="invalid-feedback text-white" data-sb-feedback="emailAddress:required">Email Address is required.</div>
            <div class="invalid-feedback text-white" data-sb-feedback="emailAddress:email">Email Address Email is not valid.</div>
            <label for="floatingInput">Email address</label>
        </div>


<!--        <div class="checkbox mb-3">-->
<!--            <label>-->
<!--                <input type="checkbox" value="remember-me"> Remember me-->
<!--            </label>-->
<!--        </div>-->
        <button class="w-100 btn btn-lg btn-primary" type="submit">Create</button>
<!--        <p class="mt-5 mb-3 text-muted">&copy; 2017–2021</p>-->
    </form>
    <div>
        <p>Already a user? <a href="create_profile.jsp">Log in </a></p>
    </div>
</main>



</body>
</html>
