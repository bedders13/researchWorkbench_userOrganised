//variables needed for the modal
let docIdForShownModal;
let docTitleForShownModal;
let docCreatorForShownModal;
let docDateForShownModal;
let listItemIdForShownModal;
//force reload page when back on browser is used
window.addEventListener( "pageshow", function ( event ) {
    const historyTraversal = event.persisted ||
        ( typeof window.performance != "undefined" &&
            window.performance.navigation.type === 2 );
    if ( historyTraversal ) {
        // Handle page restore.
        window.location.reload();
    }
});

$(document).ready(function() {
    $('[data-bs-toggle="tooltip"]').tooltip();
    //delete an item form the list
    const addToListModal = document.getElementById('addToListModal');
    addToListModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        docIdForShownModal = button.getAttribute('data-bs-objectId');
        docTitleForShownModal = button.getAttribute('data-bs-objectTitle');
        docCreatorForShownModal = button.getAttribute('data-bs-objectCreator');
        docDateForShownModal = button.getAttribute('data-bs-objectDate');
        docDateForShownModal = button.getAttribute('data-bs-objectDate');
        listItemIdForShownModal = button.getAttribute('data-bs-ListItemId');
    });
})

function showTooltips(element){
    var tooltip = bootstrap.Tooltip.getOrCreateInstance(element);
    tooltip.show();
}

function hideTooltips(element){
    var tooltip = bootstrap.Tooltip.getOrCreateInstance(element);
    tooltip.hide();
}

//get all the items/ETDs on the user list
function getListItems(){
    const urlString = window.location.search;
    const urlParms = new URLSearchParams(urlString);
    const userListId = Number(urlParms.get("id"));
    //check if there is a logged in user
    const userId = (Number(sessionStorage.getItem("logged_in")) === 1) ? sessionStorage.getItem("user_id") : -1;
    $.get({
        url: "ViewUserListServlet",
        data: {
            userListId: userListId,
            userId: userId
        }
    }).done(function (response) {
        $("#userListDiv").html(response);
    });

}

//get all the list names for add to list modal
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

//the bookmark icon is clicked
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

//add to list icon clicked
function addEtdToList(userListId){
    $.post({
        url: "SaveEtdServlet",
        data: {
            method: "list",
            id: docIdForShownModal,
            title: docTitleForShownModal,
            creator: docCreatorForShownModal,
            date: docDateForShownModal,
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

//add the ETD to the read later list
function addEtdtoReadLater(){
    const bookmarkBtn = document.getElementById(`book-${listItemIdForShownModal}`);
    //add to read later list
    if (bookmarkBtn.value === "0"){
        $.post({
            url: "SaveEtdServlet",
            data: {
                method: "bookmark",
                id: docIdForShownModal,
                title: docTitleForShownModal,
                creator: docCreatorForShownModal,
                date: docDateForShownModal,
                userId: sessionStorage.getItem("user_id")
            },
            dataType: "json",
            success: function (data){
                console.log('bookmarked');
                console.log(data);

                if (data.bookmarked){
                    bookmarkBtn.innerHTML = "<i class=\"bi bi-bookmark-fill\"></i>";
                    bookmarkBtn.value = "1";
                    $("#closeAddToListModal").click();
                }
            }
        })
    } else {
        $('#alert-pane').collapse();
    }
}

//create a new user list from the modal
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

//show the bookmark and list buttons onmouseover
function showButtons(id){
    document.getElementById(`book-${id}`).classList = "btn-lg";
    document.getElementById(`list-${id}`).classList = "btn-lg";
}
//hide the bookmark and list buttons onmouseout
function hideButtons(id){
    document.getElementById(`book-${id}`).classList = "list-button-hide";
    document.getElementById(`list-${id}`).classList = "list-button-hide";
}

//build the url to view the list item
function showListItem(id){
    location.href = `show.html?id=${id}&search=`;
}

function goBack() {
    history.back();
};