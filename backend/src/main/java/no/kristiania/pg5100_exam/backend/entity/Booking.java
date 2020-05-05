package no.kristiania.pg5100_exam.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Trip trip;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", user=" + user +
                ", trip=" + trip +
                ", createdAt=" + createdAt +
                '}';
    }
}
