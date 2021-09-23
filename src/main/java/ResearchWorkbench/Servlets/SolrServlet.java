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
    //the address of the Solr instance
    String solrUrl = "http://localhost:8983/solr/collection1";

    /**
     * Method gets the ETD metadata from a Solr instance running at the above solrUrl
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //read the get parameters
        String queryType = request.getParameter("queryType");
        String queryString = request.getParameter("queryString");
        String queryUrl = getQueryUrl(queryType, queryString);

        //read the json
        JSONObject jsonResults = readJsonFromUrl(solrUrl + queryUrl);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //send the json back to the front-end
        PrintWriter responseWriter = response.getWriter();
        responseWriter.print(jsonResults.toString());
        responseWriter.close();
    }

    /**
     * Creates the query url to send to Solr based on the query type
     * @param type the type of query to send to Solr
     * @param queryString the search query to send to Solr
     * @return returns the formatted query url to send to Solr
     */
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

    /**
     * Method builds a string when passed a BufferedReader
     * @param rd reader used to build the string from
     * @return returns the json text
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Reads a JSON file from a url
     * @param url the url to read the json from
     * @return returns a JSON object of the returned data from the url
     * @throws IOException
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException {
//        url = URLEncoder.encode(url, "UTF-8");
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
            return  new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }
}
