package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.UserList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "NewListServlet", value = "/NewListServlet")
public class NewListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String listName = request.getParameter("userListName");
        String checked = request.getParameter("userListIsPrivate");
        boolean isPrivate = true;
        if (checked == null){
            isPrivate = false;
        } else if (checked.equals("on")){
            isPrivate = true;
        }

//        boolean isPrivate = Boolean.parseBoolean(request.getParameter("userListIsPrivate"));
        HttpSession session = request.getSession();
        int userId = (Integer)session.getAttribute("userId");

        //create the UserList object
        UserList userList = new UserList(listName, isPrivate, userId);
        //get the controller methods
        BusinessLayer library = new BusinessLayer();

        //create the user list
        userList.setUserListId(library.createUserList(userList));
        response.sendRedirect("user_lists.jsp");
    }
}
