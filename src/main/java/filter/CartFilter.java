package filter;

import domain.Contract;
import domain.Role;
import domain.User;
import service.ContractService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter for cart operations
 */
public class CartFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String actionPath = req.getRequestURI();
        String refPath = req.getHeader("referer");

        Pattern cartUrlPattern = Pattern.compile("^/cart/(\\d+)/.*");
        Matcher cartUrlMatcher = cartUrlPattern.matcher(actionPath);

        ContractService contractSvc = new ContractService();

        if (cartUrlMatcher.matches()) {
            long contractId = Long.parseLong(cartUrlMatcher.group(1));
            Contract contract = contractSvc.getById(contractId);

            if (currentUser.getRole() == Role.EMPLOYEE || contract.getClient().getUser() == currentUser) {
                filterChain.doFilter(req, resp);
            }
            else {
                if (refPath == null) {
                    refPath = "/my";
                }
                resp.sendRedirect(refPath);
            }

        }



    }

    @Override
    public void destroy() {

    }
}
