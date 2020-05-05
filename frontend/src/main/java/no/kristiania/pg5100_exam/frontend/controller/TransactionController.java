package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class TransactionController {

    @Autowired
    TransactionService service;

    @Autowired
    UserInfoController infoController;

    public void purchaseItem(Long itemId) {
        String username = infoController.getUsername();
        service.registerTransaction(username, itemId);
    }

    public String sellItem(Long transactionId) {
        service.removeTransaction(transactionId);
        return "/ui/profile.jsf?faces-redirect=true";
    }

}
