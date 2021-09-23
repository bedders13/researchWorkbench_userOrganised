$(document).ready(function() {
    //initialise tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });
    //show log in buttons in navbar is not logged in
    if(Number(sessionStorage.getItem("logged_in")) === 1) {
        document.getElementById("signUpAndLogInBtn").classList.toggle("hideP");
        console.log("this should fire");
        document.getElementById("loggedInNavBar").classList.toggle("hideP");
        document.getElementById("loggedInMessage").innerHTML= `Welcome, ${sessionStorage.getItem("user_name")}!`
    }
})

//log the user out
function logOut(){
    sessionStorage.removeItem("logged_in");
    sessionStorage.removeItem("user_id");
    sessionStorage.removeItem("user_name");
    location.href = "index.jsp";

}

//log the user in
function logIn(emailAddress){
    if (emailAddress === null ) {
        alert("Please enter an email address");
    } else {
        //post user email address to servlet
        $.post({
            url: "ProfileServlet",
            data: {
                method: "logIn",
                emailAddress: emailAddress
            },
            dataType: "json",
            success: function (data){
                console.log(data);
                if (data.exists){
                    sessionStorage.setItem("logged_in", data.loggedIn);
                    sessionStorage.setItem("user_id", data.userId);
                    sessionStorage.setItem("user_name", data.userName);
                    location.reload();
                } else {
                    $('#log-in-alert-pane').collapse();
                }

            }
        })
    }
}

//create a user profile
function createProfile(userName, emailAddress) {
    if (emailAddress === null || userName === null) {
        alert("Please make sure both fields are filled in.");
    } else {
        //post data to servlet
        $.post({
            url: "ProfileServlet",
            data: {
                method: "create",
                userName: userName,
                emailAddress: emailAddress
            },
            success: function (data) {
                sessionStorage.setItem("logged_in", data.loggedIn);
                sessionStorage.setItem("user_id", data.userId);
                sessionStorage.setItem("user_name", data.userName);
                location.reload();
            },
            error: function (data){
                console.log("error in post");
            }
        })
    }
}