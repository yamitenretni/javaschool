package servlets;

import domain.*;
import form.CartContractForm;
import form.CartForm;
import service.ClientService;
import service.ContractService;
import service.ContractTariffService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet for client's personal area.
 */
public class CabinetServlet extends HttpServlet {
    /**
     * URL regexp for the main page of the cabinet.
     */
    private static final Pattern MAIN_PAGE_PATTERN = Pattern.compile("^/my$");
    /**
     * URL regexp for the contract page in the cabinet.
     */
    private static final Pattern CONTRACT_PAGE_PATTERN = Pattern.compile("^/my/contracts/(\\d+)$");

    /**
     * URL regexp for blocking contract in the cabinet.
     */
    private static final Pattern CONTRACT_BLOCK_PATTERN = Pattern.compile("^/my/contracts/(\\d+)/block$");
    /**
     * URL regexp for unlocking contract in the cabinet.
     */
    private static final Pattern CONTRACT_UNLOCK_PATTERN = Pattern.compile("^/my/contracts/(\\d+)/unlock");

    /**
     * Get service for contracts.
     */
    private static final ContractService CONTRACT_SVC = new ContractService();
    /**
     * Get service for contracts.
     */
    private static final ContractTariffService TARIFF_SVC = new ContractTariffService();

    /**
     * Get service for clients.
     */
    private static final ClientService CLIENT_SVC = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionPath = req.getRequestURI();
        String refPath = req.getHeader("referer");

        Matcher mainPageMatcher = MAIN_PAGE_PATTERN.matcher(actionPath);
        Matcher contractPageMatcher = CONTRACT_PAGE_PATTERN.matcher(actionPath);

        Matcher contractBlockMatcher = CONTRACT_BLOCK_PATTERN.matcher(actionPath);
        Matcher contractUnlockMatcher = CONTRACT_UNLOCK_PATTERN.matcher(actionPath);

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        Client currentClient = CLIENT_SVC.getByUser(currentUser);

        if (mainPageMatcher.matches()) {
            req.setAttribute("contracts", CONTRACT_SVC.getByClient(currentClient));
            req.getRequestDispatcher("/jsp/my-contracts.jsp").forward(req, resp);
        }
        else if (contractPageMatcher.matches()) {
            long contractId = Long.parseLong(contractPageMatcher.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            CartForm cartForm;
            CartContractForm cartContractForm = null;

            if (session.getAttribute("cartForm") != null) {
                cartForm = (CartForm) session.getAttribute("cartForm");
                cartContractForm = cartForm.getCartContractForm(contract);
                req.setAttribute("currentCartPosition", cartContractForm);
            }

            List<ContractTariff> availableTariffs = CONTRACT_SVC.getAvailableTariffs(contract, cartContractForm);
            List<ContractOption> availableOptions = CONTRACT_SVC.getAvailableOptions(contract, cartContractForm);


            if (contract.getClient().getId() == currentClient.getId()) {
                req.setAttribute("contract", contract);
                req.setAttribute("tariffs", availableTariffs);
                req.setAttribute("availableOptions", availableOptions);
                req.setAttribute("incompatibleOptions", CONTRACT_SVC.getIncompatibleOptions(contract, cartContractForm));
                req.setAttribute("dependOptions", CONTRACT_SVC.getDependOptions(contract, cartContractForm));
                req.getRequestDispatcher("/jsp/read-contract.jsp").forward(req, resp);
            }
            else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't watch this page");
            }
        }
        else if (contractBlockMatcher.matches()) {
            long contractId = Long.parseLong(contractBlockMatcher.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            if (!contract.isBlocked()) {
                CONTRACT_SVC.blockContract(contract, currentUser);
            }
            resp.sendRedirect(refPath);
        }
        else if (contractUnlockMatcher.matches()) {
            long contractId = Long.parseLong(contractUnlockMatcher.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            if (contract.getBlockingUser().getId() == currentUser.getId()) {
                CONTRACT_SVC.unlockContract(contract);
            }
            resp.sendRedirect(refPath);
        }
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
