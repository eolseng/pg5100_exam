package no.kristiania.pg5100_exam.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @NotBlank
    @Size(min = 3, max = 128)
    private String username;

    @NotBlank
    @Size(min = 3, max = 128)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @NotNull
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @NotNull
    @Min(0)
    private Long money;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}
