function getBookmarks(){
    $.get({
        url: "ReadLaterServlet",
        data: {
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
            getBookmarks();
        }
    })
}

function showBookmark(id){
    location.href = `show.html?id=${id}&read_later=`;
}