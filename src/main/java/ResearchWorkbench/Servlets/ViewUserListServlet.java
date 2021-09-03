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
        boolean userLoggedIn = currentUserId != -1;
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
            //if user is not logged in, then don't hide and show buttons on mouse over
            if (userLoggedIn){
                out.println("<a onmouseover=\"showButtons('" + listItems.get(i).getListItemId() + "')\" onmouseout=\"hideButtons('" + listItems.get(i).getListItemId() +  "')\" onclick=\"showListItem('" + listItems.get(i).getListObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\" li>");
            } else {
                out.println("<a onclick=\"showListItem('" + listItems.get(i).getListObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\" li>");
            }
            out.println("<div class=\"row\">");
            out.println("<div class=\"col-lg-10\">");
            out.println("<div class=\"d-flex w-100 justify-content-between\">");
            out.println("<h5 class=\"mb-1\">" + listItems.get(i).getObjectTitle() + " </h5>");
//            out.println("<div class=\"pull-right\">");

            out.println("</div>");

            out.println("<p class=\"mb-1\">" + listItems.get(i).getObjectAuthor() + "</p>");
            out.println("<small>" + listItems.get(i).getObjectDate() + "</small>");

            out.println("</div>");
            out.println("<div class=\"col-lg-2 align-self-center\">");


            if (userLoggedIn) {
                out.println("<div id=\"wrapper\">");
                out.println("<button id=\"list-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none; padding: 0.5rem;\" class=\"btn-lg list-button-hide\" onclick=\"event.stopPropagation();\" data-bs-toggle=\"modal\" data-bs-target=\"#addToListModal\" data-bs-objectId=\"" + listItems.get(i).getListObjectId() + "\"" +
                        "data-bs-objectTitle=\"" + listItems.get(i).getObjectTitle()  + "\" data-bs-objectCreator=\"" + listItems.get(i).getObjectAuthor() + "\" data-bs-objectDate=\"" + listItems.get(i).getObjectDate() + " \" data-bs-ListItemId=\"" + listItems.get(i).getListItemId() + "\">" +
                        "<i class=\"bi bi-plus-circle bi-lg\" title=\"Add to a List\"></i></Button>");
                if (layer.isObjectBookmarked(listItems.get(i).getListObjectId(), currentUserId)){
                    out.println("<button id=\"book-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none; padding: 0.5rem;\" value=\"1\" class=\"btn-lg list-button-hide\" " +
                            "onclick=\"bookmarkBtnClicked(this,'" + listItems.get(i).getListObjectId() + "', '" + listItems.get(i).getObjectTitle() + "', '" + listItems.get(i).getObjectAuthor() +
                            "', '" + listItems.get(i).getObjectDate() + "'); event.stopPropagation();\">" +
                            "<i class=\"bi bi-bookmark-fill\" title=\"Remove\"></i></Button>");
                } else {
                    out.println("<button id=\"book-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none; padding: 0.5rem\" value=\"0\" class=\"btn-lg list-button-hide\" " +
                            "onclick=\"bookmarkBtnClicked(this,'" + listItems.get(i).getListObjectId() + "', '" + listItems.get(i).getObjectTitle() + "', '" +
                            listItems.get(i).getObjectAuthor() + "', '" + listItems.get(i).getObjectDate() + "'); event.stopPropagation();\">" +
                            "<i class=\"bi bi-bookmark\" title=\"Bookmark This\"></i></Button>");
                }
            } else {
//                out.println("<button type=\"button\" class=\"btn-lg\" data-bs-container=\"body\" data-bs-toggle=\"popover\" data-bs-placement=\"left\" data-bs-content=\"Sign in to use these features!\">" +
//                        "<i class=\"bi bi-info-circle\"></i></button>");

                out.println("<div id=\"wrapper\">");
                out.println("<button style=\"border: 0; background: none; padding: 0.5rem;\" class=\"btn-lg disabled\">" +
                        "<i class=\"bi bi-plus-circle bi-lg\" onmouseover=\"showTooltips(this)\" onmouseout=\"hideTooltips(this)\" tabindex=\"0\" data-bs-toggle=\"tooltip\" title=\"Sign in to use these features!\"></i></Button>");
                out.println("<button style=\"border: 0; background: none; padding: 0.5rem\" class=\"btn-lg disabled\">" +
                        "<i class=\"bi bi-bookmark\" onmouseover=\"showTooltips(this)\"0 onmouseout=\"hideTooltips(this)\" tabindex=\"0\" data-bs-toggle=\"tooltip\" title=\"Sign in to use these features!\"></i></Button>");
            }
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
//            out.println("</div>");


            out.println("</a>");
        }
        out.println("</div>");
        out.close();


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
