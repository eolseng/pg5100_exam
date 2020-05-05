package no.kristiania.pg5100_exam.backend.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "placeholder_item")
public class PlaceholderItem {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 128)
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "item")
    private List<Transaction> transactions;

    @Min(0)
    private Long cost;

    public PlaceholderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
