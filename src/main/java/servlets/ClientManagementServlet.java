package servlets;

import domain.*;
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
     * URL regexp for cancel new client registration.
     */
    private static final Pattern CANCEL_ADD_CLIENT_PATTERN = Pattern.compile("^/clients/add/cancel$");
    /**
     * URL regexp for adding new contract.
     */
    private static final Pattern ADD_CONTRACT_PATTERN = Pattern.compile("^/clients/(\\d+)/add-contract");
    /**
     * URL regexp for the client blocking.
     */
    private static final Pattern CLIENT_BLOCK_PATTERN = Pattern.compile("^/clients/(\\d+)/block$");

    /**
     * URL regexp for the client unlocking.
     */
    private static final Pattern CLIENT_UNLOCK_PATTERN = Pattern.compile("^/clients/(\\d+)/unlock$");

    /**
     * URL regexp for the contract page.
     */
    private final Pattern contractPagePattern = Pattern.compile("^/contracts/(\\d+)$");

    /**
     * URL regexp for the contract blocking.
     */
    private static final Pattern CONTRACT_BLOCK_PATTERN = Pattern.compile("^/contracts/(\\d+)/block$");

    /**
     * URL regexp for the contract unlocking.
     */
    private static final Pattern CONTRACT_UNLOCK_PATTERN = Pattern.compile("^/contracts/(\\d+)/unlock$");

    /**
     * URL regexp for client's search.
     */
    private static final Pattern CLIENT_SEARCH_PATTERN = Pattern.compile("^/clients/search$");

    /**
     * Date format for parsing date.
     */
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);
        Matcher clientAddThirdMatcher = clientAddThirdPattern.matcher(actionPath);
        Matcher cancelAddClientMatcher = CANCEL_ADD_CLIENT_PATTERN.matcher(actionPath);

        Matcher clientListMatcher = clientListPattern.matcher(actionPath);
        Matcher clientPageMatcher = clientPagePattern.matcher(actionPath);

        Matcher clientBlockMatcher = CLIENT_BLOCK_PATTERN.matcher(actionPath);
        Matcher clientUnlockMatcher = CLIENT_UNLOCK_PATTERN.matcher(actionPath);

        Matcher contractPageMatcher = contractPagePattern.matcher(actionPath);
        Matcher addContractMatcher = ADD_CONTRACT_PATTERN.matcher(actionPath);

        Matcher contractBlockMatcher = CONTRACT_BLOCK_PATTERN.matcher(actionPath);
        Matcher contractUnlockMatcher = CONTRACT_UNLOCK_PATTERN.matcher(actionPath);

        Matcher searchClientMatcher = CLIENT_SEARCH_PATTERN.matcher(actionPath);

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
        else if (clientPageMatcher.matches()) {
            long clientId = Long.parseLong(clientPageMatcher.group(1));
            req.setAttribute("client", clientSvc.getById(clientId));
            req.getRequestDispatcher("/jsp/read-client.jsp").forward(req, resp);
        }

        /**
         * Get first step of client registration.
         */
        else if (clientAddFirstMatcher.matches()) {
            Client newClient = (Client) session.getAttribute("newClient");

            if (newClient != null) {
                req.setAttribute("firstName", newClient.getFirstName());
                req.setAttribute("lastName", newClient.getLastName());
                req.setAttribute("birthDate", newClient.getBirthDate());
                req.setAttribute("passport", newClient.getPassportData());
                req.setAttribute("address", newClient.getAddress());
                req.setAttribute("email", newClient.getUser().getLogin());
            }

            req.getRequestDispatcher("/jsp/add-client-step1.jsp").forward(req, resp);
        }
        /**
         * Get second step of client registration.
         */
        else if (clientAddSecondMatcher.matches()) {
            Contract newContract = (Contract) session.getAttribute("newContract");

            if (session.getAttribute("newClient") == null) {
                resp.sendRedirect("/clients/add/step1");
            } else {
                if (newContract != null) {
                    req.setAttribute("number", newContract.getNumber());
                    req.setAttribute("selectedTariff", newContract.getTariff());

                    List<Long> savedOptions = new ArrayList<>();
                    for (ContractOption activeOption : newContract.getActivatedOptions()) {
                        savedOptions.add(activeOption.getId());
                    }
                    req.setAttribute("savedOptions", savedOptions);
                }
                req.setAttribute("tariffs", tariffSvc.getActiveTariffs());
                req.getRequestDispatcher("/jsp/add-client-step2.jsp").forward(req, resp);
            }
        }
        /**
         * Get third step of client registration.
         */
        else if (clientAddThirdMatcher.matches()) {
            if (session.getAttribute("newClient") == null) {
                resp.sendRedirect("/clients/add/step1");
            } else if (session.getAttribute("newContract") == null) {
                resp.sendRedirect("/clients/add/step2");
            } else {
                req.getRequestDispatcher("/jsp/add-client-step3.jsp").forward(req, resp);
            }
        }
        /**
         * Cancel client registration.
         */
        else if (cancelAddClientMatcher.matches()) {
            session.setAttribute("newClient", null);
            session.setAttribute("newContract", null);
            resp.sendRedirect("/clients");
        }
        /**
         * Get contract info.
         */
        else if (contractPageMatcher.matches()) {
            long contractId = Long.parseLong(contractPageMatcher.group(1));
            Contract contract = contractSvc.getById(contractId);

            CartForm cartForm;
            CartContractForm cartContractForm = null;

            if (session.getAttribute("cartForm") != null) {
                cartForm = (CartForm) session.getAttribute("cartForm");
                cartContractForm = cartForm.getCartContractForm(contract);
                req.setAttribute("currentCartPosition", cartContractForm);
            }

            List<ContractTariff> availableTariffs = contractSvc.getAvailableTariffs(contract, cartContractForm);
            List<ContractOption> availableOptions = contractSvc.getAvailableOptions(contract, cartContractForm);


            req.setAttribute("contract", contract);
            req.setAttribute("availableOptions", availableOptions);
            req.setAttribute("tariffs", availableTariffs);
            req.setAttribute("incompatibleOptions", contractSvc.getIncompatibleOptions(contract, cartContractForm));
            req.setAttribute("dependOptions", contractSvc.getDependOptions(contract, cartContractForm));
            req.getRequestDispatcher("/jsp/read-contract.jsp").forward(req, resp);
        }
        /**
         * Add new contract.
         */
        else if (addContractMatcher.matches()) {
            long clientId = Long.parseLong(addContractMatcher.group(1));
            Client selectedClient = clientSvc.getById(clientId);
            req.setAttribute("selectedClient", selectedClient);
            req.setAttribute("tariffs", tariffSvc.getActiveTariffs());
            req.getRequestDispatcher("/jsp/add-contract.jsp").forward(req, resp);
        }
        /**
         * Block contract.
         */
        else if (contractBlockMatcher.matches()) {
            long contractId = Long.parseLong(contractBlockMatcher.group(1));
            Contract contract = contractSvc.getById(contractId);

            if (contract.getBlockingDate() == null) {
                contractSvc.blockContract(contract, currentUser);
            }

            resp.sendRedirect(refPath);
        }
        /**
         * Unlock contract.
         */
        else if (contractUnlockMatcher.matches()) {
            long contractId = Long.parseLong(contractUnlockMatcher.group(1));
            Contract contract = contractSvc.getById(contractId);

            if (contract.getBlockingDate() != null) {
                contractSvc.unlockContract(contract);
            }

            resp.sendRedirect(refPath);
        }
        /**
         * Block client.
         */
        else if (clientBlockMatcher.matches()) {
            long clientId = Long.parseLong(clientBlockMatcher.group(1));
            Client client = clientSvc.getById(clientId);

            if (client.getBlockingDate() == null) {
                clientSvc.blockClient(client, currentUser);
            }

            resp.sendRedirect(refPath);
        }
        /**
         * Unlock client.
         */
        else if (clientUnlockMatcher.matches()) {
            long clientId = Long.parseLong(clientUnlockMatcher.group(1));
            Client client = clientSvc.getById(clientId);

            if (client.getBlockingDate() != null) {
                clientSvc.unlockClient(client);
            }

            resp.sendRedirect(refPath);
        }
        /**
         * Search client by contract.
         */
        else if (searchClientMatcher.matches()) {
            String contractNumber = req.getParameter("contract");

            Contract contract = contractSvc.getByNumber(contractNumber);

            if (contract != null) {
                resp.sendRedirect("/clients/" + contract.getClient().getId());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No such client");
            }

        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String actionPath = req.getRequestURI();
        Matcher clientAddFirstMatcher = clientAddFirstPattern.matcher(actionPath);
        Matcher clientAddSecondMatcher = clientAddSecondPattern.matcher(actionPath);
        Matcher clientAddThirdMatcher = clientAddThirdPattern.matcher(actionPath);
        Matcher addContractMatcher = ADD_CONTRACT_PATTERN.matcher(actionPath);

        HttpSession session = req.getSession();

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
                session.setAttribute("newClient", newClient);

                resp.sendRedirect("/clients/add/step2");
            } else {
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
        else if (clientAddSecondMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
            List<String> validErrs = new ArrayList<>();

            //Save all parameters
            String number = req.getParameter("contractNumber");
            Long tariffId = Long.parseLong(req.getParameter("contractTariff"));
            ContractTariff tariff = tariffSvc.getById(tariffId);
            String[] selectedOptions = req.getParameterValues("selectedOptions[]");

            List<Long> savedOptions = new ArrayList<>();
            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    savedOptions.add(Long.parseLong(optionId));
                }
            }


            //Validation
            if (!contractSvc.hasUniqueNumber(0L, req.getParameter("contractNumber"))) {
                validErrs.add("notUniqueNumber");
            }

            if (validErrs.isEmpty()) {
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
            } else {
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
        else if (clientAddThirdMatcher.matches() && "submit".equals(req.getParameter("requestType"))) {
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
            session.setAttribute("newClient", null);
            session.setAttribute("newContract", null);
            resp.sendRedirect("/clients");
        }
        /**
         * Add new contract for client.
         */
        else if (addContractMatcher.matches()){
            List<String> validErrs = new ArrayList<>();

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
                long clientId = Long.parseLong(addContractMatcher.group(1));
                Client selectedClient = clientSvc.getById(clientId);

                List<ContractOption> activatedOptions = new ArrayList<>();
                if (selectedOptions != null) {
                    for (String optionId : selectedOptions) {
                        activatedOptions.add(optionSvc.getById(Long.parseLong(optionId)));
                    }
                }

                Contract newContract = contractSvc.upsertContract(0L, selectedClient, number, tariff, activatedOptions);
                List<Contract> clientContracts = selectedClient.getContracts();
                clientContracts.add(newContract);

                resp.sendRedirect("/clients/" + clientId);
            } else {
                req.setAttribute("errors", validErrs);
                req.setAttribute("number", number);
                req.setAttribute("selectedTariff", tariff);
                req.setAttribute("savedOptions", savedOptions);
                doGet(req, resp);
            }




        }
    }
}
