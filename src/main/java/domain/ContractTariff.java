package domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tariffs")
@NamedQuery(name = "ContractTariff.getAllActive",
        query = "select t from ContractTariff t where t.isDeleted = false")
public class ContractTariff {

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

    @Column (name = "deleted")
    private boolean isDeleted = false;

    public ContractTariff() {
    }

    public long getId() {
        return id;
    }

    public ContractTariff(final String newName, final double newMonthlyCost, final List<ContractOption> newAvailableOptions) {
        name = newName;
        monthlyCost = newMonthlyCost;
        availableOptions = newAvailableOptions;
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
