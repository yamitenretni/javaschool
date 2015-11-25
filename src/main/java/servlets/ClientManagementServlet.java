package servlets;

import domain.Client;
import domain.Contract;
import domain.User;
import service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManagementServlet extends HttpServlet {
    /**
     * Get service for clients
     */
    private final ClientService clientService = new ClientService();

    /**
     * URL regexp for the first step of client adding
     */
    private final Pattern clientAddFirstPattern = Pattern.compile("^/clients/add/step1$");

    /**
     * Date format for parsing date.
     */
    private final DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/add-client-step1.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);

        if (clientAddFirstMatcher.matches()) {
            User newClientUser = new User(req.getParameter("email"), req.getParameter("password"));
            Date birthDate = null;
            try {
                birthDate = format.parse(req.getParameter("birthDate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Client newClient = new Client(newClientUser, new ArrayList<Contract>(), req.getParameter("firstName"), req.getParameter("lastName"), birthDate, req.getParameter("passport"), req.getParameter("address"));
            HttpSession session = req.getSession();
            session.setAttribute("newClient", newClient);
        }

        resp.sendRedirect("/tariffs");
//        resp.sendRedirect("/clients/add/step2");
    }
}
