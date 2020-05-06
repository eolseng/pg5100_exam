package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.service.CopyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class MillController {

    @Autowired
    CopyService service;

    @Autowired
    UserInfoController infoController;

    public String sellItem(Long copyId) {
        service.millCopy(copyId);
        return "/ui/collection.jsf?faces-redirect=true";
    }

}
