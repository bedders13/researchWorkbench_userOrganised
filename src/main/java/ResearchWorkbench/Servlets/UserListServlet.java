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

    /**
     * Get all the user lists when a user is viewing their lists or get all the recommended user lists depending on
     * request parameter
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the request parameters
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        //fetch the bookmarks from the db
        BusinessLayer layer = new BusinessLayer();
        ArrayList<UserList> userLists = new ArrayList<UserList>();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //show all the user lists on the user's reading list page
        if (method.equals("userListPage")){
            //get all the user's reading lists
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

            //show all the user's reading list names
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

        //show the recommended user lists when viewing an ETD
        if (method.equals("showPage")){
            //get the ETD id parameter
            String objectId = request.getParameter("objectId");
            //find all user lists containing the given ETD
            userLists = layer.getUserListsContainingListItem(objectId);

            out.println("<a class=\"d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom\">");
            out.println("<span class=\"fs-5 fw-semibold\">User Lists</span></a>");

            //if there aren't any user lists containing the given ETD, show nothing
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

    /**
     * Create, delete or update  a user list depending on the request parameter
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the request parameters
        String method = request.getParameter("method");
        int userId = Integer.parseInt(request.getParameter("userId"));
        //create business layer to access db
        BusinessLayer layer = new BusinessLayer();

        //create a new user list
        if (method.equals("create")){
            //get the request parameters
            String listName = request.getParameter("listName");
            boolean checked = Boolean.parseBoolean(request.getParameter("isPrivate"));
            //create the UserList object
            UserList userList = new UserList(listName,checked, userId);
            //create the user list
            userList.setUserListId(layer.createUserList(userList));
        }
        //delete a user list
        if (method.equals("delete")){
            int userListId = Integer.parseInt(request.getParameter("userListId"));

            layer.deleteUserList(userListId, userId);
        }
        //update the user list with new privacy
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
