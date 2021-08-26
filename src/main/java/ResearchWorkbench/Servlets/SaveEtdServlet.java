package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SaveEtdServlet", value = "/SaveEtdServlet")
public class SaveEtdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        String objectId = request.getParameter("id");
        int userId = Integer.parseInt(request.getParameter("userId"));

        BusinessLayer layer = new BusinessLayer();

        response.setContentType("application/json");

        PrintWriter responseWriter = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        if (method.equals("check")){
            boolean bookmarked = layer.isObjectBookmarked(objectId, userId);
            jsonObject.put("bookmarked", bookmarked);
            responseWriter.print(jsonObject.toString());
            responseWriter.close();
        }
        if (method.equals("bookmark")){
            String title = request.getParameter("title");
            String author = request.getParameter("creator");
            String date = request.getParameter("date");
            Bookmark bookmark = new Bookmark(objectId, title, author, date, userId);
            int result = layer.createBookmark(bookmark);
            if (result != -1){
                jsonObject.put("bookmarked", true);
            } else {
                jsonObject.put("bookmarked", false);
            }

            responseWriter.print(jsonObject.toString());
            responseWriter.close();
        }
    }
}
