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

/**
 * Created by Лена on 15.11.2015.
 */
public class ProductsSettingsServlet extends HttpServlet {
    private ContractOptionService contractOptionService;
    private ContractTariffService contractTariffService;

    @Override
    public void init() throws ServletException {
        contractTariffService = new ContractTariffService();
        contractOptionService = new ContractOptionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionPath = req.getRequestURI();

        if ("/tariffs".equals(actionPath)) {
            req.setAttribute("tariffs", contractTariffService.getTariffs());
            req.setAttribute("options", contractOptionService.getOptions());
            req.getRequestDispatcher("/jsp/tariffs.jsp").forward(req, resp);
        }

        if ("/tariffs/add-option".equals(actionPath)) {
            req.setAttribute("options", contractOptionService.getOptions());
            req.getRequestDispatcher("/jsp/add-option.jsp").forward(req, resp);
        }

//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionPath = req.getRequestURI();

        if ("/tariffs".equals(actionPath)) {
            String tariffName = req.getParameter("tariff_name");
            Double monthlyPrice = Double.parseDouble(req.getParameter("monthly_cost"));
            ArrayList<ContractOption> tariffOptions = new ArrayList<ContractOption>();
            String[] selectedOptions = req.getParameterValues("selected_options[]");

            for (String optionId : selectedOptions) {
                tariffOptions.add(contractOptionService.getById(Integer.parseInt(optionId)));
            }
            contractTariffService.addTariff(tariffName, monthlyPrice, tariffOptions);

            doGet(req, resp);
        }
        if ("/tariffs/add-option".equals(actionPath)) {
            String optionName = req.getParameter("option_name");
            Double connectionCost = Double.parseDouble(req.getParameter("connection_cost"));
            Double monthlyCost = Double.parseDouble(req.getParameter("monthly_cost"));

            contractOptionService.addOption(optionName, connectionCost, monthlyCost);
            doGet(req, resp);
        }

        super.doPost(req, resp);
    }
}
