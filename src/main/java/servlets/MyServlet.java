package servlets;

import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.setAttribute("users", userService.getUsers());

        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userLogin = req.getParameter("login");
        String userPassword = req.getParameter("password");
        String loginStatus = "";

        if (userService.checkUser(userLogin, userPassword) != 0L) {
            loginStatus = "success";
        }
        else {
            loginStatus = "fail";
        }

        resp.getWriter().write(loginStatus);
    }
}
