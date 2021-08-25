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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //create business layer
        BusinessLayer library = new BusinessLayer();

        String email = request.getParameter("emailAddress");
        User user = library.getUser(email);
        if (request.getParameter("method").equals("create")){
            String userName = request.getParameter("userName");
            user.setUserEmail(email);
            user.setUserName(userName);
            int userId = library.createUser(user);
            user.setUserId(userId);
        }
        JSONObject jsonObject = new JSONObject();
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
