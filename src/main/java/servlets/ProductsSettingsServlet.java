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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductsSettingsServlet extends HttpServlet {
    private ContractOptionService contractOptionService;
    private ContractTariffService contractTariffService;

    private Pattern tariffEditPattern;
    private Pattern tariffDeletePattern;

    @Override
    public void init() throws ServletException {
        contractTariffService = new ContractTariffService();
        contractOptionService = new ContractOptionService();

        tariffEditPattern = Pattern.compile("^/tariffs/edit/(\\d+)$");
        tariffDeletePattern = Pattern.compile("^/tariffs/delete/(\\d+)$");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();

        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);
        Matcher tariffDeleteMatcher = tariffDeletePattern.matcher(actionPath);

        if ("/tariffs".equals(actionPath)) {
            req.setAttribute("tariffs", contractTariffService.getActiveTariffs());
            req.setAttribute("options", contractOptionService.getActiveOptions());
            req.getRequestDispatcher("/jsp/tariffs.jsp").forward(req, resp);
        }

        if ("/tariffs/add-option".equals(actionPath)) {
            req.setAttribute("options", contractOptionService.getActiveOptions());
            req.getRequestDispatcher("/jsp/add-option.jsp").forward(req, resp);
        }

        if (tariffEditMatcher.matches()) {
            long tariffId = Long.parseLong(tariffEditMatcher.group(1));

            req.setAttribute("editedTariff", contractTariffService.getById(tariffId));
            req.setAttribute("options", contractOptionService.getActiveOptions());

            req.getRequestDispatcher("/jsp/edit-tariff.jsp").forward(req, resp);
        }

        if (tariffDeleteMatcher.matches()) {
            long tariffId = Long.parseLong(tariffDeleteMatcher.group(1));

            contractTariffService.deleteTariff(tariffId);

            resp.sendRedirect("/tariffs");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String actionPath = req.getRequestURI();

        Matcher tariffEditMatcher = tariffEditPattern.matcher(actionPath);
        // TODO: 23.11.2015 think about normal routing
        boolean tariffEdited = tariffEditMatcher.matches();

        if ("/tariffs".equals(actionPath) || tariffEdited) {
            long tariffId = 0L;
            if (tariffEdited) {
                tariffId = Long.parseLong(tariffEditMatcher.group(1));
            }

            String tariffName = req.getParameter("tariff_name");
            Double monthlyPrice = Double.parseDouble(req.getParameter("monthly_cost"));
            ArrayList<ContractOption> tariffOptions = new ArrayList<ContractOption>();
            String[] selectedOptions = req.getParameterValues("selected_options[]");

            if (selectedOptions != null) {
                for (String optionId : selectedOptions) {
                    tariffOptions.add(contractOptionService.getById(Integer.parseInt(optionId)));
                }
            }
            contractTariffService.upsertTariff(tariffId, tariffName, monthlyPrice, tariffOptions);

            resp.sendRedirect("/tariffs");
        }
        if ("/tariffs/add-option".equals(actionPath)) {
            String optionName = req.getParameter("option_name");
            Double connectionCost = Double.parseDouble(req.getParameter("connection_cost"));
            Double monthlyCost = Double.parseDouble(req.getParameter("monthly_cost"));

            contractOptionService.addOption(optionName, connectionCost, monthlyCost);
            doGet(req, resp);
        }
    }
}
