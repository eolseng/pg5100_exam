package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ItemInfoController {

    @Autowired
    ItemService itemService;

    List<Item> allItems;

    public List<Item> getAllItems() {
        if(allItems == null) {
            allItems = itemService.getAllItems(false);
        }
        return allItems;
    }
}
