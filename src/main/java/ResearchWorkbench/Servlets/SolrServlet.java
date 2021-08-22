package ResearchWorkbench.Servlets;

import com.oracle.javafx.jmx.json.JSONException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.*;

@WebServlet(name = "SolrServlet", value = "/SolrServlet")
public class SolrServlet extends HttpServlet {
    String solrUrl = "http://20.87.26.56:8983/solr/collection1";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //read the get parameters
        String queryType = request.getParameter("queryType");
        String queryString = request.getParameter("queryString");
//        String queryStringUrl = URLEncoder.encode(queryString, "UTF-8");
        //build the query url
//        if(queryString.contains(" "))
//            queryString = queryString.replace(" ", "%20");
        String queryUrl = getQueryUrl(queryType, queryString);

        //read the json
        JSONObject jsonResults = readJsonFromUrl(solrUrl + queryUrl);
//        String jsonResultsString = jsonResults.toString();
        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");

        PrintWriter responseWriter = response.getWriter();
        responseWriter.print(jsonResults.toString());
        responseWriter.close();
//        responseWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getQueryUrl(String type, String queryString){
        String queryUrl;
        switch (type){
            case "main_query":
                queryUrl = "/main_query?" + queryString;
                break;
            case "get":
                queryUrl = "/get?" + queryString;
                break;
            default:
                queryUrl = "/select?" +queryString;
        }
        return queryUrl;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException {
//        url = URLEncoder.encode(url, "UTF-8");
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
            return  new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }
}
