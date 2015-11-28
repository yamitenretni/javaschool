package domain;

import javax.persistence.*;

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
        monthlyCost = newMonthlyCost;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
