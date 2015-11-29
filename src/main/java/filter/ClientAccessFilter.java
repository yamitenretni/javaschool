package filter;

import domain.Role;
import domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter for personal area pages.
 */
public class ClientAccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getRole() == Role.CLIENT) {
            filterChain.doFilter(req, resp);
        }
        else {
            resp.sendRedirect("/clients");
        }

    }

    @Override
    public void destroy() {

    }
}
