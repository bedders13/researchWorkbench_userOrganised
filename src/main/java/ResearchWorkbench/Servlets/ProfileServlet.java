package ResearchWorkbench.Servlets;

import ResearchWorkbench.Controllers.BusinessLayer;
import ResearchWorkbench.Models.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("emailAddress");
        String userName = request.getParameter("userName");
        //create business layer
        BusinessLayer library = new BusinessLayer();
        User user = library.getUser(email);

        if (user.getUserId() == 0){
            user.setUserEmail(email);
            user.setUserName(userName);
            int userId = library.createUser(user);
            user.setUserId(userId);
        }
        //set the servlet context attribute to get user in the MainServlet
        HttpSession session = request.getSession();
        session.setAttribute("logged_in", new Integer(1));
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getUserName());
        response.sendRedirect("index.jsp");
    }
}
