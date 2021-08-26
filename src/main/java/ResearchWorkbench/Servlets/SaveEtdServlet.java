package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.UserList;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "SaveEtdServlet", value = "/SaveEtdServlet")
public class SaveEtdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        BusinessLayer layer = new BusinessLayer();
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        userLists = layer.getUserLists(userId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        for (UserList userList : userLists) {
            out.println("<a onclick=\"addEtdToList(" + userList.getUserListId() + ")\" class=\"list-group-item list-group-item-action\">");
            out.println("<p class=\"mb-1\" style=\"color: black\">" + userList.getUserListName()+ "</p>");
            if (userList.getIsPrivate()){
                out.println("<small>Private</small>");
            } else {
                out.println("<small>Public</small>");
            }
        }
        out.close();
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

        if (method.equals("checkBookmark")){
            boolean bookmarked = layer.isObjectBookmarked(objectId, userId);
            jsonObject.put("bookmarked", bookmarked);
            responseWriter.print(jsonObject.toString());
            responseWriter.close();
        }
        if (method.equals("checkList")){
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
        if (method.equals("list")){
            String title = request.getParameter("title");
            String author = request.getParameter("creator");
            String date = request.getParameter("date");
            int userListId = Integer.parseInt(request.getParameter("userListId"));
            ListItem listItem = new ListItem(objectId, title, author, date, userListId);
            int result = layer.createListItem(listItem);
            if (result != -1){
                jsonObject.put("added", true);
            } else {
                jsonObject.put("added", false);
            }

            responseWriter.print(jsonObject.toString());
            responseWriter.close();
        }
    }
}
