function getUserLists(){
    $.get({
        url: "UserListServlet",
        data: {
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#userListGroup").html(response);
    });
}

function createUserList(listName, isPrivate){
    if (listName === null ) {
        alert("Please enter a name for your list");
    } else {
        $.post({
            url: "UserListServlet",
            data: {
                method: "create",
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

function deleteUserList(userListId){
    $.post({
        url: "UserListServlet",
        data: {
            method: "delete",
            userListId: userListId,
            userId: sessionStorage.getItem("user_id"),
        },
        success: function (data){
            // $("#createNewUserListModal").modal('dispose');
            $("#closeModalBtn").click();
            getUserLists();

        }
    })
}

function deleteUserList(userListId){
    $.post({
        url: "UserListServlet",
        data: {
            method: "delete",
            userListId: userListId,
            userId: sessionStorage.getItem("user_id"),
        },
        success: function (data){
            // $("#createNewUserListModal").modal('dispose');
            $("#closeModalBtn").click();
            getUserLists();

        }
    })
}

function updateUserList(userListId, isPrivate){
    $.post({
        url: "UserListServlet",
        data: {
            method: "update",
            userListId: userListId,
            isPrivate: isPrivate,
            userId: sessionStorage.getItem("user_id"),
        },
        success: function (data){
            // $("#createNewUserListModal").modal('dispose');
            $("#closeModalBtn").click();
            getUserLists();

        }
    })
}


function getListItems(userListId){
    $.get({
        url: "ListItemsServlet",
        data: {
            userListId: userListId
        }
    }).done(function (response) {
        $("#userListItems").html(response);
    });
}

function deleteListItem(objectId, userListId){
    console.log("clicked delete");
    $.post({
        url: "ReadLaterServlet",
        data: {
            method: "list",
            objectId: objectId,
            userListId: userListId
        },
        success: function (data){
            console.log(`deleted list item with id: ${objectId}`);
            getListItems(userListId);
        }
    })
}

function showListItem(id){
    location.href = `show.html?id=${id}&user_list=`;
}

$(function(){
    console.log('ready');

    $('.list-group a').click(function(e) {
        e.preventDefault()

        $that = $(this);

        $('.list-group').find('a').removeClass('active');
        $that.addClass('active');
    });
})


