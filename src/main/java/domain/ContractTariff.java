package domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Лена on 15.11.2015.
 */
@Entity
@Table(name = "tariffs")
//@NamedQuery(name = "ContractTariff.addAvailableOption", query = "insert into tariff_options t values t.tariff_id = :tariff, t.option_id = :option")
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
}
