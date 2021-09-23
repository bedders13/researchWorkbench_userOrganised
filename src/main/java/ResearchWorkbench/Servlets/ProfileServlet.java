package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.User;
import org.json.JSONObject;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    /**
     * Servlet handles User creating profile and logging in
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create business layer
        BusinessLayer library = new BusinessLayer();
        //get the email address of either the user logging in or creating a profile
        String email = request.getParameter("emailAddress");
        //get the user from db if it exists
        User user = library.getUser(email);

        JSONObject jsonObject = new JSONObject();

        //log user in if the method parameter is logIn
        if (request.getParameter("method").equals("logIn")){
            if (user.getUserId() == 0){
                jsonObject.put("exists", false);
            } else {
                jsonObject.put("exists", true);
                jsonObject.put("loggedIn", 1);
                jsonObject.put("userId", user.getUserId());
                jsonObject.put("userName", user.getUserName());

            }
        }

        //create user profile if the method parameter is create
        if (request.getParameter("method").equals("create")) {
            String userName = request.getParameter("userName");
            user.setUserEmail(email);
            user.setUserName(userName);
            int userId = library.createUser(user);
            user.setUserId(userId);

            jsonObject.put("loggedIn", 1);
            jsonObject.put("userId", user.getUserId());
            jsonObject.put("userName", user.getUserName());
        }
        //put parameters into json object
        jsonObject.put("loggedIn", 1);
        jsonObject.put("userId", user.getUserId());
        jsonObject.put("userName", user.getUserName());

        //return json with the response
        response.setContentType("application/json");
        PrintWriter responseWriter = response.getWriter();
        responseWriter.print(jsonObject.toString());
        responseWriter.close();
    }
}
