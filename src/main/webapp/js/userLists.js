$(document).ready(function() {
    //delete the user list
    const deleteUserListModal = document.getElementById('deleteUserListModal');
    deleteUserListModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        const button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        const userListId = Number(button.getAttribute('data-bs-listId'));
        const deleteUserListBtn = document.getElementById("deleteUserListBtn");
        deleteUserListBtn.onclick = function () {
            deleteUserList(userListId);
        };
    });

    //delete an item from the list
    const deleteListItemModal = document.getElementById('deleteListItemModal');
    deleteListItemModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        const button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        const objectId = button.getAttribute('data-bs-objectId');
        const userListId = Number(button.getAttribute('data-bs-listId'));
        const deleteListItemBtn = document.getElementById("deleteListItemBtn");
        deleteListItemBtn.onclick = function () {
            deleteListItem(objectId, userListId);
        };
    });

    //delete an item from the read later list
    const deleteBookmarkModal = document.getElementById('deleteBookmarkModal');
    deleteBookmarkModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        const button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        const objectId = button.getAttribute('data-bs-objectId');
        const userId = Number(button.getAttribute('data-bs-userId'));
        const deleteBookmarkBtn = document.getElementById("deleteBookmarkBtn");
        deleteBookmarkBtn.onclick = function () {
            deleteBookmark(objectId, userId);
        };
    });


})

function showButtons(id){
    document.getElementById(id).classList = "pull-right";
}
function hideButtons(id){
    document.getElementById(id).classList = "pull-right list-button-hide";
}

//get all the user's reading lists
function getUserLists(){
    $.get({
        url: "UserListServlet",
        data: {
            method: "userListPage",
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#userListGroup").html(response);
    });
}

//create a new user list for the user
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

//delete the user's reading list
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
            $("#deleteUserListCloseBtn").click();
            getUserLists();

        }
    })
}

//update the privacy of the user list
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

//get all the list items on a user list
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

//get the user's bookmarks
function getBookmarks(){
    $.get({
        url: "ReadLaterServlet",
        data: {
            method: "list",
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#userListItems").html(response);
    });
}

//delete the user's bookmark
function deleteBookmark(objectId, userId){
    console.log("clicked delete");
    $.post({
        url: "ReadLaterServlet",
        data: {
            method: "bookmark",
            objectId: objectId,
            userId: userId
        },
        success: function (data){
            console.log(`deleted bookmark with id: ${objectId} and userId: ${userId}`);
            $("#deleteBookmarkCloseBtn").click();
            getBookmarks();
        }
    })
}

//remove a item/ETD from a user list
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
            $("#deleteListItemCloseBtn").click();
            getListItems(userListId);
        }
    })
}

//build the URL to view the list item
function showListItem(id){
    location.href = `show.html?id=${id}&user_list=`;
}



