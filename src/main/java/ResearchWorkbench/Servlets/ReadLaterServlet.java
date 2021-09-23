package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.UserList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.print.Book;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ReadLaterServlet", value = "/ReadLaterServlet")
public class ReadLaterServlet extends HttpServlet {
    /**
     * Get the bookmarked items and send back the HTML response to the front-end
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the parameters
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        //create business layer to access db
        BusinessLayer library = new BusinessLayer();
        //fetch the bookmarks from the db
        ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
        bookmarks = library.getBookmarks(userId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //if the bookmarks are being shown under read later on the user list page, show the Read Later heading
        if(method.equals("list")){
            out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
            out.println("<span class=\"fs-5 fw-semibold\" style=\"margin-bottom: 12px\">Read Later</span>");
            out.println("</a>");
        }

        //if there are no bookmarks, show message
        if (bookmarks.size() == 0){
            out.println("<p style=\"text-align: center; margin-top: 8px; text-decoration: none\" >You currently have no saved Electronic Theses or Dissertations in your Read Later.</p>");
        } else {
            for (int i = 0; i < bookmarks.size(); i++){
                out.println("<a onmouseover=\"showButtons('del-" + bookmarks.get(i).getBookmarkId() +  "')\" onmouseout=\"hideButtons('del-" + bookmarks.get(i).getBookmarkId() +  "')\" onclick=\"showBookmark('" + bookmarks.get(i).getObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                out.println("<div class=\"row\">");
                out.println("<div class=\"col-lg-10\">");
                out.println("<div class=\"d-flex w-100 justify-content-between\">");
                out.println("<h5 class=\"mb-1\">" + bookmarks.get(i).getObjectTitle() + " </h5>");
                out.println("</div>");
                out.println("<p class=\"mb-1\">" + bookmarks.get(i).getObjectAuthor() + "</p>");
                out.println("<small>" + bookmarks.get(i).getObjectDate() + "</small>");
                out.println("</div>");
                out.println("<div class=\"col-lg-2 align-self-center\">");
                out.println("<div id=\"wrapper\">");
                out.println("<button id=\"del-" + bookmarks.get(i).getBookmarkId() + "\" style=\"border: 0; background: none;\" class=\"list-button-hide\" data-bs-toggle=\"modal\" data-bs-target=\"#deleteBookmarkModal\" data-bs-objectId=\"" + bookmarks.get(i).getObjectId() + "\" data-bs-userId=\"" + userId + "\" onclick=\"event.stopPropagation();\"><i class=\"bi bi-x-lg\"></i></Button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println(" </a>");
            }
        }
        out.close();
    }

    /**
     * Delete a bookmark or remove an item from a list
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the request parameters
        String method = request.getParameter("method");
        String objectId = request.getParameter("objectId");
        //create a business layer to access db
        BusinessLayer layer = new BusinessLayer();

        //if the method parameter is bookmark, then delete the bookmark
        if (method.equals("bookmark")){
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean deleted = layer.deleteBookmark(objectId, userId);
        }
        //if the method parameter is list, then remove the list item
        if (method.equals("list")){
            int userListId = Integer.parseInt(request.getParameter("userListId"));
            boolean deleted = layer.deleteListItem(objectId, userListId);
        }
    }
}
