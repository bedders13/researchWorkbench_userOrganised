
$(document).ready(function() {
    // if(Number(document.getElementById("hiddenLoggedIn").value) === 1) {
    //     document.getElementById("signUpAndLogInBtn").classList.toggle("hideP");
    //     console.log("this should fire");
    //     document.getElementById("loggedInNavBar").classList.toggle("hideP");
    //     sessionStorage.setItem("logged_in", 1+"");
    //     sessionStorage.setItem("user_id", document.getElementById("hiddenUserId").value)
    //     sessionStorage.setItem("user_name", document.getElementById("hiddenUserName").value)
    // }
    //show log in buttons in navbar is not logged in
    if(Number(sessionStorage.getItem("logged_in")) === 1) {
        document.getElementById("signUpAndLogInBtn").classList.toggle("hideP");
        console.log("this should fire");
        document.getElementById("loggedInNavBar").classList.toggle("hideP");
        document.getElementById("loggedInMessage").innerHTML= `Welcome, ${sessionStorage.getItem("user_name")}!`
    }
})

function logOut(){
    sessionStorage.removeItem("logged_in");
    sessionStorage.removeItem("user_id");
    sessionStorage.removeItem("user_name");
    location.reload();
    // $.post({
    //     url: "LogOutServlet",
    //     success: function (data){
    //         const path = window.location.pathname;
    //         const page = path.split('/').pop();
    //         console.log(`page: ${page}`)
    //         if (page === "user_list.jsp" || page === "read_later.html" ){
    //             location.href = "index.jsp";
    //         } else {
    //             location.reload();
    //         }
    //     }
    // })
}

function logIn(emailAddress){
    if (emailAddress === null ) {
        alert("Please enter an email address");
    } else {
        console.log("this part works lol");
        $.post({
            url: "ProfileServlet",
            data: {
                method: "logIn",
                emailAddress: emailAddress
            },
            dataType: "json",
            success: function (data){
                console.log(data);
                sessionStorage.setItem("logged_in", data.loggedIn);
                sessionStorage.setItem("user_id", data.userId);
                sessionStorage.setItem("user_name", data.userName);
                location.reload();
            }
        })
    }
}

function createProfile(userName, emailAddress) {
    if (emailAddress === null || userName === null) {
        alert("Please make sure both fields are filled in.");
    } else {

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