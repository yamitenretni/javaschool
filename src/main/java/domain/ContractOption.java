package domain;

import javax.persistence.*;

/**
 * Created by Лена on 15.11.2015.
 */
@Entity
@Table(name = "options")
public class ContractOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "connection_cost")
    private double connectionCost;

    @Column(name = "monthly_cost")
    private double monthlyCost;

    public ContractOption() {
    }

    public ContractOption(final String newName, final double newConnectionCost, final double newMonthlyCost) {
        name = newName;
        connectionCost = newConnectionCost;
        monthlyCost = newMonthlyCost;
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
        this.monthlyCost = newMonthlyCost;
    }
}
