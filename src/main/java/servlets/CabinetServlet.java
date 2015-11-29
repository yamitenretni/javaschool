package servlets;

import domain.Client;
import domain.User;
import service.ClientService;
import service.ContractService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet for client's personal area.
 */
public class CabinetServlet extends HttpServlet {
    /**
     * URL regexp for the main page of cabinet.
     */
    private static final Pattern MAIN_PAGE_PATTERN = Pattern.compile("^/my$");

    /**
     * Get service for contracts.
     */
    private static final ContractService CONTRACT_SVC = new ContractService();

    /**
     * Get service for clients.
     */
    private static final ClientService CLIENT_SVC = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionPath = req.getRequestURI();

        Matcher mainPageMatcher = MAIN_PAGE_PATTERN.matcher(actionPath);

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        Client currentClient = CLIENT_SVC.getByUser(currentUser);

        if (mainPageMatcher.matches()) {
            req.setAttribute("contracts", CONTRACT_SVC.getByClient(currentClient));
            req.getRequestDispatcher("/jsp/my-contracts.jsp").forward(req, resp);
        }
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
