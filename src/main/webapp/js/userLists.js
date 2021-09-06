$(document).ready(function() {
    //delete the user list
    var deleteUserListModal = document.getElementById('deleteUserListModal');
    deleteUserListModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        var userListId = Number(button.getAttribute('data-bs-listId'));

        const deleteUserListBtn = document.getElementById("deleteUserListBtn");
        deleteUserListBtn.onclick = function () {
            deleteUserList(userListId);
        };
    });

    //delete an item from the list
    var deleteListItemModal = document.getElementById('deleteListItemModal');
    deleteListItemModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        var objectId = button.getAttribute('data-bs-objectId');
        var userListId = Number(button.getAttribute('data-bs-listId'));

        const deleteListItemBtn = document.getElementById("deleteListItemBtn");
        deleteListItemBtn.onclick = function () {
            deleteListItem(objectId, userListId);
        };
    });

    //delete an item from the read later list
    var deleteBookmarkModal = document.getElementById('deleteBookmarkModal');
    deleteBookmarkModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        var objectId = button.getAttribute('data-bs-objectId');
        var userId = Number(button.getAttribute('data-bs-userId'));
        // If necessary, you could initiate an AJAX request here
        // and then do the updating in a callback.
        //
        // Update the modal's content.
        // var modalTitle = exampleModal.querySelector('.modal-title')
        // var modalBodyInput = exampleModal.querySelector('.modal-body input')
        //
        // modalTitle.textContent = 'New message to ' + recipient
        // modalBodyInput.value = recipient
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
            $("#deleteUserListCloseBtn").click();
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
            getBookmarks();
        }
    })
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
            $("#deleteListItemCloseBtn").click();
            getListItems(userListId);
        }
    })
}

function showListItem(id){
    location.href = `show.html?id=${id}&user_list=`;
}

// $(function(){
//     console.log('ready');
//
//     $('.list-group a').click(function(e) {
//         e.preventDefault()
//
//         $that = $(this);
//
//         $('.list-group').find('a').removeClass('active');
//         $that.addClass('active');
//     });
// })


