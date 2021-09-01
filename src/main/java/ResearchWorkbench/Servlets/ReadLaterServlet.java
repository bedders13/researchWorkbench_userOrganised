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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the parameters
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        //create business layer to access db
        BusinessLayer library = new BusinessLayer();
        ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
        bookmarks = library.getBookmarks(userId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        if(method.equals("list")){
            out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
            out.println("<span class=\"fs-5 fw-semibold\" style=\"margin-bottom: 12px\">Read Later</span>");
            out.println("</a>");
        }

        if (bookmarks.size() == 0){
            out.println("<p style=\"text-align: center; margin-top: 8px; text-decoration: none\" >You currently have no saved Electronic Theses or Dissertations in your Read Later.</p>");
        } else {
            for (int i = 0; i < bookmarks.size(); i++){
                out.println("<a onclick=\"showBookmark('" + bookmarks.get(i).getObjectId() + "')\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                out.println("<div class=\"d-flex w-100 justify-content-between\">");
                out.println("<h5 class=\"mb-1\">" + bookmarks.get(i).getObjectTitle() + " </h5>");
                out.println("<span class=\"pull-right\">");
//                out.println("<button class=\"btn btn-xs btn-default\" onclick=\"deleteBookmark('" + bookmarks.get(i).getObjectId() +"')\">");
//                out.println("<i class=\"bi bi-x-lg\"></i>");
                out.println("<span class=\"btn btn-xs btn-default\" onclick=\"deleteBookmark('" + bookmarks.get(i).getObjectId() + "',"+ userId + ");event.preventDefault(); event.stopPropagation();\">");
                out.println("<span class=\"bi bi-x-lg\" aria-hidden=\"true\"></span>");
                out.println("</span></span></div>");
                out.println("<p class=\"mb-1\">" + bookmarks.get(i).getObjectAuthor() + "</p>");
                out.println("<small>" + bookmarks.get(i).getObjectDate() + "</small>");
                out.println(" </a>");

            }
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        String objectId = request.getParameter("objectId");
        BusinessLayer layer = new BusinessLayer();

        if (method.equals("bookmark")){
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean deleted = layer.deleteBookmark(objectId, userId);
        }

        if (method.equals("list")){
            int userListId = Integer.parseInt(request.getParameter("userListId"));
            boolean deleted = layer.deleteListItem(objectId, userListId);
        }
    }
}
