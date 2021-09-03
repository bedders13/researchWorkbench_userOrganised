$(document).ready(function() {
    //delete an item form the list
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
    document.getElementById(id).classList = "";
}
function hideButtons(id){
    document.getElementById(id).classList = "list-button-hide";
}

function getBookmarks(){
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

function showBookmark(id){
    location.href = `show.html?id=${id}&read_later=`;
}