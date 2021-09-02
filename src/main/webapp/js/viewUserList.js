let docId;
let docTitle;
let docCreator;
let docDate;
window.addEventListener( "pageshow", function ( event ) {
    var historyTraversal = event.persisted ||
        ( typeof window.performance != "undefined" &&
            window.performance.navigation.type === 2 );
    if ( historyTraversal ) {
        // Handle page restore.
        window.location.reload();
    }
});

$(document).ready(function() {
    //delete an item form the list
    var addToListModal = document.getElementById('addToListModal');
    addToListModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        docId = button.getAttribute('data-bs-objectId');
        docTitle = button.getAttribute('data-bs-objectTitle');
        docCreator = button.getAttribute('data-bs-objectCreator');
        docDate = button.getAttribute('data-bs-objectDate');

        // const deleteBookmarkBtn = document.getElementById("deleteBookmarkBtn");
        // deleteBookmarkBtn.onclick = function () {
        //     deleteBookmark(objectId, userId);
        // };
    });

})

function getListItems(){
    const urlString = window.location.search;
    const urlParms = new URLSearchParams(urlString);
    const userListId = Number(urlParms.get("id"));
    $.get({
        url: "ViewUserListServlet",
        data: {
            userListId: userListId,
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#userListDiv").html(response);
    });

}

//for add to list modal
function getListNames(){
    const userId = sessionStorage.getItem("user_id");
    if (userId !== null){
        $.get({
            url: "SaveEtdServlet",
            data: {
                userId: sessionStorage.getItem("user_id")
            }
        }).done(function (response) {
            $("#addToListModalBody").html(response);
        });
    }

}
function bookmarkBtnClicked(button, id, title, creator, date){
    //add bookmark
    if (button.value === "0"){
        $.post({
            url: "SaveEtdServlet",
            data: {
                method: "bookmark",
                id: id,
                title: title,
                creator: creator,
                date: date,
                userId: sessionStorage.getItem("user_id")
            },
            dataType: "json",
            success: function (data){
                console.log('bookmarked');
                console.log(data);
                if (data.bookmarked){
                    button.innerHTML = "<i class=\"bi bi-bookmark-fill\"></i>";
                    button.value = "1";
                }
            }
        })
        //delete bookmark
    } else {
        $.post({
            url: "ReadLaterServlet",
            data: {
                method: "bookmark",
                objectId: id,
                userId: sessionStorage.getItem("user_id")
            },
            success: function (data){
                console.log(`deleted bookmark with id: ${id} and userId: ${sessionStorage.getItem("user_id")}`);
                button.innerHTML = "<i class=\"bi bi-bookmark\"></i>";
                button.value = "0";
            }
        })
    }
}

function addEtdToList(userListId){
    $.post({
        url: "SaveEtdServlet",
        data: {
            method: "list",
            id: docId,
            title: docTitle,
            creator: docCreator,
            date: docDate,
            userListId: userListId,
            userId: sessionStorage.getItem("user_id")
        },
        dataType: "json",
        success: function (data){
            console.log('added to list function');
            console.log(data);
            if (data.exists){
                $('#alert-pane').collapse();

            }
            if (data.added){
                // document.getElementById('addToListBtn').className = "btn btn-primary btn-sm float-right disabled";
                $("#closeAddToListModal").click();
            }
        }
    })
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
                $('#addToListModal').modal('show');
                getListNames();

            }
        })
    }
}

function showButtons(id){
    document.getElementById(`book-${id}`).classList = "";
    document.getElementById(`list-${id}`).classList = "";
}
function hideButtons(id){
    document.getElementById(`book-${id}`).classList = "list-button-hide";
    document.getElementById(`list-${id}`).classList = "list-button-hide";
}
function showListItem(id){
    location.href = `show.html?id=${id}&search=`;
}
function goBack() {
    history.back();
};