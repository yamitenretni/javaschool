package servlets;

import domain.*;
import service.ClientService;
import service.ContractOptionService;
import service.ContractService;
import service.ContractTariffService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet for working with clients and contracts information.
 */
public class ClientManagementServlet extends HttpServlet {
    /**
     * Get service for clients.
     */
    private final ClientService clientSvc = new ClientService();
    /**
     * Get service for users.
     */
    private final UserService userSvc = new UserService();
    /**
     * Get service for contracts.
     */
    private final ContractService contractSvc = new ContractService();
    /**
     * Get service for tariffs.
     */
    private final ContractTariffService tariffSvc = new ContractTariffService();
    /**
     * Get service for options.
     */
    private final ContractOptionService optionSvc = new ContractOptionService();

    /**
     * URL regexp for the client list.
     */
    private final Pattern clientListPattern = Pattern.compile("^/clients$");
    /**
     * URL regexp for the client page.
     */
    private final Pattern clientPagePattern = Pattern.compile("^/clients/(\\d+)$");
    /**
     * URL regexp for the first step of client adding.
     */
    private final Pattern clientAddFirstPattern = Pattern.compile("^/clients/add/step1$");
    /**
     * URL regexp for the first step of client adding.
     */
    private final Pattern clientAddSecondPattern = Pattern.compile("^/clients/add/step2$");
    /**
     * URL regexp for the third step of client adding.
     */
    private final Pattern clientAddThirdPattern = Pattern.compile("^/clients/add/step3$");

    /**
     * URL regexp for the contract page.
     */
    private final Pattern contractPagePattern = Pattern.compile("^/contracts/(\\d+)$");

    /**
     * Date format for parsing date.
     */
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);
        Matcher clientAddThirdMatcher = clientAddThirdPattern.matcher(actionPath);

        Matcher clientListMatcher = clientListPattern.matcher(actionPath);
        Matcher clientPageMatcher = clientPagePattern.matcher(actionPath);

        Matcher contractPageMatcher = contractPagePattern.matcher(actionPath);

        /**
         * Get client list.
         */
        if (clientListMatcher.matches()) {
            List<Client> clients = clientSvc.getClients();
            req.setAttribute("clients", clients);
            req.getRequestDispatcher("/jsp/clients.jsp").forward(req, resp);
        }
        /**
         * Get client info.
         */
        if (clientPageMatcher.matches()) {
            long clientId = Long.parseLong(clientPageMatcher.group(1));
            req.setAttribute("client", clientSvc.getById(clientId));
            req.getRequestDispatcher("/jsp/read-client.jsp").forward(req, resp);
        }

        /**
         * Get first step of client registration.
         */
        if (clientAddFirstMatcher.matches()) {
            req.getRequestDispatcher("/jsp/add-client-step1.jsp").forward(req, resp);
        }
        /**
         * Get second step of client registration.
         */
        if (clientAddSecondMatcher.matches()) {
            req.setAttribute("tariffs", tariffSvc.getActiveTariffs());
            req.getRequestDispatcher("/jsp/add-client-step2.jsp").forward(req, resp);
        }
        /**
         * Get third step of client registration.
         */
        if (clientAddThirdMatcher.matches()) {
            req.getRequestDispatcher("/jsp/add-client-step3.jsp").forward(req, resp);
        }

        /**
         * Get contract info.
         */
        if (contractPageMatcher.matches()) {
            long contractId = Long.parseLong(contractPageMatcher.group(1));
            Contract contract = contractSvc.getById(contractId);
            List<ContractOption> availableOptions = contractSvc.getAvailableOptions(contract);

            req.setAttribute("contract", contract);
            req.setAttribute("availableOptions", availableOptions);
            req.setAttribute("tariffs", tariffSvc.getActiveTariffs());
            req.getRequestDispatcher("/jsp/read-contract.jsp").forward(req, resp);
        }

    }

    // TODO: 26.11.2015 'Cancel' and 'Previous step' buttons
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String actionPath = req.getRequestURI();
        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);
        Matcher clientAddThirdMatcher = clientAddThirdPattern.matcher(actionPath);

        /**
         * First step of client registration wizard submit.
         */
        if (clientAddFirstMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            List<String> validErrs = new ArrayList<>();

            //Save all parameters
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            Date birthDate = null;
            try {
                birthDate = format.parse(req.getParameter("birthDate"));
            } catch (ParseException e) {
                // TODO: 26.11.2015 Log it
            }
            String passport = req.getParameter("passport");
            String address = req.getParameter("address");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            //Validation
            if (!clientSvc.hasUniquePassport(0L, req.getParameter("passport"))) {
                validErrs.add("notUniquePassport");
            }
            if (!userSvc.hasUniqueLogin(0L, req.getParameter("email"))) {
                validErrs.add("notUniqueLogin");
            }

            if (validErrs.isEmpty()) {
                User newClientUser = new User(email, password, Role.CLIENT);

                Client newClient = new Client(newClientUser,
                        new ArrayList<Contract>(),
                        firstName,
                        lastName,
                        birthDate,
                        passport,
                        address);

                HttpSession session = req.getSession();
                session.setAttribute("newClient", newClient);

                resp.sendRedirect("/clients/add/step2");
            }
            else {
                req.setAttribute("errors", validErrs);
                req.setAttribute("firstName", firstName);
                req.setAttribute("lastName", lastName);
                req.setAttribute("birthDate", birthDate);
                req.setAttribute("passport", passport);
                req.setAttribute("address", address);
                req.setAttribute("email", email);
                doGet(req, resp);
            }
        }

        /**
         * Second step of client registration wizard submit.
         */
        if (clientAddSecondMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            List<String> validErrs = new ArrayList<>();
            // TODO: 26.11.2015 add redirect to the first step if session doesn't contain client

            //Save all parameters
            String number = req.getParameter("contractNumber");
            Long tariffId = Long.parseLong(req.getParameter("contractTariff"));
            ContractTariff tariff = tariffSvc.getById(tariffId);
            String[] selectedOptions = req.getParameterValues("selectedOptions[]");

            List<Long> savedOptions = new ArrayList<>();
            for (String optionId : selectedOptions) {
                savedOptions.add(Long.parseLong(optionId));
            }


            //Validation
            if (!contractSvc.hasUniqueNumber(0L, req.getParameter("contractNumber"))) {
                validErrs.add("notUniqueNumber");
            }

            if (validErrs.isEmpty()) {
                HttpSession session = req.getSession();

                Client newClient = (Client) session.getAttribute("newClient");
                List<ContractOption> activatedOptions = new ArrayList<>();
                if (selectedOptions != null) {
                    for (String optionId : selectedOptions) {
                        activatedOptions.add(optionSvc.getById(Long.parseLong(optionId)));
                    }
                }

                Contract contract = new Contract(newClient, req.getParameter("contractNumber"), tariff, activatedOptions);
                session.setAttribute("newContract", contract);

                resp.sendRedirect("/clients/add/step3");
            }
            else {
                req.setAttribute("errors", validErrs);
                req.setAttribute("number", number);
                req.setAttribute("selectedTariff", tariff);
                req.setAttribute("savedOptions", savedOptions);
                doGet(req, resp);
            }

        }

        /**
         * Third step of client registration wizard submit.
         */
        if (clientAddThirdMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            HttpSession session = req.getSession();
            Contract newContract = (Contract) session.getAttribute("newContract");
            Client newClient = (Client) session.getAttribute("newClient");

            User newUser = userSvc.addUser(newClient.getUser());
            newClient.setUser(newUser);

            newClient = clientSvc.upsertClient(newClient);
            newContract.setClient(newClient);
            newContract = contractSvc.upsertContract(newContract);

            List<Contract> newClientContracts = newClient.getContracts();

            newClientContracts.add(newContract);
            newClient.setContracts(newClientContracts);

            resp.sendRedirect("/clients");
        }
    }
}
