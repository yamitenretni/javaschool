package form;

import domain.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all data in the cart.
 */
public class CartForm {
    /**
     * List of contracts with changes.
     */
    private List<CartContractForm> cartContractForms;

    /**
     * Default constructor.
     */
    public CartForm() {
        cartContractForms = new ArrayList<>();
    }

    /**
     * Get CartContractForm by Contract or create, if it's not exist.
     *
     * @param contract contract for search
     * @return found or created CartContractForm
     */
    public final CartContractForm getCartContractForm(final Contract contract) {
        for (CartContractForm cartContract : cartContractForms) {
            if (cartContract.getContract() == contract) {
                return cartContract;
            }
        }
        CartContractForm cartContractForm = new CartContractForm(contract);
        cartContractForms.add(cartContractForm);

        return cartContractForm;
    }

    /**
     * Getter for list of changing contracts.
     *
     * @return list of contracts with changes
     */
    public final List<CartContractForm> getCartContractForms() {
        return cartContractForms;
    }

    /**
     * Setter for list of changing contracts.
     *
     * @param newCartContractForms list of contracts with changes
     */
    public final void setCartContractForms(final List<CartContractForm> newCartContractForms) {
        cartContractForms = newCartContractForms;
    }
}
