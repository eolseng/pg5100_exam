package no.kristiania.pg5100_exam.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final Long STARTING_BALANCE = 3000L;
    public static final int STARTING_CARD_PACKS = 3;

    @Id
    @NotBlank
    @Size(min = 3, max = 128)
    private String username;

    @NotBlank
    // Since this is a BCrypt hash of the password minimum length is set on the UserService
    @Column(name = "password")
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @NotNull
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private List<Copy> copies;

    @NotNull
    @Min(0)
    private Long balance;

    @NotNull
    @Min(0)
    private int cardPacks;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long money) {
        this.balance = money;
    }

    public int getCardPacks() {
        return cardPacks;
    }

    public void setCardPacks(int cardPacks) {
        this.cardPacks = cardPacks;
    }
}
