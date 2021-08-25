package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.UserList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


@WebServlet(name = "UserListServlet", value = "/UserListServlet")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        int userId = Integer.parseInt(userIdStr);
        BusinessLayer library = new BusinessLayer();
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        userLists = library.getUserLists(userId);


        response.getOutputStream().println("<table class=\"table\">\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <th>List Name</th>\n" +
                "        <th>Private</th>\n" +
                "    </tr>\n" +
                "    </thead>\n" +
                "    <tbody>");
        for (UserList userList : userLists) {
            String isPrivate = "";
            if (userList.getIsPrivate()){
                isPrivate = "Yes";
            } else{
                isPrivate = "No";
            }
            response.getOutputStream().println("<tr>");
            response.getOutputStream().println("<td style=\"cursor: pointer;\" data-bs-toggle=\"modal\" data-bs-target=\"#listContentsModal\" data-mdb-name=\"" + userList.getUserListName() + "\" >" + userList.getUserListName() + "</td>");
            response.getOutputStream().println("<td>" + isPrivate + "</td>");
            response.getOutputStream().println("</tr>");

        }
        response.getOutputStream().println("\n" +
                "    </tbody>\n" +
                "\n" +
                "</table>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String listName = request.getParameter("listName");
        String checked = request.getParameter("isPrivate");
        int userId = Integer.parseInt(request.getParameter("userId"));
        boolean isPrivate = true;
        if (checked == null){
            isPrivate = false;
        }


        //create the UserList object
        UserList userList = new UserList(listName, isPrivate, userId);
        //get the controller methods
        BusinessLayer library = new BusinessLayer();

        //create the user list
        userList.setUserListId(library.createUserList(userList));
        response.sendRedirect("user_lists.jsp");
    }
}
