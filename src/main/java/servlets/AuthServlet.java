package servlets;

import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet for login and logout.
 */
public class AuthServlet extends HttpServlet {
    /**
     * URL regexp for login.
     */
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^/login$");

    /**
     * URL regexp for login.
     */
    private static final Pattern LOGOUT_PATTERN = Pattern.compile("^/logout$");

    private static final UserService USER_SVC = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher loginMatcher = LOGIN_PATTERN.matcher(actionPath);
        Matcher logoutMatcher = LOGOUT_PATTERN.matcher(actionPath);

        if (loginMatcher.matches()) {
            req.setAttribute("refpath", req.getParameter("refpath"));
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
        if (logoutMatcher.matches()) {
            req.getSession().setAttribute("currentUser", null);
            resp.sendRedirect("/login");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();

        Matcher loginMatcher = LOGIN_PATTERN.matcher(actionPath);

        if (loginMatcher.matches()) {
            List<String> validErrs = new ArrayList<>();

            String userLogin = req.getParameter("login");
            String userPassword = req.getParameter("password");
            String refPath = req.getParameter("refpath");

            long userId = USER_SVC.checkUser(userLogin, userPassword);

            if (userId == 0L) {
                validErrs.add("noSuchUser");
                req.setAttribute("errors", validErrs);
                req.setAttribute("refpath", refPath);
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            }
            else {
                req.getSession().setAttribute("currentUser", USER_SVC.getById(userId));
                resp.sendRedirect(refPath);
            }
        }

    }
}
