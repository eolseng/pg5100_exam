package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.service.CardPackService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class CardPackController {

    @Autowired
    UserInfoController infoController;

    @Autowired
    CardPackService cardPackService;

    List<Copy> cardPack;

    public int getPrice() {
        return CardPackService.CARD_PACK_PRICE;
    }

    public String purchaseCardPack() {
        try{
            cardPackService.purchaseCardPack(infoController.getUsername());
        } catch (Exception e) {
            return "/ui/collection.jsf?faces-redirect=true&purchase=failed";
        }
        return "/ui/collection.jsf?faces-redirect=true&purchase=success";
    }

    public List<Copy> openCardPack() {
        if (cardPack == null) {
            cardPack = cardPackService.openCardPack(infoController.getUsername());
        }
        return cardPack;
    }

}
