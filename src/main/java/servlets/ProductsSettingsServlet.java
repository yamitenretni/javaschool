package servlets;

import domain.Contract;
import domain.ContractOption;
import service.ContractOptionService;
import service.ContractTariffService;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet for tariffs and options settings.
 */
public class ProductsSettingsServlet extends HttpServlet {
    /**
     * Get service for options.
     */
    private final ContractOptionService optionSvc = new ContractOptionService();
    /**
     * Get service for tariffs.
     */
    private final ContractTariffService tariffSvc = new ContractTariffService();

    /**
     * URL regexp for adding tariff.
     */
    private final Pattern tariffAddPattern = Pattern.compile("^/tariffs/add$");
    /**
     * URL regexp for editing tariff.
     */
    private final Pattern tariffEditPattern = Pattern.compile("^/tariffs/edit/(\\d+)$");
    /**
     * URL regexp for deleting tariff.
     */
    private final Pattern tariffDeletePattern = Pattern.compile("^/tariffs/delete/(\\d+)$");
    /**
     * URL regexp for adding option.
     */
    private final Pattern optionAddPattern = Pattern.compile("^/options/add$");
    /**
     * URL regexp for editing option.
     */
    private final Pattern optionEditPattern = Pattern.compile("^/options/edit/(\\d+)$");
    /**
     * URL regexp for deleting option.
     */
    private final Pattern optionDeletePattern = Pattern.compile("^/options/delete/(\\d+)$");
    /**
     * URL regexp for return options for tariff.
     */
    private final Pattern tariffOptionsPattern = Pattern.compile("^/tariffs/options/(\\d+)$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();
        final String refPath = req.getHeader("referer");

        Matcher tariffAddMatcher = tariffAddPattern.matcher(actionPath);
        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);
        Matcher tariffDeleteMatcher = tariffDeletePattern.matcher(actionPath);

        Matcher optionAddMatcher = optionAddPattern.matcher(actionPath);
        Matcher optionEditMatcher = optionEditPattern.matcher(actionPath);
        Matcher optionDeleteMatcher = optionDeletePattern.matcher(actionPath);

        Matcher tariffOptionsMatcher = tariffOptionsPattern.matcher(actionPath);

        /**
         * Get tariffs list.
         */
        if ("/tariffs".equals(actionPath)) {
            req.setAttribute("tariffs", tariffSvc.getActiveTariffs());
            req.getRequestDispatcher("/jsp/tariffs.jsp").forward(req, resp);
        }

        /**
         * Get tariff add form.
         */
        if (tariffAddMatcher.matches()) {
            req.setAttribute("options", optionSvc.getActiveOptions());
            req.getRequestDispatcher("/jsp/add-tariff.jsp").forward(req, resp);
        }

        /**
         * Get tariff edit form.
         */
        if (tariffEditMatcher.matches()) {
            long tariffId = Long.parseLong(tariffEditMatcher.group(1));

            // TODO: 24.11.2015 set map (tariff, checked) instead of two lists
            req.setAttribute("editedTariff", tariffSvc.getById(tariffId));
            req.setAttribute("options", optionSvc.getActiveOptions());

            req.getRequestDispatcher("/jsp/edit-tariff.jsp").forward(req, resp);
        }

        /**
         * Delete tariff.
         */
        if (tariffDeleteMatcher.matches()) {
            long tariffId = Long.parseLong(tariffDeleteMatcher.group(1));

            tariffSvc.deleteTariff(tariffId);

            resp.sendRedirect(refPath);
        }

        /**
         * Get options list.
         */
        if ("/options".equals(actionPath)) {
            req.setAttribute("options", optionSvc.getActiveOptions());
            req.getRequestDispatcher("/jsp/options.jsp").forward(req, resp);
        }

        /**
         * Get option add form.
         */
        if (optionAddMatcher.matches()) {
            req.setAttribute("options", optionSvc.getActiveOptions());

            req.getRequestDispatcher("/jsp/add-option.jsp").forward(req, resp);
        }

        /**
         * Get option edit form.
         */
        if (optionEditMatcher.matches()) {
            long optionId = Long.parseLong(optionEditMatcher.group(1));
            ContractOption editedOption = optionSvc.getById(optionId);
            List<ContractOption> options = new ArrayList<>(optionSvc.getActiveOptions());
            options.remove(editedOption);

            req.setAttribute("editedOption", editedOption);
            req.setAttribute("options", options);

            req.getRequestDispatcher("/jsp/edit-option.jsp").forward(req, resp);
        }

        /**
         * Delete option.
         */
        if (optionDeleteMatcher.matches()) {
            long optionId = Long.parseLong(optionDeleteMatcher.group(1));

            optionSvc.deleteOption(optionId);

            resp.sendRedirect(refPath);
        }

        /**
         * Get options for tariff.
         */
        if (tariffOptionsMatcher.matches()) {
            long tariffId = Long.parseLong(tariffOptionsMatcher.group(1));
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            for (ContractOption option : tariffSvc.getById(tariffId).getAvailableOptions()) {
                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", option.getId())
                        .add("name", option.getName())
                        .add("connectionCost", String.valueOf(option.getConnectionCost()))
                        .add("monthlyCost", String.valueOf(option.getMonthlyCost())
                        ));
            }

            resp.setContentType("application/json");
            resp.getWriter().print(jsonArrayBuilder.build());
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String actionPath = req.getRequestURI();

        Matcher tariffAddMatcher = tariffAddPattern.matcher(actionPath);
        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);

        Matcher optionAddMatcher = optionAddPattern.matcher(actionPath);
        Matcher optionEditMatcher = optionEditPattern.matcher(actionPath);

        // TODO: 23.11.2015 think about normal routing
        /**
         * Save tariff.
         */
        if (tariffAddMatcher.matches() || tariffEditMatcher.matches()) {
            List<String> validErrs = new ArrayList<>();

            long tariffId = 0L;
            if (tariffEditMatcher.matches()) {
                tariffId = Long.parseLong(tariffEditMatcher.group(1));
            }
            String tariffName = req.getParameter("tariff_name");
            Double monthlyCost = Double.parseDouble(req.getParameter("monthly_cost"));
            List<ContractOption> tariffOptions = new ArrayList<>();
            String[] selectedOptions = req.getParameterValues("selected_options[]");

            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    tariffOptions.add(optionSvc.getById(Long.parseLong(optionId)));
                }
            }

            if (!tariffSvc.hasUniqueName(tariffId, tariffName)) {
                validErrs.add("notUniqueName");
            }

            if (validErrs.isEmpty()) {
                tariffSvc.upsertTariff(tariffId, tariffName, monthlyCost, tariffOptions);

                resp.sendRedirect("/tariffs");
            } else {
                req.setAttribute("errors", validErrs);
                req.setAttribute("name", tariffName);
                req.setAttribute("monthlyCost", monthlyCost);
                doGet(req, resp);
            }
        }

        /**
         * Save option.
         */
        if (optionAddMatcher.matches() || optionEditMatcher.matches()) {
            List<String> validErrs = new ArrayList<>();

            long optionId = 0L;
            if (optionEditMatcher.matches()) {
                optionId = Long.parseLong(optionEditMatcher.group(1));
            }
            // Save all parameters
            String optionName = req.getParameter("option_name");
            Double connectionCost = 0D;
            Double monthlyCost = 0D;
            List<ContractOption> incompatibleList = new ArrayList<>();
            List<ContractOption> mandatoryList = new ArrayList<>();

            String[] incompatibleOptions = req.getParameterValues("incompatible_options[]");
            String[] mandatoryOptions = req.getParameterValues("mandatory_options[]");


            if (!"".equals(req.getParameter("connection_cost"))) {
                connectionCost = Double.parseDouble(req.getParameter("connection_cost"));
            }
            if (!"".equals(req.getParameter("monthly_cost"))) {
                monthlyCost = Double.parseDouble(req.getParameter("monthly_cost"));
            }

            // TODO: 30.11.2015 add to service method for getting List of options from array
            if (incompatibleOptions != null) {
                for (String incOptionId : incompatibleOptions) {
                    incompatibleList.add(optionSvc.getById(Long.parseLong(incOptionId)));
                }
            }
            if (mandatoryOptions != null) {
                for (String manOptionId : mandatoryOptions) {
                    mandatoryList.add(optionSvc.getById(Long.parseLong(manOptionId)));
                }
            }

            // Verification
            if (!optionSvc.hasUniqueName(optionId, optionName)) {
                validErrs.add("notUniqueName");
            }

            if (validErrs.isEmpty()) {
                ContractOption option;
                if (optionId != 0L) {
                    option = optionSvc.getById(optionId);
                }
                else {
                    option = new ContractOption();
                }

                option.setName(optionName);
                option.setConnectionCost(connectionCost);
                option.setMonthlyCost(monthlyCost);

                option = optionSvc.upsertOption(option);
                optionSvc.updateIncompatibleList(option, incompatibleList);
                optionSvc.updateMandatoryList(option, mandatoryList);

                resp.sendRedirect("/options");
            } else {
                req.setAttribute("errors", validErrs);
                req.setAttribute("name", optionName);
                req.setAttribute("connectionCost", connectionCost);
                req.setAttribute("monthlyCost", monthlyCost);
                req.setAttribute("incompatibleOptions", incompatibleList);
                req.setAttribute("mandatoryOptions", mandatoryList);
                doGet(req, resp);
            }
        }
    }
}
