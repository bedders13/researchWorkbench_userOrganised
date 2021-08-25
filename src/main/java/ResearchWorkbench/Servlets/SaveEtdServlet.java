package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.Bookmark;

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
        String userIdStr = request.getParameter("userId");
        int userId = Integer.parseInt(userIdStr);
        //        HttpSession session = request.getSession();
//        int userId = (Integer)session.getAttribute("user_id");

        BusinessLayer layer = new BusinessLayer();

        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");

        PrintWriter responseWriter = response.getWriter();


        if (method.equals("check")){
            boolean bookmarked = layer.isObjectChecked(objectId);
            responseWriter.print("{ response: " + bookmarked + " }");
            responseWriter.close();
        }
        if (method.equals("bookmark")){
            Bookmark bookmark = new Bookmark(objectId, userId);
            int result = layer.createBookmark(bookmark);
            responseWriter.print("{ response: " + true + " }");
            responseWriter.close();
        }
    }
}
