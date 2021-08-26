package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.UserList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;


@WebServlet(name = "UserListServlet", value = "/UserListServlet")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        BusinessLayer library = new BusinessLayer();
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        userLists = library.getUserLists(userId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
        out.println("<span class=\"fs-5 fw-semibold\">Lists</span>");
        out.println("<span class=\"pull-right\" >");
        out.println("<span class=\"btn btn-xs btn-default\" data-bs-toggle=\"modal\" data-bs-target=\"#createNewUserListModal\">");
        out.println("<span class=\"bi bi-plus-lg\" aria-hidden=\"true\"></span>");
        out.println("</span></span></a>");

        for (UserList userList : userLists) {
            out.println("<a onclick=\"getListItems(" + userList.getUserListId() + ")\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
            out.println("<div class=\"d-flex w-100 justify-content-between\">");
            out.println("<h5 class=\"mb-1\">" + userList.getUserListName() + " </h5>");
            out.println("<span class=\"pull-right\">");
//                out.println("<button class=\"btn btn-xs btn-default\" onclick=\"deleteBookmark('" + bookmarks.get(i).getObjectId() +"')\">");
//                out.println("<i class=\"bi bi-x-lg\"></i>");
            out.println("<span class=\"btn btn-xs btn-default\" onclick=\"deleteUserList(" + userList.getUserListId() + "); event.stopPropagation();\">");
            out.println("<span class=\"bi bi-x-lg\" aria-hidden=\"true\"></span>");
            out.println("</span></span></div>");
            out.println("<select onchange=\"updateUserList(" + userList.getUserListId() + ",this.value); event.stopPropagation();\" class=\"form-select\" aria-label=\"Default select example\">");
            if (userList.getIsPrivate()){
                out.println("<option value=\"0\">Public</option>");
                out.println("<option value=\"1\" selected>Private</option>");
            } else {
                out.println("<option value=\"0\" selected>Public</option>");
                out.println("<option value=\"1\">Private</option>");
            }
            out.println("</select>");
//            out.println("<p class=\"mb-1\">" + userList.getDateCreated() + "</p>");
            out.println(" </a>");


        }
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        //get the controller methods
        BusinessLayer layer = new BusinessLayer();

        if (method.equals("create")){
            String listName = request.getParameter("listName");
            boolean checked = Boolean.parseBoolean(request.getParameter("isPrivate"));


            //create the UserList object
            UserList userList = new UserList(listName,checked, userId);
            //create the user list
            userList.setUserListId(layer.createUserList(userList));
        }

        if (method.equals("delete")){
            int userListId = Integer.parseInt(request.getParameter("userListId"));

            layer.deleteUserList(userListId, userId);
        }
        if (method.equals("update")){
            int userListId = Integer.parseInt(request.getParameter("userListId"));
            int isPrivateInt = Integer.parseInt(request.getParameter("isPrivate"));
            Boolean isPrivate;
            if (isPrivateInt == 0){
                isPrivate = false;
            } else {
                isPrivate = true;
            }
            UserList newUserList = layer.getUserList(userListId);
            newUserList.setIsPrivate(isPrivate);
            layer.updateUserList(newUserList);
        }
    }

}
