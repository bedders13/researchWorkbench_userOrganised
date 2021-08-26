function readLaterBtnClicked(id, title, creator, date){
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
                document.getElementById('bookmarkBtn').className = "btn btn-primary btn-sm float-right disabled";
            }

        }
    })
}
function isEtdAddedToReadLater(id){
    $.post({
        url: "SaveEtdServlet",
        data: {
            method: "check",
            id: id,
            userId: sessionStorage.getItem("user_id")
        },
        dataType: "json",
        success: function (data){
            console.log('bookmarked');
            console.log(data);
            if (data.bookmarked){
                document.getElementById('bookmarkBtn').className = "btn btn-primary btn-sm float-right disabled";
            }

        }
    })
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
let backUrl = null;
let doc;
let readLater = false;

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
        backUrl = getParams.get("back");
    }
    if (getParams.has("read_later")){
        readLater = true;
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
    }

    //load back to search button
    if (backUrl !== null){
        const aTag = document.createElement("a");
        aTag.href = backUrl;
        aTag.className = "btn btn-primary btn-sm";
        aTag.style = "margin-top: 15px; margin-bottom: 15px;";
        const backSymbol = document.createElement("i");
        backSymbol.className = "bi bi-chevron-left";
        aTag.appendChild(backSymbol);
        aTag.append("Search");


        buttonColDiv.appendChild(aTag);
        showDocDiv.appendChild(buttonColDiv);
    } else {
        if (readLater){
            const aTag = document.createElement("a");
            aTag.href = "read_later.html";
            aTag.className = "btn btn-primary btn-sm";
            aTag.style = "margin-top: 15px; margin-bottom: 15px;";
            const backSymbol = document.createElement("i");
            backSymbol.className = "bi bi-chevron-left";
            aTag.appendChild(backSymbol);
            aTag.append("Read Later");


            buttonColDiv.appendChild(aTag);
            showDocDiv.appendChild(buttonColDiv);
        }
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
                tagsDiv.className = "widget-26-job-category bg-soft-info";
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