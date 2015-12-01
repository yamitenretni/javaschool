package form;

import domain.Contract;
import domain.ContractOption;
import domain.ContractTariff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * List of options that will be deactivated, user can't cancel it.
     */
    private List<ContractOption> unsupportedOptions;

    /**
     * List of depending options, which will be deactivate, user can't cancel it.
     */
    private List<ContractOption> dependingOptions;

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
        unsupportedOptions = new ArrayList<>();
        dependingOptions = new ArrayList<>();
    }

    /**
     * Constructor with contract.
     *
     * @param newContract changing contract
     */
    public CartContractForm(final Contract newContract) {
        contract = newContract;
        deactivatedOptions = new ArrayList<>();
        newOptions = new ArrayList<>();
        unsupportedOptions = new ArrayList<>();
        dependingOptions = new ArrayList<>();
    }

    /**
     * Add deactivated option to the cart if it's not there.
     *
     * @param option new option
     */
    public final void addDeactivatedOption(final ContractOption option) {
        //don't delete unsupported option twice
        if (unsupportedOptions.contains(option)) {
            return;
        }

        if (newOptions.contains(option)) {
            newOptions.remove(option);
        } else if (!deactivatedOptions.contains(option)) {
            deactivatedOptions.add(option);
        }

        //deactivate depending options
        List<ContractOption> activeOptions = new ArrayList<>(contract.getActivatedOptions());
        activeOptions.addAll(newOptions);
        activeOptions.removeAll(unsupportedOptions);
        activeOptions.removeAll(deactivatedOptions);

        for (ContractOption dependOption : option.getDependOptions()) {
            if (activeOptions.contains(dependOption) || deactivatedOptions.contains(dependOption)) {
                List<ContractOption> mandatoryOptions = new ArrayList<>(dependOption.getMandatoryOptions());
                mandatoryOptions.remove(option);
                int mandatoryCount = mandatoryOptions.size();
                mandatoryOptions.removeAll(activeOptions);

                if (mandatoryCount != 0 && mandatoryOptions.size() == mandatoryCount && !dependingOptions.contains(dependOption)) {
                    // TODO: 01.12.2015 delete new depend options don't add it to depending
                    if (newOptions.contains(dependOption)) {
                        newOptions.remove(dependOption);
                    }
                    else {
                        dependingOptions.add(dependOption);
                    }
                }
//                else if (mandatoryOptions.isEmpty() && !dependingOptions.contains(dependOption)) {
//                    if (newOptions.contains(dependOption)) {
//                        newOptions.remove(dependOption);
//                    }
//                    else {
//                        dependingOptions.add(dependOption);
//                    }
//                }


            }
        }
    }

    /**
     * Delete deactivated option from the cart.
     *
     * @param option deleting option
     */
    public final void deleteDeactivatedOption(final ContractOption option) {
        if (deactivatedOptions.contains(option)) {
            deactivatedOptions.remove(option);
        }

        //delete depend options
        for (ContractOption dependOption : option.getDependOptions()) {
            if (dependingOptions.contains(dependOption))
            {
                dependingOptions.remove(dependOption);
            }
        }

        //restore new incompatible options
        for (ContractOption incOption : option.getIncompatibleOptions()) {
            if (newOptions.contains(incOption)) {
                newOptions.remove(incOption);
            }
        }
    }

    /**
     * Add new option to the cart if it's not there.
     *
     * @param option new option
     */
    public final void addNewOption(final ContractOption option) {
        //don't add incompatible options
        for (ContractOption futureOption : getFutureOptionList()) {
            if (futureOption.getIncompatibleOptions().contains(option)) {
                return;
            }
        }
        //don't add depend options without mandatory
        boolean isDepend = true;
        for (ContractOption mandatoryOption : option.getMandatoryOptions()) {
            if (getFutureOptionList().contains(mandatoryOption)) {
                isDepend = false;
                break;
            }
        }
        if (isDepend && !option.getMandatoryOptions().isEmpty()) {
            return;
        }

        if (deactivatedOptions.contains(option)) {
            deactivatedOptions.remove(option);
        } else if (!newOptions.contains(option)) {
            newOptions.add(option);
        }

        //delete depend options from list
        for (ContractOption dependOption : option.getDependOptions()) {
            if (dependingOptions.contains(dependOption)) {
                dependingOptions.remove(dependOption);
            }
        }
    }

    /**
     * Delete deactivated option from the cart.
     *
     * @param option deleting option
     */
    public final void deleteAddedOption(final ContractOption option) {
        if (newOptions.contains(option)) {
            newOptions.remove(option);

            //remove depend options from the cart
            List<ContractOption> dependOptions = new ArrayList<>();

            for (ContractOption newOption : newOptions) {
                Set<ContractOption> mandatoryOptions = newOption.getMandatoryOptions();
                boolean isDepend = true;
                for (ContractOption mandatoryOption : mandatoryOptions) {
                    if (getFutureOptionList().contains(mandatoryOption)) {
                        isDepend = false;
                        break;
                    }
                }
                if (isDepend && !mandatoryOptions.isEmpty()) {
                    dependOptions.add(newOption);
                }
            }
            newOptions.removeAll(dependOptions);

            //add depend active option to depending option
            List<ContractOption> options = new ArrayList<>(deactivatedOptions);
            options.addAll(contract.getActivatedOptions());
            for (ContractOption deactiveOption : options) {
                Set<ContractOption> mandatoryOptions = deactiveOption.getMandatoryOptions();
                boolean isDepend = true;
                for (ContractOption mandatoryOption : mandatoryOptions) {
                    if (getFutureOptionList().contains(mandatoryOption)) {
                        isDepend = false;
                        break;
                    }
                }
                if (isDepend && !mandatoryOptions.isEmpty() && !dependingOptions.contains(deactiveOption)) {
                    dependingOptions.add(deactiveOption);
                }
            }





        }
    }

    /**
     * Change contract tariff and deactivate all active options.
     *
     * @param tariff new tariff for the contract
     */
    public final void changeTariff(final ContractTariff tariff) {
        newTariff = tariff;

        //deactivate unsupported options
        List<ContractOption> options = new ArrayList<>();
        options.addAll(contract.getActivatedOptions());
        options.removeAll(tariff.getAvailableOptions());
        unsupportedOptions = options;

        //delete new unsupported options from the cart
        List<ContractOption> deletingNewOptions = new ArrayList<>();
        for (ContractOption newOption : newOptions) {
            if (!tariff.getAvailableOptions().contains(newOption)) {
                deletingNewOptions.add(newOption);
            }
        }
        newOptions.removeAll(deletingNewOptions);

        /**
         * Simple logic: all activated options will be deactivated if tariff changes
         */
//        for (ContractOption option : contract.getActivatedOptions()) {
//            addDeactivatedOption(option);
//        }
    }

    /**
     * Set null into newTariff field.
     */
    public final void deleteNewTariff() {
        newTariff = null;
        unsupportedOptions = new ArrayList<>();
    }

    /**
     * Get list of options that current contract will have after submit
     * @return list of options
     */
    public final List<ContractOption> getFutureOptionList() {
        List<ContractOption> options = new ArrayList<>();
        options.addAll(contract.getActivatedOptions());
        options.addAll(newOptions);
        options.removeAll(unsupportedOptions);
        options.removeAll(deactivatedOptions);

        return options;
    }

    /**
     * Contract getter.
     *
     * @return contract of CartContractForm
     */
    public final Contract getContract() {
        return contract;
    }

    /**
     * Contract setter.
     *
     * @param changingContract contract that will be changed after submit
     */
    public final void setContract(final Contract changingContract) {
        contract = changingContract;
    }

    /**
     * Tariff getter.
     *
     * @return new tariff of contract
     */
    public final ContractTariff getNewTariff() {
        return newTariff;
    }

    /**
     * Deactivated options getter.
     *
     * @return list of options that will be deactivated after submit
     */
    public final List<ContractOption> getDeactivatedOptions() {
        List<ContractOption> options = new ArrayList<>(deactivatedOptions);
        options.removeAll(unsupportedOptions);
        options.removeAll(dependingOptions);
        return options;
    }

    /**
     * New options getter.
     *
     * @return list of options that will be added after submit
     */
    public final List<ContractOption> getNewOptions() {
        return newOptions;
    }

    /**
     * Unsupported options getter.
     *
     * @return list of options that will be deactivated after submit with tariff changing
     */
    public final List<ContractOption> getUnsupportedOptions() {
        return unsupportedOptions;
    }

    /**
     * Depending options getter.
     *
     * @return list of options that will be deactivated after submit with mandatory options deleting.
     */
    public final List<ContractOption> getDependingOptions() {
        return dependingOptions;
    }
}
