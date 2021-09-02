function readLaterBtnClicked(id, title, creator, date){
    const bookmarkBtn = document.getElementById('bookmarkBtn');
    //add bookmark
    if (bookmarkBtn.value === "0"){
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
                    bookmarkBtn.style = "margin-top: 15px; float: right; margin-right: 2px; background-color: #5C636A; border-color: #5C636A;";
                    bookmarkBtn.innerHTML = "";
                    const iTag = document.createElement("i");
                    iTag.className = "bi bi-check2";
                    iTag.style = "margin-right: 2px;"
                    bookmarkBtn.appendChild(iTag);
                    bookmarkBtn.append("Added");
                    bookmarkBtn.value = "1";
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
                bookmarkBtn.style = "margin-top: 15px; float: right; margin-right: 2px;";
                bookmarkBtn.innerText = "Bookmark";
                bookmarkBtn.value = "0";
            }
        })
    }
}

function addEtdtoReadLater(){
    const bookmarkBtn = document.getElementById('bookmarkBtn');
    //add bookmark
    if (bookmarkBtn.value === "0"){
        $.post({
            url: "SaveEtdServlet",
            data: {
                method: "bookmark",
                id: docId,
                title: docTitle,
                creator: docAuthor,
                date: docDate,
                userId: sessionStorage.getItem("user_id")
            },
            dataType: "json",
            success: function (data){
                console.log('bookmarked');
                console.log(data);
                // if (data.exists) {
                //     $('#alert-pane').collapse();
                // }
                if (data.bookmarked){
                    bookmarkBtn.style = "margin-top: 15px; float: right; margin-right: 2px; background-color: #5C636A; border-color: #5C636A;";
                    bookmarkBtn.innerHTML = "";
                    const iTag = document.createElement("i");
                    iTag.className = "bi bi-check2";
                    iTag.style = "margin-right: 2px;"
                    bookmarkBtn.appendChild(iTag);
                    bookmarkBtn.append("Added");
                    bookmarkBtn.value = "1";
                    $("#closeAddToListModal").click();
                }
            }
        })
    } else {
        $('#alert-pane').collapse();
    }
}

function addEtdToList(userListId){
    $.post({
        url: "SaveEtdServlet",
        data: {
            method: "list",
            id: docId,
            title: docTitle,
            creator: docAuthor,
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
                getUserListsContainingEtd();
            }
        }
    })
}


function isEtdAddedToReadLater(id){
    $.post({
        url: "SaveEtdServlet",
        data: {
            method: "checkBookmark",
            id: id,
            userId: sessionStorage.getItem("user_id")
        },
        dataType: "json",
        success: function (data){
            console.log('bookmarked');
            console.log(data);
            if (data.bookmarked){
                const bookmarkBtn = document.getElementById('bookmarkBtn');
                bookmarkBtn.style = "margin-top: 15px; float: right; margin-right: 2px; background-color: #5C636A; border-color: #5C636A;";
                bookmarkBtn.innerHTML = "";
                const iTag = document.createElement("i");
                iTag.className = "bi bi-check2";
                iTag.style = "margin-right: 2px;"
                bookmarkBtn.appendChild(iTag);
                bookmarkBtn.append("Added");
                bookmarkBtn.value = "1";
            }

        }
    })
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

function getUserListsContainingEtd(){
    const queryString = window.location.search;
    const getParams = new URLSearchParams(queryString);
    const objectId = getParams.get("id");
    let userId = sessionStorage.getItem("user_id");
    if (userId === null) {
        userId = -1;
    }
    $.get({
        url: "UserListServlet",
        data: {
            method: "showPage",
            objectId: objectId,
            userId: userId
        }
    }).done(function (response) {
        $("#userListCol").html(response);
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
                $('#addToListModal').modal('show');
                getListNames();

            }
        })
    }
}

function showListItem(id){
    location.href = `show.html?id=${id}&search=`;
}

//functions needed to load document
const additionalFieldsMap = {
    id:'Identifier',
    date_printable:'Date',
    creator:'Creators',
    contributo:'Contributors',
    publisher:'Publisher',
    source_set_names:'Source Sets',
    language:'Language',
    language_detected_printable:'Detected Language',
    type: 'Type',
    format: 'Format',
    source: 'Source',
    coverage:'Coverage',
    rights:'Rights',
    relation:'Relation'
}
let backUrlExists = false;
let doc;
let readLater = false;
let userList = false;

let docId;
let docTitle;
let docAuthor;
let docDate;

$.get({
    url: "SolrServlet",
    data: {
        queryType: "get",
        queryString: buildMainQueryString(),
    },
    dataType: "json",
    success: function (data){
        doc = data.doc;
        console.log(data);
        // document.getElementById("currentDocId").value = data.doc.id;
        // document.getElementById("currentDocTitle").value = data.doc.title;
        // document.getElementById("currentDocAuthor").value = data.doc.author;
        // document.getElementById("currentDocDate").value = data.doc.date;
        docId = data.doc.id;
        docTitle = data.doc.title;
        docAuthor = ((doc.hasOwnProperty("creator")) ? doc.creator.join(', ') : 'null' );
        docDate = data.doc.date_printable;
        loadScript();

    },
    error: function(data) {
        console.log('error getting query');
        console.log(data);
    }

})

function buildMainQueryString(){
    const queryString = window.location.search;
    const getParams = new URLSearchParams(queryString);
    console.log(getParams.toString());
    let id;
    id = getParams.get("id");

    //set the back url if it exists
    if (getParams.has("back")){
        backUrlExists = true;
    }
    if (getParams.has("read_later")){
        readLater = true;
    }
    if (getParams.has("user_list")){
        userList = true;
    }

    return `id=${encodeURI(id)}`;

}

//function to create link for tag search
function getLinkForTagSearch(tag){
    return `search.html?q=subject:"${tag}"`;
}
//load the html tags after the json query has loaded

function loadScript(){

    const showDocDiv = document.getElementById("showDocDiv");

    //read later buttons
    const buttonColDiv = document.getElementById("buttonColDiv");
    const bookmarkButton = document.getElementById("bookmarkBtn");
    bookmarkButton.onclick = function () {readLaterBtnClicked(doc.id, doc.title, ((doc.hasOwnProperty("creator")) ? doc.creator.join(', ') : 'null' ), doc.date_printable)}

    const addToListBtn = document.getElementById("addToListBtn");

    //check if user is logged in to  enable buttons
    if(Number(sessionStorage.getItem("logged_in")) === 1){
        bookmarkButton.className = "btn btn-primary btn-sm float-right";
        addToListBtn.className = "btn btn-primary btn-sm float-right";
        isEtdAddedToReadLater(doc.id);
        //remove info button
        document.getElementById("infoButton").classList.toggle("hideP");

    }

    //load back to search button
    if (backUrlExists){
        const fullUrl = window.location.search;
        const fullUrlList = fullUrl.split('&');
        fullUrlList.shift();
        const backUrl = fullUrlList.join('&');
        const aTag = document.createElement("a");
        aTag.href = backUrl.slice(5);
        aTag.className = "btn btn-primary btn-sm";
        aTag.style = "margin-top: 15px; margin-bottom: 15px;";
        const backSymbol = document.createElement("i");
        backSymbol.className = "bi bi-chevron-left";
        aTag.appendChild(backSymbol);
        aTag.append("Search");


        buttonColDiv.appendChild(aTag);
    } else if (readLater){
            const aTag = document.createElement("a");
            aTag.href = "read_later.html";
            aTag.className = "btn btn-primary btn-sm";
            aTag.style = "margin-top: 15px; margin-bottom: 15px;";
            const backSymbol = document.createElement("i");
            backSymbol.className = "bi bi-chevron-left";
            aTag.appendChild(backSymbol);
            aTag.append("Read Later");
            buttonColDiv.appendChild(aTag);
    } else if(userList) {
        const aTag = document.createElement("a");
        aTag.href = "user_list.html";
        aTag.className = "btn btn-primary btn-sm";
        aTag.style = "margin-top: 15px; margin-bottom: 15px;";
        const backSymbol = document.createElement("i");
        backSymbol.className = "bi bi-chevron-left";
        aTag.appendChild(backSymbol);
        aTag.append("User Lists");
        buttonColDiv.appendChild(aTag);
    } else {
        const aTag = document.createElement("a");
        aTag.onclick=function () {
            history.back();
        };
        aTag.className = "btn btn-primary btn-sm";
        aTag.style = "margin-top: 15px; margin-bottom: 15px;";
        const backSymbol = document.createElement("i");
        backSymbol.className = "bi bi-chevron-left";
        aTag.appendChild(backSymbol);
        aTag.append("Back");
        buttonColDiv.appendChild(aTag);
    }


    //doc title
    const titleColDiv = document.getElementById("titleColDiv");
    const docTitle = document.createElement("h2");
    docTitle.textContent = doc.title;
    titleColDiv.appendChild(docTitle);
    showDocDiv.appendChild(titleColDiv);

    //doc description
    if (doc.hasOwnProperty("description")){
        const descriptionColDiv = document.getElementById("descriptionColDiv");
        const descriptionHeaderDiv = document.createElement("div");
        descriptionHeaderDiv.className = "page-header";
        const descriptionHeading = document.createElement("h5");
        descriptionHeading.append("Description");
        descriptionHeaderDiv.appendChild(descriptionHeading);

        const descriptionTextDiv = document.createElement("div");
        descriptionTextDiv.className = "description-text";
        const descriptionText = document.createElement("p");
        descriptionText.textContent = doc.description;
        descriptionTextDiv.appendChild(descriptionText);

        descriptionColDiv.appendChild(descriptionHeaderDiv);
        descriptionColDiv.appendChild(descriptionTextDiv);
        showDocDiv.appendChild(descriptionColDiv);
    } else {
        const descriptionColDiv = document.createElement("div");
        descriptionColDiv.className = "col-sm-12";
        const emTag = document.createElement("em");
        emTag.textContent = "No description available";

        descriptionColDiv.appendChild(emTag);
        showDocDiv.appendChild(descriptionColDiv);
    }

    //doc urls
    if (doc.has_links) {
        const linksColDiv = document.getElementById("linksColDiv");
        const linksHeaderDiv = document.createElement("div");
        linksHeaderDiv.className = "page-header";
        const linksHeading = document.createElement("h5");
        linksHeading.style = "margin-top: 15px";
        linksHeading.append("Links & Downloads");
        linksHeaderDiv.appendChild(linksHeading);

        const linksList = document.createElement("ol");
        for (let l = 0; l < doc.urls.length; l++){
            const listItem = document.createElement("li");
            const aTag = document.createElement("a");
            aTag.href = doc.urls[l];
            aTag.textContent = doc.urls[l];
            aTag.target = "_blank";
            listItem.appendChild(aTag);
            linksList.appendChild(listItem);
        }
        linksColDiv.appendChild(linksHeaderDiv);
        linksColDiv.appendChild(linksList);
        showDocDiv.appendChild(linksColDiv);
    }

    //doc tags
    if (doc.hasOwnProperty("subject") && !doc["subject"].every(item => (item.length > 40))){
        const tagsColDiv = document.getElementById("tagsColDiv");
        const tagsHeaderDiv = document.createElement("div");
        tagsHeaderDiv.className = "page-header";
        const tagsHeading = document.createElement("h5");
        tagsHeading.style = "margin-top: 15px";
        tagsHeading.append("Tags");
        tagsHeaderDiv.appendChild(tagsHeading);
        tagsColDiv.appendChild(tagsHeaderDiv);
        for (let m = 0; m < doc.subject.length; m++){
            if (doc.subject[m].length > 40) {
                continue;

            } else{
                const tagsDiv = document.createElement("div");
                tagsDiv.className = "widget-26-job-category bg-soft-info2";
                const indicator = document.createElement("i");
                indicator.className = "indicator bg-info";
                const aTag = document.createElement("a");
                aTag.href = getLinkForTagSearch(doc.subject[m]);
                const textSpan = document.createElement("span");
                textSpan.textContent = doc.subject[m];
                aTag.appendChild(textSpan);

                tagsDiv.appendChild(indicator);
                tagsDiv.appendChild(aTag);

                tagsColDiv.appendChild(tagsDiv);

            }
        }
        showDocDiv.appendChild(tagsColDiv);
    }

    //doc additional fields
    const additionalColDiv = document.getElementById("additionalColDiv");
    const additionalFieldsDiv = document.createElement("div");
    additionalFieldsDiv.className = "page-header";
    const additionalFieldsHeading = document.createElement("h5");
    additionalFieldsHeading.style = "margin-top: 15px";
    additionalFieldsHeading.append("Additional Fields");
    additionalFieldsDiv.appendChild(additionalFieldsHeading);

    const additionalFieldsTable = document.createElement("table");
    additionalFieldsTable.className = "table-bordered";
    const additionalFieldsTableBody = document.createElement("tbody");

    for (var key in additionalFieldsMap){
        if(doc.hasOwnProperty(key)){
            const tableRow = document.createElement("tr");
            const tableKeyCol = document.createElement("td");
            tableKeyCol.className = "bold-table-column";
            tableKeyCol.textContent = `${additionalFieldsMap[key]}`;
            const tableValueCol = document.createElement("td");
            tableValueCol.textContent =  Array.isArray(doc[key]) ? doc[key].join(", ") : doc[key];

            tableRow.appendChild(tableKeyCol);
            tableRow.appendChild(tableValueCol);

            additionalFieldsTableBody.appendChild(tableRow);
        }
    }
    additionalFieldsTable.appendChild(additionalFieldsTableBody);

    additionalColDiv.appendChild(additionalFieldsDiv);
    additionalColDiv.appendChild(additionalFieldsTable);
    showDocDiv.appendChild(additionalColDiv);

}