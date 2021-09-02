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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the parameters
        int userListId = Integer.parseInt(request.getParameter("userListId"));
        //create business layer to access db
        BusinessLayer layer = new BusinessLayer();
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        listItems = layer.getListItems(userListId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
        out.println("<span class=\"fs-5 fw-semibold\" style=\"margin-bottom: 12px\">" + layer.getUserList(userListId).getUserListName() + "</span>");
        out.println("</a>");

        if (listItems.size() == 0){
            out.println("<p style=\"text-align: center; margin-top: 8px; text-decoration: none\" >Add some ETDs to this list.</p>");
//            "    <h5 style=\"text-align: center; margin-top: 64px; text-decoration: none\" >Save ETDs to different lists</h5>\n" +
        } else {
            for (int i = 0; i < listItems.size(); i++){
                out.println("<a onmouseover=\"showButtons('del-" + listItems.get(i).getListItemId() +  "')\" onmouseout=\"hideButtons('del-" + listItems.get(i).getListItemId() +  "')\" onclick=\"showListItem('" + listItems.get(i).getListObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                out.println("<div class=\"d-flex w-100 justify-content-between\">");
                out.println("<h5 class=\"mb-1\">" + listItems.get(i).getObjectTitle() + " </h5>");

//                out.println("<span id=\"li-" + listItems.get(i).getListItemId() + "\" class=\"pull-right list-button-hide\">");
//                out.println("<span class=\"btn btn-xs btn-default\" data-bs-toggle=\"modal\" data-bs-target=\"#deleteListItemModal\" data-bs-objectId=\"" + listItems.get(i).getListObjectId() + "\" data-bs-listId=\"" + userListId + "\" onclick=\"event.stopPropagation();\">");
//                out.println("<span class=\"bi bi-x-lg\" aria-hidden=\"true\"></span>");
//                out.println("</span></span></div> ");
                out.println("<button id=\"del-" + listItems.get(i).getListItemId() + "\" style=\"border: 0; background: none;\" class=\"list-button-hide\" data-bs-toggle=\"modal\" data-bs-target=\"#deleteListItemModal\" data-bs-objectId=\"" + listItems.get(i).getListObjectId() + "\" data-bs-listId=\"" + userListId + "\" onclick=\"event.stopPropagation();\"><i class=\"bi bi-x-lg\"></i></Button>");
                out.println("</div>");

                out.println("<p  class=\"mb-1\">" + listItems.get(i).getObjectAuthor() + "</p>");
                out.println("<small>" + listItems.get(i).getObjectDate() + "</small>");
                out.println("</a>");

            }
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
