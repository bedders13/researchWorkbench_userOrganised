function getBookmarks(){
    $.get({
        url: "ShowBookmarkServlet",
        data: {
            userId: sessionStorage.getItem("user_id")
        }
    }).done(function (response) {
        $("#bookmarkListGroup").html(response);
    });
}

function deleteBookmark(id){
    console.log("clicked delete");
    $.post({
        url: "ShowBookmarkServlet",
        data: {
            id: id,
        },
        success: function (data){
            console.log(`deleted bookmark with id: ${id}`);
            getBookmarks();
        }
    })
}

function showBookmark(id){
    location.href = `show.html?id=${id}&read_later=`;
}