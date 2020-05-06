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
import java.util.List;

@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private UserService userService;

    public String getUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public List<String> getRoles() {
        return new ArrayList<>(userService.getUser(getUsername(), false).getRoles());
    }

    public Long getMoney() {
        return userService.getUser(getUsername(), false).getBalance();
    }

    public List<Copy> getCopies() {
        return userService.getUser(getUsername(), true).getCopies();
    }

    public Long getBalance() {
        return userService.getUser(getUsername(), false).getBalance();
    }

    public int getCardPacks() {
        return userService.getUser(getUsername(), false).getCardPacks();
    }

    public int getTotalCards() {
        List<Copy> copies = userService.getUser(getUsername(), true).getCopies();
        return copies.stream().mapToInt(Copy::getAmount).sum();
    }

    public int getUniqueCards() {
        return userService.getUser(getUsername(), true).getCopies().size();
    }

}
