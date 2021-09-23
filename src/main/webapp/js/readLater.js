$(document).ready(function() {
    //delete an item from the list
    var deleteBookmarkModal = document.getElementById('deleteBookmarkModal');
    deleteBookmarkModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-bs-* attributes
        var objectId = button.getAttribute('data-bs-objectId');
        var userId = Number(button.getAttribute('data-bs-userId'));
        const deleteBookmarkBtn = document.getElementById("deleteBookmarkBtn");
        deleteBookmarkBtn.onclick = function () {
            deleteBookmark(objectId, userId);
        };
    });

})

function showButtons(id){
    document.getElementById(id).classList = "";
}
function hideButtons(id){
    document.getElementById(id).classList = "list-button-hide";
}

//get all the user bookmarks
function getBookmarks(){
    //get bookmark html from servlet
    $.get({
        url: "ReadLaterServlet",
        data: {
            method: "bookmark",
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#bookmarkListGroup").html(response);
    });
}

//delete bookmark
function deleteBookmark(objectId, userId){
    console.log("clicked delete");
    //post data to servlet
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

function showBookmark(id){
    location.href = `show.html?id=${id}&read_later=`;
}