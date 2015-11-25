package servlets;

import domain.Client;
import domain.Contract;
import domain.ContractOption;
import domain.User;
import service.ClientService;
import service.ContractOptionService;
import service.ContractTariffService;

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
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManagementServlet extends HttpServlet {
    /**
     * Get service for clients
     */
    private final ClientService clientService = new ClientService();
    /**
     * Get service for tariffs
     */
    private final ContractTariffService contractTariffService = new ContractTariffService();
    /**
     * Get service for options
     */
    private final ContractOptionService contractOptionService = new ContractOptionService();

    /**
     * URL regexp for the first step of client adding
     */
    private final Pattern clientAddFirstPattern = Pattern.compile("^/clients/add/step1$");
    /**
     * URL regexp for the first step of client adding
     */
    private final Pattern clientAddSecondPattern = Pattern.compile("^/clients/add/step2$");

    /**
     * Date format for parsing date.
     */
    private final DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);

        if (clientAddFirstMatcher.matches()) {
            req.getRequestDispatcher("/jsp/add-client-step1.jsp").forward(req, resp);
        }
        if (clientAddSecondMatcher.matches()) {
            req.setAttribute("tariffs", contractTariffService.getActiveTariffs());
            req.getRequestDispatcher("/jsp/add-client-step2.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        final String actionPath = req.getRequestURI();
        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);

        /**
         * First step of client adding wizard submit
         */
        if (clientAddFirstMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
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

            resp.sendRedirect("/clients/add/step2");
        }

        /**
         * Second step of client adding wizard submit
         */
        if (clientAddSecondMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            // TODO: 26.11.2015 add redirect to the first step if session doesn't contain client
            HttpSession session = req.getSession();
            Client newClient = (Client) session.getAttribute("newClient");
            List<ContractOption> activatedOptions = new ArrayList<>();
            String[] selectedOptions = req.getParameterValues("selectedOptions[]");
            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    activatedOptions.add(contractOptionService.getById(Long.parseLong(optionId)));
                }
            }

            Contract contract = new Contract(newClient, req.getParameter("contractNumber"), activatedOptions);
            session.setAttribute("newContract", contract);

            resp.sendRedirect("/clients/add/step3");

        }
    }
}
