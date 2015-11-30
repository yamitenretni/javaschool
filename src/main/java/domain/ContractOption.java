package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
@NamedQueries({
        @NamedQuery(name = "ContractOption.getAllActive",
                query = "select o from ContractOption o where o.isDeleted = false"),
        @NamedQuery(name = "ContractOption.hasUniqueName",
                query = "select o from ContractOption o where o.name = :name and o.id <> :id")
})
public class ContractOption {
    public static final String GET_ACTIVE = "ContractOption.getAllActive";
    public static final String HAS_UNIQUE_NAME = "ContractOption.hasUniqueName";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "connection_cost")
    private double connectionCost;

    @Column(name = "monthly_cost")
    private double monthlyCost;

    @Column(name = "deleted")
    private boolean isDeleted = false;

    @ManyToMany
    @JoinTable(name = "incompatible_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "incomp_option_id")})
    private List<ContractOption> incompatibleOptions;

    @ManyToMany
    @JoinTable(name = "mandatory_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "mand_option_id")})
    private List<ContractOption> mandatoryOptions;

    public ContractOption() {
        incompatibleOptions = new ArrayList<>();
        mandatoryOptions = new ArrayList<>();
    }

    public ContractOption(final String newName, final double newConnectionCost, final double newMonthlyCost) {
        name = newName;
        connectionCost = newConnectionCost;
        monthlyCost = newMonthlyCost;
        incompatibleOptions = new ArrayList<>();
        mandatoryOptions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String newName) {
        name = newName;
    }

    public double getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(final double newConnectionCost) {
        connectionCost = newConnectionCost;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(final double newMonthlyCost) {
        monthlyCost = newMonthlyCost;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(final boolean deleted) {
        isDeleted = deleted;
    }

    public final List<ContractOption> getIncompatibleOptions() {
        return incompatibleOptions;
    }

//    public final void setIncompatibleOptions(final List<ContractOption> options) {
//        incompatibleOptions = options;
//    }

    public final List<ContractOption> getMandatoryOptions() {
        return mandatoryOptions;
    }

    public final void setMandatoryOptions(final List<ContractOption> options) {
        mandatoryOptions = options;
    }
}
