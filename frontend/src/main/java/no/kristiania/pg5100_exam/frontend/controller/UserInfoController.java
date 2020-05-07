package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.User;
import no.kristiania.pg5100_exam.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private UserService userService;

    private String username;
    private User user;


    private void getUser() {
        if (username == null) {
            username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        user = userService.getUser(username, true);
    }

    public String getUsername() {
        if (username == null) {
            username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        return username;
    }

    public List<String> getRoles() {
        if (user == null) {
            getUser();
        }
        return new ArrayList<>(user.getRoles());
    }

    public List<Copy> getCopies() {
        if (user == null) {
            getUser();
        }
        return user.getCopies();
    }

    public Long getBalance() {
        if (user == null) {
            getUser();
        }
        return user.getBalance();
    }

    public int getCardPacks() {
        if (user == null) {
            getUser();
        }
        return user.getCardPacks();
    }

    public int getTotalCards() {
        if (user == null) {
            getUser();
        }
        return user.getCopies().stream().mapToInt(Copy::getAmount).sum();
    }

    public int getUniqueCards() {
        if (user == null) {
            getUser();
        }
        return user.getCopies().size();
    }

    public void sortCopiesBy(String sort) {

        switch (sort) {
            case "alphabetical":
                user.getCopies().sort((a, b) -> a.getItem().getName().compareToIgnoreCase(b.getItem().getName()));
                break;
            case "copies":
                user.getCopies().sort(Comparator.comparingInt(Copy::getAmount));
                break;
            case "painLevel":
                user.getCopies().sort(Comparator.comparingInt(item -> item.getItem().getPainLevel()));
                break;
            case "value":
                // Sorting by painLevel or value results in the same list because the lazy CardDesigners use value=painLevel*10
                user.getCopies().sort(Comparator.comparingInt(item -> item.getItem().getValue()));
                break;
        }
    }

}
