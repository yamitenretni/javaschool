package servlets;

import domain.Client;
import domain.Contract;
import domain.ContractOption;
import domain.ContractTariff;
import domain.User;
import form.CartContractForm;
import form.CartForm;
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
    private final ContractService contactSvc = new ContractService();
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
     * URL regexp for deactivating contract option.
     */
    private final Pattern deactivateOptionPattern = Pattern.compile("^/contracts/(\\d+)/deactivate/(\\d+)$");

    /**
     * URL regexp for cancel deactivation of option from cart.
     */
    private final Pattern cancelDeactivateOptionPattern = Pattern.compile("^/contracts/(\\d+)/deactivate/(\\d+)/cancel$");

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

        Matcher deactivateOptionMatcher = deactivateOptionPattern.matcher(actionPath);
        Matcher cancelDeactivateOptionMatcher = cancelDeactivateOptionPattern.matcher(actionPath);

        /**
         * Get client list.
         */
        if (clientListMatcher.matches()) {
            req.setAttribute("clients", clientSvc.getClients());
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

            req.setAttribute("contract", contactSvc.getById(contractId));
            req.getRequestDispatcher("/jsp/read-contract.jsp").forward(req, resp);
        }

        /**
         * Deactivate contract option.
         */
        if (deactivateOptionMatcher.matches()) {
            CartForm cartForm;
            CartContractForm cartContractForm;

            long contractId = Long.parseLong(deactivateOptionMatcher.group(1));
            long optionId = Long.parseLong(deactivateOptionMatcher.group(2));
            Contract contract = contactSvc.getById(contractId);
            ContractOption option = optionSvc.getById(optionId);

            HttpSession session = req.getSession();
            if (session.getAttribute("cartForm") != null) {
                cartForm = (CartForm) session.getAttribute("cartForm");

            } else {
                cartForm = new CartForm();
            }

            cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.addDeactivatedOption(option);

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect("/contracts/" + contractId);
        }

        /**
         * Delete deactivated option from the cart.
         */
        if (cancelDeactivateOptionMatcher.matches()) {
            long contractId = Long.parseLong(cancelDeactivateOptionMatcher.group(1));
            long optionId = Long.parseLong(cancelDeactivateOptionMatcher.group(2));
            Contract contract = contactSvc.getById(contractId);
            ContractOption option = optionSvc.getById(optionId);

            HttpSession session = req.getSession();
            CartForm cartForm = (CartForm) session.getAttribute("cartForm");

            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.deleteDeactivatedOption(option);

            resp.sendRedirect(refPath);
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
            User newClientUser = new User(req.getParameter("email"), req.getParameter("password"));
            Date birthDate = null;
            try {
                birthDate = format.parse(req.getParameter("birthDate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Client newClient = new Client(newClientUser,
                    new ArrayList<Contract>(),
                    req.getParameter("firstName"),
                    req.getParameter("lastName"),
                    birthDate,
                    req.getParameter("passport"),
                    req.getParameter("address"));
            HttpSession session = req.getSession();
            session.setAttribute("newClient", newClient);

            resp.sendRedirect("/clients/add/step2");
        }

        /**
         * Second step of client registration wizard submit.
         */
        if (clientAddSecondMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            // TODO: 26.11.2015 add redirect to the first step if session doesn't contain client
            HttpSession session = req.getSession();
            Client newClient = (Client) session.getAttribute("newClient");
            ContractTariff tariff = tariffSvc.getById(Long.parseLong(req.getParameter("contractTariff")));
            List<ContractOption> activatedOptions = new ArrayList<>();
            String[] selectedOptions = req.getParameterValues("selectedOptions[]");
            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    activatedOptions.add(optionSvc.getById(Long.parseLong(optionId)));
                }
            }

            Contract contract = new Contract(newClient, req.getParameter("contractNumber"), tariff, activatedOptions);
            session.setAttribute("newContract", contract);

            resp.sendRedirect("/clients/add/step3");

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
            newContract = contactSvc.upsertContract(newContract);

            List<Contract> newClientContracts = newClient.getContracts();

            newClientContracts.add(newContract);
            newClient.setContracts(newClientContracts);

            resp.sendRedirect("/clients");
        }
    }
}
