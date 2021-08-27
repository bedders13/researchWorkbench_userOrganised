function getListItems(){
    const urlString = window.location.search;
    const urlParms = new URLSearchParams(urlString);
    const userListId = Number(urlParms.get("id"));
    $.get({
        url: "ViewUserListServlet",
        data: {
            userListId: userListId
        }
    }).done(function (response) {
        $("#userListDiv").html(response);
    });

}


function showListItem(id){
    location.href = `show.html?id=${id}&search=`;
}

// $(document).ready(function() {
//
// })