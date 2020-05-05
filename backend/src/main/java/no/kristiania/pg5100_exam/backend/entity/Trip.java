package no.kristiania.pg5100_exam.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 128)
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "trip")
    private List<Booking> bookings;

    @Min(0)
    private Long cost;

    public Trip() {
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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
