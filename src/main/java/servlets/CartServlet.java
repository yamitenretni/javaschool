package servlets;

import domain.Contract;
import domain.ContractOption;
import form.CartContractForm;
import form.CartForm;
import service.ContractOptionService;
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
 * Servlet for working with the cart.
 */
public class CartServlet extends HttpServlet {
    /**
     * URL regexp for deactivating contract option.
     */
    private static final Pattern DEACTIVATE_OPTION = Pattern.compile("^/cart/(\\d+)/deactivate/(\\d+)$");
    /**
     * URL regexp for cancel deactivation of option from cart.
     */
    private static final Pattern CANCEL_DEACTIVATE_OPTION = Pattern.compile("^/cart/(\\d+)/deactivate/(\\d+)/cancel$");
    /**
     * URL regexp for adding contract option.
     */
    private static final Pattern ADD_OPTION = Pattern.compile("^/cart/(\\d+)/add/(\\d+)$");
    /**
     * URL regexp for cancel adding contract option.
     */
    private static final Pattern CANCEL_ADD_OPTION = Pattern.compile("^/cart/(\\d+)/add/(\\d+)/cancel$");


    /**
     * Get service for tariffs.
     */
    private static final ContractService CONTRACT_SVC = new ContractService();
    /**
     * Get service for options.
     */
    private static final ContractOptionService OPTION_SVC = new ContractOptionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher deactivateOptionMatcher = DEACTIVATE_OPTION.matcher(actionPath);
        Matcher cancelDeactivateOptionMatcher = CANCEL_DEACTIVATE_OPTION.matcher(actionPath);
        Matcher addOptionMatcher = ADD_OPTION.matcher(actionPath);
        Matcher cancelAddOptionMatcher = CANCEL_ADD_OPTION.matcher(actionPath);

        /**
         * Deactivate contract option.
         */
        if (deactivateOptionMatcher.matches()) {
            CartForm cartForm;
            CartContractForm cartContractForm;

            long contractId = Long.parseLong(deactivateOptionMatcher.group(1));
            long optionId = Long.parseLong(deactivateOptionMatcher.group(2));
            Contract contract = CONTRACT_SVC.getById(contractId);
            ContractOption option = OPTION_SVC.getById(optionId);

            HttpSession session = req.getSession();
            cartForm = getSessionCartForm(session);

            cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.addDeactivatedOption(option);

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect("/contracts/" + contractId);
        }

        /**
         * Add contract option.
         */
        if (addOptionMatcher.matches()) {
            CartForm cartForm;
            CartContractForm cartContractForm;

            long contractId = Long.parseLong(addOptionMatcher.group(1));
            long optionId = Long.parseLong(addOptionMatcher.group(2));
            Contract contract = CONTRACT_SVC.getById(contractId);
            ContractOption option = OPTION_SVC.getById(optionId);

            HttpSession session = req.getSession();
            cartForm = getSessionCartForm(session);

            cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.addNewOption(option);

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect(refPath);
        }

        /**
         * Delete deactivated option from the cart.
         */
        if (cancelDeactivateOptionMatcher.matches()) {
            long contractId = Long.parseLong(cancelDeactivateOptionMatcher.group(1));
            long optionId = Long.parseLong(cancelDeactivateOptionMatcher.group(2));
            Contract contract = CONTRACT_SVC.getById(contractId);
            ContractOption option = OPTION_SVC.getById(optionId);

            HttpSession session = req.getSession();
            CartForm cartForm = getSessionCartForm(session);

            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.deleteDeactivatedOption(option);

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect(refPath);
        }

        /**
         * Delete added option from the cart.
         */
        if (cancelAddOptionMatcher.matches()) {
            long contractId = Long.parseLong(cancelAddOptionMatcher.group(1));
            long optionId = Long.parseLong(cancelAddOptionMatcher.group(2));
            Contract contract = CONTRACT_SVC.getById(contractId);
            ContractOption option = OPTION_SVC.getById(optionId);

            HttpSession session = req.getSession();
            CartForm cartForm = getSessionCartForm(session);

            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.deleteAddedOption(option);

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect(refPath);
        }

    }

    /**
     * Get CartForm object from given session or new CartForm
     *
     * @param session current session
     * @return current or new CartForm
     */
    private CartForm getSessionCartForm(final HttpSession session) {
        if (session.getAttribute("cartForm") != null) {
            return (CartForm) session.getAttribute("cartForm");

        } else {
            return new CartForm();
        }
    }
}
