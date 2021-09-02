package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.UserList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ViewUserListServlet", value = "/ViewUserListServlet")
public class ViewUserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the parameters
        int userListId = Integer.parseInt(request.getParameter("userListId"));
        int currentUserId = Integer.parseInt(request.getParameter("userId"));
        //create business layer to access db
        BusinessLayer layer = new BusinessLayer();
        UserList userList = layer.getUserList(userListId);
        ArrayList<ListItem> listItems = layer.getListItems(userListId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<button style=\"margin-top: 16px;\" class=\"btn  btn-primary btn-sm\" onclick=\"goBack()\"><i class=\"bi bi-chevron-left\"></i> Back </button>");
        out.println("<h1 style=\"margin-top: 16px; margin-bottom: 32px; text-align: center;\">" + userList.getUserListName()  +  "</h1>");
//        out.println("<button id=\"backButton\" class=\"btn btn-primary btn-sm float-right\" style=\"margin-top: 15px; float: left; margin-right: 2px;\" ><i class=\"bi bi-chevron-left\"></i>Back</button>");
        out.println("<p style=\"text-align: center;\">Created by: " + layer.getUser(userList.getUserId()).getUserName() + "</p>");
        out.println("<div class=\"list-group\">");
        out.println("<input type=\"hidden\" id=\"refresh\" value=\"no\">");
        for (int i = 0; i < listItems.size(); i++){
            out.println("<a onmouseover=\"showButtons('" + listItems.get(i).getListItemId() + "')\" onmouseout=\"hideButtons('" + listItems.get(i).getListItemId() +  "')\" onclick=\"showListItem('" + listItems.get(i).getListObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\" li>");
            out.println("<div class=\"d-flex w-100 justify-content-between\">");
            out.println("<h5 class=\"mb-1\">" + listItems.get(i).getObjectTitle() + " </h5>");
            out.println("<div class=\"pull-right\">");
            out.println("<button id=\"list-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none;\" class=\"list-button-hide\" onclick=\"event.stopPropagation();\" data-bs-toggle=\"modal\" data-bs-target=\"#addToListModal\" data-bs-objectId=\"" + listItems.get(i).getListObjectId() + "\"" +
                    "data-bs-objectTitle=\"" + listItems.get(i).getObjectTitle()  + "\" data-bs-objectCreator=\"" + listItems.get(i).getObjectAuthor() + "\" data-bs-objectDate=\"" + listItems.get(i).getObjectDate() + " \" data-bs-ListItemId=\"" + listItems.get(i).getListItemId() + "\" " +
                    "data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title=\"Add To a List\" data-bs-delay={'show':500, 'hide':0}>" +
                    "<i class=\"bi bi-list\"></i></i></Button>");
            if (layer.isObjectBookmarked(listItems.get(i).getListObjectId(), currentUserId)){
                out.println("<button id=\"book-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none;\" value=\"1\" class=\"list-button-hide\" " +
                        "onclick=\"bookmarkBtnClicked(this,'" + listItems.get(i).getListObjectId() + "', '" + listItems.get(i).getObjectTitle() + "', '" + listItems.get(i).getObjectAuthor() +
                        "', '" + listItems.get(i).getObjectDate() + "'); event.stopPropagation();\"" +
                        "data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title=\"Bookmark\" data-bs-delay={'show':500, 'hide':0}>" +
                        "<i class=\"bi bi-bookmark-fill\"></i></Button>");
            } else {
                out.println("<button id=\"book-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none;\" value=\"0\" class=\"list-button-hide\" " +
                        "onclick=\"bookmarkBtnClicked(this,'" + listItems.get(i).getListObjectId() + "', '" + listItems.get(i).getObjectTitle() + "', '" +
                        listItems.get(i).getObjectAuthor() + "', '" + listItems.get(i).getObjectDate() + "'); event.stopPropagation();\"" +
                        "data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title=\"Bookmark\" data-bs-delay={'show':500, 'hide':0}>" +
                        "<i class=\"bi bi-bookmark\"></i></Button>");
            }

            out.println("</div>");
            out.println("</div>");
            out.println("<p class=\"mb-1\">" + listItems.get(i).getObjectAuthor() + "</p>");
            out.println("<small>" + listItems.get(i).getObjectDate() + "</small>");
            out.println("</a>");
        }
        out.println("</div>");
        out.close();


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
