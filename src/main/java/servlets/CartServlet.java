package servlets;

import domain.Contract;
import domain.ContractOption;
import domain.ContractTariff;
import form.CartContractForm;
import form.CartForm;
import service.ContractOptionService;
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
     * URL regexp for edit contract tariff.
     */
    private static final Pattern EDIT_TARIFF = Pattern.compile("^/cart/(\\d+)/newtariff$");
    /**
     * URL regexp for cancel editing tariff.
     */
    private static final Pattern CANCEL_EDIT_TARIFF = Pattern.compile("^/cart/(\\d+)/newtariff/cancel$");
    /**
     * URL regexp for saving changes.
     */
    private static final Pattern SAVE_CHANGES = Pattern.compile("^/cart/(\\d+)/save$");
    /**
     * URL regexp for saving changes.
     */
    private static final Pattern CLEAR_CART = Pattern.compile("^/cart/(\\d+)/clear$");

    /**
     * Get service for tariffs.
     */
    private static final ContractService CONTRACT_SVC = new ContractService();
    /**
     * Get service for options.
     */
    private static final ContractOptionService OPTION_SVC = new ContractOptionService();

    /**
     * Get service for tariffs.
     */
    private static final ContractTariffService TARIFF_SVC = new ContractTariffService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher editTariffMatcher = EDIT_TARIFF.matcher(actionPath);

        if (editTariffMatcher.matches()) {
            CartForm cartForm;
            CartContractForm cartContractForm;

            long contractId = Long.parseLong(editTariffMatcher.group(1));
            long tariffId = Long.parseLong(req.getParameter("newTariff"));
            Contract contract = CONTRACT_SVC.getById(contractId);
            ContractTariff tariff = TARIFF_SVC.getById(tariffId);

            HttpSession session = req.getSession();
            cartForm = getSessionCartForm(session);
            cartContractForm = cartForm.getCartContractForm(contract);

            cartContractForm.changeTariff(tariff);
            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect(refPath);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher deactivateOptionMatcher = DEACTIVATE_OPTION.matcher(actionPath);
        Matcher cancelDeactivateOptionMatcher = CANCEL_DEACTIVATE_OPTION.matcher(actionPath);
        Matcher addOptionMatcher = ADD_OPTION.matcher(actionPath);
        Matcher cancelAddOptionMatcher = CANCEL_ADD_OPTION.matcher(actionPath);
        Matcher cancelEditTariff = CANCEL_EDIT_TARIFF.matcher(actionPath);
        Matcher saveChanges = SAVE_CHANGES.matcher(actionPath);
        Matcher clearCart = CLEAR_CART.matcher(actionPath);

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

            resp.sendRedirect(refPath);
        }

        /**
         * Add contract option.
         */
        else if (addOptionMatcher.matches()) {
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
        else if (cancelDeactivateOptionMatcher.matches()) {
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
        else if (cancelAddOptionMatcher.matches()) {
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

        /**
         * Cancel tariff edit.
         */
        else if (cancelEditTariff.matches()) {
            long contractId = Long.parseLong(cancelEditTariff.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            HttpSession session = req.getSession();
            CartForm cartForm = getSessionCartForm(session);

            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);
            cartContractForm.deleteNewTariff();

            session.setAttribute("cartForm", cartForm);

            resp.sendRedirect(refPath);
        }

        /**
         * Save changes of contract.
         */
        else if (saveChanges.matches()) {
            long contractId = Long.parseLong(saveChanges.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            HttpSession session = req.getSession();
            CartForm cartForm = getSessionCartForm(session);
            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);

            List<ContractOption> deactivatedOptions = cartContractForm.getDeactivatedOptions();
            deactivatedOptions.addAll(cartContractForm.getUnsupportedOptions());
            List<ContractOption> newOptions = cartContractForm.getNewOptions();
            ContractTariff newTariff = cartContractForm.getNewTariff();

            if (!deactivatedOptions.isEmpty()) {
                contract.getActivatedOptions().removeAll(deactivatedOptions);
            }

            if (newTariff != null) {
                contract.setTariff(newTariff);
            }

            if (!newOptions.isEmpty()) {
                contract.getActivatedOptions().addAll(newOptions);
            }

            CONTRACT_SVC.upsertContract(contract);

            cartForm.deleteCartContractForm(cartContractForm);

            resp.sendRedirect(refPath);
        }
        else if (clearCart.matches()) {
            long contractId = Long.parseLong(clearCart.group(1));
            Contract contract = CONTRACT_SVC.getById(contractId);

            HttpSession session = req.getSession();
            CartForm cartForm = getSessionCartForm(session);
            CartContractForm cartContractForm = cartForm.getCartContractForm(contract);

            cartContractForm.clearAll();

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
            CartForm cartForm = new CartForm();
            session.setAttribute("cartForm", cartForm);
            return cartForm;
        }
    }
}
