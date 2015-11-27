package form;

import domain.Contract;
import domain.ContractOption;
import domain.ContractTariff;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all changes for the one contract.
 */
public class CartContractForm {
    /**
     * Changing contract.
     */
    private Contract contract;

    /**
     * New tariff for the contract.
     */
    private ContractTariff newTariff;

    /**
     * List of deactivated options.
     */
    private List<ContractOption> deactivatedOptions;

    /**
     * List of new options.
     */
    private List<ContractOption> newOptions;

    /**
     * Default constructor
     */
    public CartContractForm() {
        deactivatedOptions = new ArrayList<>();
        newOptions = new ArrayList<>();
    }

    /**
     * Constructor with contract.
     * @param newContract changing contract
     */
    public CartContractForm(final Contract newContract) {
        contract = newContract;
        deactivatedOptions = new ArrayList<>();
        newOptions = new ArrayList<>();
    }

    /**
     * Add deactivated option to the cart if it's not there
     * @param option new option
     */
    public final void addDeactivatedOption(final ContractOption option) {
        if (!deactivatedOptions.contains(option)) {
            deactivatedOptions.add(option);
        }
    }

    /**
     * Delete deactivated option from the cart
     * @param option deleting option
     */
    public final void deleteDeactivatedOption(final ContractOption option) {
        if (deactivatedOptions.contains(option)) {
            deactivatedOptions.remove(option);
        }
    }

    /**
     * Add new option to the cart if it's not there
     * @param option new option
     */
    public final void addNewOption(final ContractOption option) {
        if (!newOptions.contains(option)) {
            newOptions.add(option);
        }
    }

    /**
     * Delete deactivated option from the cart
     * @param option deleting option
     */
    public final void deleteAddedOption(final ContractOption option) {
        if (newOptions.contains(option)) {
            newOptions.remove(option);
        }
    }

    public final void changeTariff(final ContractTariff tariff) {
        newTariff = tariff;

        //deactivate options
        List<ContractOption> activeOptions = contract.getActivatedOptions();
        List<ContractOption> deactivatingOptions = tariff.getAvailableOptions();

        deactivatingOptions.removeAll(activeOptions);

        for (ContractOption option : deactivatingOptions) {
            addDeactivatedOption(option);
        }
    }

    /**
     * Contract getter.
     * @return contract of CartContractForm
     */
    public final Contract getContract() {
        return contract;
    }

    /**
     * Contract setter.
     * @param changingContract contract that will be changed after submit
     */
    public final void setContract(final Contract changingContract) {
        contract = changingContract;
    }

    /**
     * Tariff getter.
     * @return new tariff of contract
     */
    public final ContractTariff getNewTariff() {
        return newTariff;
    }

    /**
     * Tariff setter.
     * @param tariff new tariff of contract
     */
    public final void setNewTariff(final ContractTariff tariff) {
        newTariff = tariff;
    }

    /**
     * Deactivated options getter.
     * @return list of options that will be deactivated after submit
     */
    public final List<ContractOption> getDeactivatedOptions() {
        return deactivatedOptions;
    }

    /**
     * Deactivated options setter.
     * @param options list of options that will be deactivated after submit
     */
    public final void setDeactivatedOptions(final List<ContractOption> options) {
        deactivatedOptions = options;
    }

    /**
     * New options getter.
     * @return list of options that will be added after submit
     */
    public final List<ContractOption> getNewOptions() {
        return newOptions;
    }

    /**
     * New options setter.
     * @param options list of options that will be added after submit
     */
    public final void setNewOptions(final List<ContractOption> options) {
        newOptions = options;
    }
}
