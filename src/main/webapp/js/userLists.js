function getUserLists(){
    // const ajaxRequest = new XMLHttpRequest();
    // ajaxRequest.onreadystatechange = function (){
    //     if (ajaxRequest.readyState == 4) {
    //         if (ajaxRequest.status == 200){
    //             document.getElementById("userListTable").innerHTML = ajaxRequest.responseText;
    //         } else{
    //             console.log("Status error: " + ajaxRequest.status);
    //         }
    //     }else {
    //         console.log("Ignored ready state: " + ajaxRequest.readyState);
    //     }
    // }
    // ajaxRequest.open("GET", "UserListServlet");
    // ajaxRequest.setRequestHeader("userId", sessionStorage.getItem("user_id"));
    // ajaxRequest.send();
    console.log(`user_id: ${sessionStorage.getItem("user_id")}`)
    $.get({
        url: "UserListServlet",
        data: {
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#userListTable").html(response);
    });
}

function createUserList(listName, isPrivate){
    if (listName === null ) {
        alert("Please enter a name for your list");
    } else {
        $.post({
            url: "UserListServlet",
            data: {
                userId: sessionStorage.getItem("user_id"),
                listName: listName,
                isPrivate: isPrivate
            },
            success: function (data){
                // $("#createNewUserListModal").modal('dispose');
                $("#closeModalBtn").click();
                getUserLists();

            }
        })
    }
}




