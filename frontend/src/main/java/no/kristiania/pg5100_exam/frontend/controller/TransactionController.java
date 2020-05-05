package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class TransactionController {

    @Autowired
    BookingService service;

    @Autowired
    UserInfoController infoController;

    public void purchaseItem(Long itemId) {
        String username = infoController.getUsername();
        service.registerBooking(username, itemId);
    }

    public String sellItem(Long transactionId) {
        service.cancelBooking(transactionId);
        return "/ui/profile.jsf?faces-redirect=true";
    }

}
