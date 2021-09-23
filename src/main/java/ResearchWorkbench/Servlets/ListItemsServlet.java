package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ListItemsServlet", value = "/ListItemsServlet")
public class ListItemsServlet extends HttpServlet {
    /**
     * Called from the userLists.js to show the list items when viewing a user list
     * Gets all the list items when given a userListId, then sends the HTML back to the front-end
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the parameters
        int userListId = Integer.parseInt(request.getParameter("userListId"));
        //create business layer to access db
        BusinessLayer layer = new BusinessLayer();
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        //get all the list items
        listItems = layer.getListItems(userListId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //show list name
        out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
        out.println("<span class=\"fs-5 fw-semibold\" style=\"margin-bottom: 12px\">" + layer.getUserList(userListId).getUserListName() + "</span>");
        out.println("</a>");

        //if there are no list items, show nothing
        if (listItems.size() == 0){
            out.println("<p style=\"text-align: center; margin-top: 8px; text-decoration: none\" >Add some ETDs to this list.</p>");
        } else {
            for (int i = 0; i < listItems.size(); i++){
                out.println("<a onmouseover=\"showButtons('del-" + listItems.get(i).getListItemId() +  "')\" onmouseout=\"hideButtons('del-" + listItems.get(i).getListItemId() +  "')\" onclick=\"showListItem('" + listItems.get(i).getListObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                out.println("<div class=\"row\">");
                out.println("<div class=\"col-lg-10\">");
                out.println("<div class=\"d-flex w-100 justify-content-between\">");
                out.println("<h5 class=\"mb-1\">" + listItems.get(i).getObjectTitle() + " </h5>");
                out.println("</div>");
                out.println("<p  class=\"mb-1\">" + listItems.get(i).getObjectAuthor() + "</p>");
                out.println("<small>" + listItems.get(i).getObjectDate() + "</small>");
                out.println("</div>");
                out.println("<div class=\"col-lg-2 align-self-center\">");
                out.println("<div id=\"wrapper\">");
                out.println("<button id=\"del-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none;\" class=\"list-button-hide\" data-bs-toggle=\"modal\" data-bs-target=\"#deleteListItemModal\" data-bs-objectId=\"" + listItems.get(i).getListObjectId() + "\" data-bs-listId=\"" + userListId + "\" onclick=\"event.stopPropagation();\"><i class=\"bi bi-x-lg\"></i></Button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</a>");
            }
        }
        out.close();
    }
}
