function getUserLists(){
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




