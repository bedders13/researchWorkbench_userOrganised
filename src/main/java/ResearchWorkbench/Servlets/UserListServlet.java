package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.ListItem;
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
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        BusinessLayer layer = new BusinessLayer();
        ArrayList<UserList> userLists = new ArrayList<UserList>();


        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (method.equals("userListPage")){

            userLists = layer.getUserLists(userId);
            out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
            out.println("<span class=\"fs-5 fw-semibold\">Lists</span>");
            out.println("<span class=\"pull-right\" >");
            out.println("<span class=\"btn btn-xs btn-default\" data-bs-toggle=\"modal\" data-bs-target=\"#createNewUserListModal\">");
            out.println("<span class=\"bi bi-plus-lg\" aria-hidden=\"true\"></span>");
            out.println("</span></span></a>");

            //show the bookmark/read later items
            out.println("<a onclick=\"getBookmarks()\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
            out.println("<div class=\"d-flex w-100 justify-content-between\">");
            out.println("<h5 class=\"mb-1\">Read Later</h5>");
            out.println("<span class=\"pull-right\">");



            out.println("</span>");
            out.println("</div>");
            out.println("<small>" + layer.getBookmarks(userId).size() + " items</small>");
            out.println("<p><i class=\"bi bi-lock\"></i> Private</p>");
            out.println(" </a>");


            if (userLists.size() != 0){
                for (UserList userList : userLists) {
                    out.println("<a onclick=\"getListItems(" + userList.getUserListId() + ")\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                    out.println("<div class=\"d-flex w-100 justify-content-between\">");
                    out.println("<h5 class=\"mb-1\">" + userList.getUserListName() + " </h5>");
                    out.println("<span class=\"pull-right\">");

                    out.println("<span class=\"btn btn-xs btn-default\" data-bs-toggle=\"modal\" data-bs-target=\"#deleteUserListModal\" data-bs-listId=\"" + userList.getUserListId() + "\" onclick=\"event.stopPropagation();\">");
                    out.println("<span class=\"bi bi-x-lg\" aria-hidden=\"true\"></span>");

                    out.println("</span></span>");

                    out.println("</div>");
                    out.println("<small>" + layer.getListItems(userList.getUserListId()).size() + " items</small>");
                    out.println("<select style=\"margin-top: 8px;\" onchange=\"updateUserList(" + userList.getUserListId() + ",this.value); event.stopPropagation();\" class=\"form-select\" aria-label=\"Default select example\">");
                    if (userList.getIsPrivate()) {
                        out.println("<option value=\"0\">Public</option>");
                        out.println("<option value=\"1\" selected>Private</option>");
                    } else {
                        out.println("<option value=\"0\" selected>Public</option>");
                        out.println("<option value=\"1\">Private</option>");
                    }
                    out.println("</select>");
                    out.println(" </a>");
                }
            }

        }

        if (method.equals("showPage")){
            String objectId = request.getParameter("objectId");
            userLists = layer.getUserListsContainingListItem(objectId);

            out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
            out.println("<span class=\"fs-5 fw-semibold\">User Lists</span></a>");

            if (userLists.size() == 0){
                out.println("<p style=\"text-align: center; margin-top: 8px; text-decoration: none\" >No other user lists contain this paper</p>");
            } else {

                out.println("<div class=\"list-group\">");
                for(UserList userList : userLists){
                    ArrayList<ListItem> listItems = layer.getListItems(userList.getUserListId());
                    out.println("<a href=\"view_user_list.html?id=" + userList.getUserListId() + "\" class=\"list-group-item list-group-item-action\" aria-current=\"true\">");
                    out.println("<div class=\"d-flex w-100 justify-content-between\">");
                    out.println("<h5 class=\"mb-1\">" + userList.getUserListName() + " </h5>");
                    out.println("</div>");
                    out.println("<p class=\"mb-1\"> Created by: " + layer.getUser(userList.getUserId()).getUserName() + "</p>");
                    out.println("<small>" + listItems.size() + " items</small>");
                    out.println(" </a>");
                }
            }
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
