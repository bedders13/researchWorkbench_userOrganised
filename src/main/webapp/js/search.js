//variables needed for search
let docs;
let data;
let totalCount;
let cursor;
let batchSize;
let queryTime;
let suggestions;

//facet variables
let selectedLanguageFacets = getFacetList('language');
let selectedSubjectFacets = getFacetList('subject');
let selectedSourceSetNamesFacets = getFacetList('source_set_names');
let selectedYearStartFacet = getIfSet('year_start');
let selectedYearEndFacet = getIfSet('year_end');
let languageFacets;
let subjectFacets;
let sourceSetNamesFacets;


//functions for handling solr queries
function getSuggestions(data){
    let suggestions;
    if (data.hasOwnProperty("spellcheck")){
        const suggestionsBlock = data.spellcheck.suggestions;
        if (suggestionsBlock.length > 0 ){
            suggestions = suggestionsBlock[1].suggestion;
        }
        // for (let i = 0; i < suggestionsBlock.length; i += 2){
        //     if(suggestionsBlock[i] === "collation" && typeof suggestionsBlock[i+1] === "string"){
        //         output.push(suggestionsBlock[i+1]);
        //     }
        // }
    }
    return suggestions;
}

//function for creating link to show document
function getLinkForDoc(id){
    return `show.html?id=${id}&back=http://${window.location.host}${window.location.pathname}${window.location.search}`;
}

//function to create link for another search
function getLinkForAnotherSearch(query){
    return `search.html?q=${query}`;
}

//function to create link for tag search
function getLinkForTagSearch(tag){
    return `search.html?q=subject:"${tag}"`;
}

//build link to different page
function getLinkForPage(startPage){
    const currentSearchQuery = window.location.search;
    const searchParams = new URLSearchParams(currentSearchQuery);
    searchParams.set("start", startPage);
    return `search.html?${searchParams.toString()}`;
}

//Returns a get value if it exists
function getIfSet(key){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    if (urlParams.has(key) && urlParams.get(key) !== null){
        return urlParams.getAll(key)[0] === "" ? null : urlParams.getAll(key);
    } else {
        return null;
    }
}

function getFacetList(facetName){
    const facet = getIfSet(facetName);
    if (facet !== null) {
        return (Array.isArray(facet)) ? facet : [facet];
    }
    return null;
}

//build the facet query
function buildFacetString(facetName, mustWrapQuotes = false, firstFq = false) {
    let facets = getFacetList(facetName);
    if (facets !== null) {
        if (mustWrapQuotes){
            facets = facets.map(function (facet){
                return '"' + facet + '"';
            })
        }
        const facetString = facets.join(" OR ");
        return (facetName === "language" || firstFq === true) ? ` +${facetName}:(${facetString})` : ` AND ${facetName}:(${facetString})`
    } else {
        return '';
    }
}

//build facet date query
function buildDateFacetString(firstFq = false){
    let yearStartFacet = getIfSet("year_start");
    let yearEndFacet = getIfSet("year_end");
    const today = new Date();
    //check if either the start year or end year exists
    if(yearStartFacet !== null || yearEndFacet !== null){
        if (yearStartFacet !== null){
            //check whether start date is before today and after 1970
            yearStartFacet = Math.min(Math.max(Number(yearStartFacet), 1970), today.getFullYear());
        } else{
            //if start date does not exist, set it to 1070
            yearStartFacet = 1970
        }

        if (yearEndFacet !== null){
            //check whether end date is less than today and after 1970
            yearEndFacet = Math.max(Math.min(Number(yearEndFacet), today.getFullYear()), 1970);
        } else {
            yearEndFacet = today.getFullYear();
        }

    }
    if (yearStartFacet !== null && yearEndFacet !== null){
        return firstFq ? ` +date:[${yearStartFacet}-01-01T00:00:00Z TO ${yearEndFacet}-12-31T23:59:59Z]` : ` AND date:[${yearStartFacet}-01-01T00:00:00Z TO ${yearEndFacet}-12-31T23:59:59Z]`;
    } else{
        return '';
    }
}


//check whether facet is selected
function isFacetSelected(facet, facetList){
    return (facetList !== null && facetList.includes(facet));
}

//function to build the main_query
function buildMainQueryString(){
    const queryString = window.location.search;
    const getParams = new URLSearchParams(queryString);
    console.log(getParams.toString());
    let q;

    q = ((getParams.has('q')) ? getParams.get('q') : getParams.get('query'));

    const start = (getParams.has("start") ? Math.max(Number(getParams.get("start")), 0) : 0 );
    const language = buildFacetString("language");
    let subject;
    if (language === ''){
        subject = buildFacetString("subject", false, true);
    } else {
        subject = buildFacetString("subject");
    }
    let sourceSetNames;
    if (language === '' && subject === ''){
        sourceSetNames = buildFacetString("source_set_names", true, true);
    } else {
        sourceSetNames = buildFacetString("source_set_names", true);
    }
    let date;
    if (language === '' && subject === '' && sourceSetNames === ''){
        date = buildDateFacetString(true);
    } else {
        date = buildDateFacetString();
    }
    // const facetQuery = `${buildFacetString("language")}${buildFacetString("subject")}${buildFacetString("source_set_names", true)}${buildDateFacetString()}`
    const facetQuery = '' + language + subject + sourceSetNames + date;
    // if ( typeof facetQuery === 'undefined'){
    // return `q=${q}&start=${start}`;
    // } else {
    return `q=${encodeURI(q)}&start=${encodeURI(start)}&fq=${encodeURI(facetQuery)}`;
    // }

}

//function to send main_query on page load
$.get({
    url: "SolrServlet",
    data: {
        queryType: "main_query",
        queryString: buildMainQueryString(),
    },
    dataType: "json",
    success: function (data){
        docs = data.response.docs;
        totalCount = data.response.numFound;
        cursor = data.response.start;
        batchSize = docs.length;
        queryTime = data.responseHeader.QTime / 1000;

        suggestions = getSuggestions(data);

        //facets
        sourceSetNamesFacets = data.facet_counts.facet_fields.source_set_names;
        languageFacets = data.facet_counts.facet_fields.language;
        subjectFacets = data.facet_counts.facet_fields.subject;
        console.log(data);
        loadScript();

    },
    error: function(data) {
        console.log('error getting query');
        console.log(data);
        // alert('woops!'); //or whatever
    }

})

//load the html tags after the json query has loaded
function loadScript(){
    const queryString = window.location.search;
    const getParams = new URLSearchParams(queryString);
    const q = getParams.get('q');
    document.getElementById("refineQuery").value = q;

    //populate the source dropdown
    const select = document.getElementById("source_set_names");
    for (let i = 0; i < sourceSetNamesFacets.length; i+= 2){
        let opt = document.createElement("option");
        opt.textContent = `${sourceSetNamesFacets[i]} [${sourceSetNamesFacets[i+1]}]`;
        opt.value = sourceSetNamesFacets[i];
        if (selectedSourceSetNamesFacets !== null && selectedSourceSetNamesFacets.includes(sourceSetNamesFacets[i])){
            opt.selected = true;
        }
        select.appendChild(opt);
    }

    if (selectedYearStartFacet !== null){
        document.getElementById("year_start").value = selectedYearStartFacet;
    }
    if (selectedYearEndFacet !== null){
        document.getElementById("year_end").value = selectedYearEndFacet;
    }

    //add the language checkboxes
    let nonZeroFacets = 0;
    const languageListDiv = document.getElementById("languageList");
    for (let i = 0; i < languageFacets.length; i+=2){
        if (languageFacets[i] !== 'tw'){
            if (languageFacets[i+1] === 0){
                continue;
            } else{
                nonZeroFacets++;
            }
            if (languageFacets[i] === 'zh'){
                languageFacets[i] = 'Zh-Tw';
            }
            const listItem = document.createElement("li");
            const formCheckDiv = document.createElement("div");
            const checkboxLabel = document.createElement("label");
            const checkboxInput = document.createElement("input");
            listItem.className = "list-group-item";
            formCheckDiv.className = "form-check";
            checkboxInput.type = "checkbox";
            checkboxInput.className = "form-check-input";
            checkboxInput.name = "language";
            checkboxInput.id = `$languageCheckbox${languageFacets[i]}`;
            checkboxInput.value = languageFacets[i];
            checkboxLabel.className = "form-check-label";
            checkboxLabel.for = `$languageCheckbox${languageFacets[i]}`;
            let languageString = languageFacets[i];
            languageString = languageString.charAt(0).toUpperCase() + languageString.slice(1);
            checkboxLabel.textContent = ` ${languageString}`;

            if (isFacetSelected(languageFacets[i], selectedLanguageFacets)){
                checkboxInput.checked = true;
            }

            listItem.appendChild(checkboxInput);
            listItem.appendChild(checkboxLabel);
            languageListDiv.appendChild(listItem);

        }

    }
    if (nonZeroFacets === 0){
        const listItem = document.createElement("li");
        listItem.className = "list-group-item";
        listItem.textContent = "No language data";
        languageListDiv.appendChild(listItem);
    }

    //add the subject checkboxes
    nonZeroFacets = 0;
    const subjectListDiv = document.getElementById("subjectsList");
    for (let i = 0; i < subjectFacets.length; i+=2){
        if (subjectFacets[i+1] === 0){
            continue;
        } else{
            nonZeroFacets++;
        }
        const listItem = document.createElement("li");
        const formCheckDiv = document.createElement("div");
        const checkboxLabel = document.createElement("label");
        const checkboxInput = document.createElement("input");
        listItem.className = "list-group-item";
        formCheckDiv.className = "form-check";
        checkboxInput.type = "checkbox";
        checkboxInput.className = "form-check-input";
        checkboxInput.name = "subject";
        checkboxInput.id = `subjectCheckbox${subjectFacets[i]}`;
        checkboxInput.value = subjectFacets[i];
        checkboxLabel.className = "form-check-label";
        checkboxLabel.for = `subjectCheckbox${subjectFacets[i]}`;
        let subjectString = subjectFacets[i];
        subjectString = subjectString.charAt(0).toUpperCase() + subjectString.slice(1);
        checkboxLabel.textContent = ` ${subjectString}`;

        if (isFacetSelected(subjectFacets[i], selectedSubjectFacets)){
            checkboxInput.checked = true;
        }

        listItem.appendChild(checkboxInput);
        listItem.appendChild(checkboxLabel);
        subjectListDiv.appendChild(listItem);
        // subjectListDiv.innerHTML +=  `<li class="list-group-item"> <div class="checkbox"> <label class="plain"> <input type="checkbox" name="subject[]" value='${subjectFacets[i]}'`;

        // subjectListDiv.innerHTML += `</label> <span class="badge document-count pull-right ">${subjectFacets[i+1]}</span> </div> </li>`;
    }
    //check if there are any subject tags
    if (nonZeroFacets === 0){
        const listItem = document.createElement("li");
        listItem.className = "list-group-item";
        listItem.textContent = "No subject data";
        subjectListDiv.appendChild(listItem);
    }

    //**** SEARCH RESULTS ***

    //add number results and spelling suggestions
    const resultsAndSuggestionsDiv = document.getElementById("resultsAndSuggestionsDiv");
    const resultsDiv = document.createElement("div");
    resultsDiv.className = "row";
    const resultsColDiv = document.createElement("div");
    resultsColDiv.className = "col-lg-6";
    const recordsDiv = document.createElement("div");
    recordsDiv.className = "records";
    const smallText = document.createElement("small");
    if (batchSize > 0){
        smallText.textContent = `Showing: ${cursor + 1}-${cursor + batchSize} of ${totalCount} results (${queryTime} seconds)`;
    } else{
        smallText.textContent = `Showing ${cursor}-${cursor} of ${totalCount} results (${queryTime} seconds)`;
    }
    recordsDiv.appendChild(smallText);
    resultsColDiv.appendChild(recordsDiv);
    resultsDiv.appendChild(resultsColDiv);
    resultsAndSuggestionsDiv.appendChild(resultsDiv);

    if (typeof suggestions !== "undefined"){
        if (suggestions.length > 0){
            const resultsDiv = document.createElement("div");
            resultsDiv.className = "row";
            const resultsColDiv = document.createElement("div");
            resultsColDiv.className = "col-lg-6";
            const recordsDiv = document.createElement("div");
            recordsDiv.className = "records";
            let suggestionString = "Spelling suggestions: ";
            recordsDiv.append(suggestionString);
            for (let i = 0; i < suggestions.length; i++) {
                const emElement = document.createElement("em");
                const aElement = document.createElement("a");
                aElement.href = getLinkForAnotherSearch(suggestions[i]);
                aElement.textContent = `"${suggestions[i]}"`;

                emElement.appendChild(aElement);
                if (i !== (suggestions.length - 1)){
                    emElement.append( ", ");
                }

                recordsDiv.appendChild(emElement);
            }
            resultsColDiv.appendChild(recordsDiv);
            resultsDiv.appendChild(resultsColDiv);
            resultsAndSuggestionsDiv.appendChild(resultsDiv);
        }
    }


    // populate the search results
    const searchResultsTableBody = document.getElementById("searchResultsTableBody");
    let loopIndex = cursor + 1;
    for (let i = 0; i < docs.length; i++){
        //create a row for the table
        const resultRow = document.createElement("tr");

        //index column
        const indexCol = document.createElement("td");
        const indexDiv = document.createElement("div");
        indexDiv.className = "widget-26-job-emp-img";
        const indexA = document.createElement("a");
        indexA.textContent = loopIndex;
        indexDiv.appendChild(indexA);
        indexCol.appendChild(indexDiv);
        //add to the row
        resultRow.appendChild(indexCol);

        //title column
        const titleCol = document.createElement("td");
        const titleDiv = document.createElement("div");
        titleDiv.className = "widget-26-job-title";
        const titleTag = document.createElement("a");
        titleTag.href = getLinkForDoc(docs[i].id);
        titleTag.textContent = docs[i].title;

        const dateTag = document.createElement("p");
        dateTag.className = "m-0";
        const dateSpanTag = document.createElement("span");
        dateSpanTag.className = "text-muted time"
        dateSpanTag.textContent = ((docs[i].hasOwnProperty("date_printable")) ? docs[i].date_printable : 'Unknown Date' );
        dateTag.appendChild(dateSpanTag);

        const descriptionTag = document.createElement("p");
        descriptionTag.className = "m-0";
        const descriptionSpanTag = document.createElement("span");
        descriptionSpanTag.className = "text-muted time"
        if (docs[i].hasOwnProperty("description")){
            descriptionSpanTag.textContent = docs[i].description;
        } else {
            descriptionSpanTag.textContent = "No description available";
        }
        descriptionTag.appendChild(descriptionSpanTag);

        titleCol.appendChild(titleTag);
        titleCol.appendChild(dateTag);
        titleCol.appendChild(descriptionTag);

        resultRow.appendChild(titleCol);

        //author column
        const authorCol = document.createElement("td");
        const authorDiv = document.createElement("div");
        authorDiv.className = "widget-26-job-info";
        const authorTag = document.createElement("p");
        authorTag.className = "type m-0";
        authorTag.textContent = ((docs[i].hasOwnProperty("creator")) ? docs[i].creator.join(', ') : '' );
        authorDiv.appendChild(authorTag);

        authorCol.appendChild(authorDiv);

        resultRow.appendChild(authorCol);

        //links column
        const linksCol = document.createElement("td");
        const linksDiv = document.createElement("div");
        linksDiv.className = "widget-26-job-salary";
        if (docs[i].hasOwnProperty("has_links") && docs[i].has_links === true) {
            const spanLinks = document.createElement("span");
            spanLinks.className = "glyphicon glyphicon-link";
            const iText = document.createElement("i");
            iText.textContent = "(has links)";
            linksDiv.appendChild(spanLinks);
            linksDiv.appendChild(iText);
        }
        if (docs[i].hasOwnProperty("has_pdf") && docs[i].has_pdf === true) {
            const spanLinks = document.createElement("span");
            spanLinks.className = "glyphicon glyphicon-file";
            const iText = document.createElement("i");
            iText.textContent = "(PDF)";
            linksDiv.appendChild(spanLinks);
            linksDiv.appendChild(iText);
        }

        linksCol.appendChild(linksDiv);

        resultRow.appendChild(linksCol);

        //tags column
        const tagsCol = document.createElement("td");
        if (docs[i].hasOwnProperty("subject")){

            for (let j = 0; j < docs[i].subject.length; j++){
                if (docs[i].subject[j].length > 40) {
                    continue;

                } else{
                    const tagsDiv = document.createElement("div");
                    tagsDiv.className = "widget-26-job-category bg-soft-info";
                    const indicator = document.createElement("i");
                    indicator.className = "indicator bg-info";
                    const aTag = document.createElement("a");
                    aTag.href = getLinkForTagSearch(docs[i].subject[j]);
                    const textSpan = document.createElement("span");
                    textSpan.textContent = docs[i].subject[j];
                    aTag.appendChild(textSpan);

                    tagsDiv.appendChild(indicator);
                    tagsDiv.appendChild(aTag);

                    tagsCol.appendChild(tagsDiv);

                }
            }

        }
        resultRow.appendChild(tagsCol);

        //append row to the table body
        searchResultsTableBody.appendChild(resultRow);

        loopIndex++;
    }

    if ((new URLSearchParams(window.location.search)).has('q')){

        const qString = (new URLSearchParams(window.location.search)).get('q');
        console.log(qString);
        // console.log(decodeURI(qString));
        document.getElementById("hiddenQ").value = (new URLSearchParams(window.location.search)).get('q');
        document.getElementById("refineQuery").value = (new URLSearchParams(window.location.search)).get('q');

        console.log(document.getElementById("refineQuery").value);
    }
    if ((new URLSearchParams(window.location.search)).has("query")){
        // console.log(escapeHtml((new URLSearchParams(window.location.search)).get("query")));
        // console.log((new URLSearchParams(window.location.search)).get("query"));
        document.getElementById("hiddenQ").value = (new URLSearchParams(window.location.search)).get("query");
        document.getElementById("refineQuery").value = (new URLSearchParams(window.location.search)).get("query");
    }

    //pagination
    const numSidePages = 4;
    const hasPreviousPage = (cursor >= batchSize);
    const previousPage = (cursor-batchSize);
    const hasNextPage = (totalCount-batchSize);
    const nextPage = (cursor+batchSize);
    const startPageCursor = Math.max(0, (cursor - numSidePages*batchSize));
    const endPageCursor = Math.min (totalCount, (cursor + numSidePages*batchSize));

    const paginationList = document.getElementById("paginationList");
    if (hasPreviousPage){
        const previousPageListItem = document.createElement("li");
        previousPageListItem.className = "page-item";
        const aTag = document.createElement("a");
        aTag.className = "page-link no-border";
        aTag.href = getLinkForPage(previousPage);
        const spanSymbol = document.createElement("span");
        spanSymbol.textContent = '\u00AB';
        const spanText = document.createElement("span");
        spanText.className = "sr-only";
        spanText.textContent = "Previous"
        aTag.appendChild(spanSymbol);
        aTag.appendChild(spanText);
        previousPageListItem.appendChild(aTag);
        paginationList.appendChild(previousPageListItem);
    }

    let currentPageCursor = startPageCursor;
    for (let k = 0; k < (numSidePages*2 + 1); k++){
        const pageNumber = (currentPageCursor/batchSize + 1);
        const currentPageListItem = document.createElement("page-item");
        currentPageListItem.className = "page-item";
        const aPageNumber = document.createElement("a");
        aPageNumber.className = "page-link no-border";
        aPageNumber.href = getLinkForPage(currentPageCursor);
        aPageNumber.textContent = String(pageNumber);
        //check if list item should be active
        if(currentPageCursor === cursor ){
            currentPageListItem.className += " active";
        }
        //increment the current page cursor
        currentPageCursor += batchSize;
        if(currentPageCursor > endPageCursor){
            break;
        }

        currentPageListItem.appendChild(aPageNumber);
        paginationList.appendChild(currentPageListItem);
    }

    if (hasNextPage){
        const nextPageListItem = document.createElement("li");
        nextPageListItem.className = "page-item";
        const aTag = document.createElement("a");
        aTag.className = "page-link no-border";
        aTag.href = getLinkForPage(nextPage);
        const spanSymbol = document.createElement("span");
        spanSymbol.textContent = '\u00BB';
        const spanText = document.createElement("span");
        spanText.className = "sr-only";
        spanText.textContent = "Next"
        aTag.appendChild(spanText);
        aTag.appendChild(spanSymbol);
        nextPageListItem.appendChild(aTag);
        paginationList.appendChild(nextPageListItem);
    }

}