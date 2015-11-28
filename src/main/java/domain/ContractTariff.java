package domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tariffs", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
        @NamedQuery(name = "ContractTariff.getAllActive",
                query = "select t from ContractTariff t where t.isDeleted = false"),
        @NamedQuery(name = "ContractTariff.hasUniqueName",
                query = "select t from ContractTariff t where t.name = :name and t.id <> :id")
})
public class ContractTariff {
    public static final String GET_ACTIVE = "ContractTariff.getAllActive";
    public static final String HAS_UNIQUE_NAME = "ContractTariff.hasUniqueName";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "monthly_cost")
    private double monthlyCost;

    @ManyToMany
    @JoinTable(name = "tariff_options",
            joinColumns = {@JoinColumn(name = "tariff_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")})
    private List<ContractOption> availableOptions;

    @Column(name = "deleted")
    private boolean isDeleted = false;

    public ContractTariff() {
    }

    public ContractTariff(final String newName, final double newMonthlyCost, final List<ContractOption> newAvailableOptions) {
        name = newName;
        monthlyCost = newMonthlyCost;
        availableOptions = newAvailableOptions;
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

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(final double newMonthlyCost) {
        monthlyCost = newMonthlyCost;
    }

    public List<ContractOption> getAvailableOptions() {
        return availableOptions;
    }

    public void setAvailableOptions(final List<ContractOption> newAvailableOptions) {
        availableOptions = newAvailableOptions;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
