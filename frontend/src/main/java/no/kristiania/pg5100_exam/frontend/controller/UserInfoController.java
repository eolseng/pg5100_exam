package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.Transaction;
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
        return userService.getUser(getUsername(), false).getMoney();
    }

    public List<Transaction> getTransactions() {
        return userService.getUser(getUsername(), true).getTransactions();
    }

}
