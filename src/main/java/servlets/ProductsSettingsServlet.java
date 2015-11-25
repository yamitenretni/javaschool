package servlets;

import domain.ContractOption;
import service.ContractOptionService;
import service.ContractTariffService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductsSettingsServlet extends HttpServlet {
    /**
     * Get service for options
     */
    private final ContractOptionService contractOptionService = new ContractOptionService();
    /**
     * Get service for tariffs
     */
    private final ContractTariffService contractTariffService = new ContractTariffService();

    /**
     * URL regexp for adding tariff
     */
    private final Pattern tariffAddPattern = Pattern.compile("^/tariffs/add$");
    /**
     * URL regexp for editing tariff
     */
    private final Pattern tariffEditPattern = Pattern.compile("^/tariffs/edit/(\\d+)$");
    /**
     * URL regexp for deleting tariff
     */
    private final Pattern tariffDeletePattern = Pattern.compile("^/tariffs/delete/(\\d+)$");
    /**
     * URL regexp for adding option
     */
    private final Pattern optionAddPattern = Pattern.compile("^/options/add$");
    /**
     * URL regexp for editing option
     */
    private final Pattern optionEditPattern = Pattern.compile("^/options/edit/(\\d+)$");
    /**
     * URL regexp for deleting option
     */
    private final Pattern optionDeletePattern = Pattern.compile("^/options/delete/(\\d+)$");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();

        Matcher tariffAddMatcher = tariffAddPattern.matcher(actionPath);
        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);
        Matcher tariffDeleteMatcher = tariffDeletePattern.matcher(actionPath);

        Matcher optionAddMatcher = optionAddPattern.matcher(actionPath);
        Matcher optionEditMatcher = optionEditPattern.matcher(actionPath);
        Matcher optionDeleteMatcher = optionDeletePattern.matcher(actionPath);

        /**
         * Get tariffs list
         */
        if ("/tariffs".equals(actionPath)) {
            req.setAttribute("tariffs", contractTariffService.getActiveTariffs());
            req.getRequestDispatcher("/jsp/tariffs.jsp").forward(req, resp);
        }

        /**
         * Get tariff add form
         */
        if (tariffAddMatcher.matches()) {
            req.setAttribute("options", contractOptionService.getActiveOptions());
            req.getRequestDispatcher("/jsp/add-tariff.jsp").forward(req, resp);
        }

        /**
         * Get tariff edit form
         */
        if (tariffEditMatcher.matches()) {
            long tariffId = Long.parseLong(tariffEditMatcher.group(1));

            // TODO: 24.11.2015 set map (tariff, checked) instead of two lists
            req.setAttribute("editedTariff", contractTariffService.getById(tariffId));
            req.setAttribute("options", contractOptionService.getActiveOptions());

            req.getRequestDispatcher("/jsp/edit-tariff.jsp").forward(req, resp);
        }

        /**
         * Get tariff delete page and delete form
         */
        if (tariffDeleteMatcher.matches()) {
            long tariffId = Long.parseLong(tariffDeleteMatcher.group(1));

            contractTariffService.deleteTariff(tariffId);

            resp.sendRedirect("/tariffs");
        }

        /**
         * Get options list
         */
        if ("/options".equals(actionPath)) {
            req.setAttribute("options", contractOptionService.getActiveOptions());
            req.getRequestDispatcher("/jsp/options.jsp").forward(req, resp);
        }

        /**
         * Get option add form
         */
        if (optionAddMatcher.matches()) {
            req.getRequestDispatcher("/jsp/add-option.jsp").forward(req, resp);
        }

        /**
         * Get option edit form
         */
        if (optionEditMatcher.matches()) {
            long optionId = Long.parseLong(optionEditMatcher.group(1));

            req.setAttribute("editedOption", contractOptionService.getById(optionId));

            req.getRequestDispatcher("/jsp/edit-option.jsp").forward(req, resp);
        }

        /**
         * Get option delete page and delete form
         */
        if (optionDeleteMatcher.matches()) {
            long optionId = Long.parseLong(optionDeleteMatcher.group(1));

            contractOptionService.deleteOption(optionId);

            resp.sendRedirect("/options");
        }

    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();

        Matcher tariffAddMatcher = tariffAddPattern.matcher(actionPath);
        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);

        Matcher optionAddMatcher = optionAddPattern.matcher(actionPath);
        Matcher optionEditMatcher = optionEditPattern.matcher(actionPath);
        
        // TODO: 23.11.2015 think about normal routing
        /**
         * Save tariff
         */
        if (tariffAddMatcher.matches() || tariffEditMatcher.matches()) {
            long tariffId = 0L;
            if (tariffEditMatcher.matches()) {
                tariffId = Long.parseLong(tariffEditMatcher.group(1));
            }

            String tariffName = req.getParameter("tariff_name");
            Double monthlyPrice = Double.parseDouble(req.getParameter("monthly_cost"));
            List<ContractOption> tariffOptions = new ArrayList<>();
            String[] selectedOptions = req.getParameterValues("selected_options[]");

            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    tariffOptions.add(contractOptionService.getById(Integer.parseInt(optionId)));
                }
            }
            contractTariffService.upsertTariff(tariffId, tariffName, monthlyPrice, tariffOptions);

            resp.sendRedirect("/tariffs");
        }

        /**
         * Save option
         */
        if (optionAddMatcher.matches() || optionEditMatcher.matches()) {
            long optionId = 0L;
            if (optionEditMatcher.matches()) {
                optionId = Long.parseLong(optionEditMatcher.group(1));
            }

            String optionName = req.getParameter("option_name");
            Double connectionCost = Double.parseDouble(req.getParameter("connection_cost"));
            Double monthlyCost = Double.parseDouble(req.getParameter("monthly_cost"));

            contractOptionService.upsertOption(optionId, optionName, connectionCost, monthlyCost);

            resp.sendRedirect("/options");
        }
    }
}
