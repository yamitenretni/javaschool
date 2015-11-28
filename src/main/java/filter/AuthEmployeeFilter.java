package filter;

import domain.Role;
import domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Authorisation filter.
 */
public class AuthEmployeeFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        String path = req.getRequestURI();

        User currentUser;
        if (session.getAttribute("currentUser") != null) {
            currentUser = (User) session.getAttribute("currentUser");
            if (currentUser.getRole() != Role.EMPLOYEE) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Nothing to do here");
            }
            else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        else {
            String refPathParam = "";
            if (path != null) {
                refPathParam = "?refpath=" + URLEncoder.encode(path, "UTF-8");

            }
            resp.sendRedirect("/login" + refPathParam);
        }
    }

    @Override
    public void destroy() {

    }
}
