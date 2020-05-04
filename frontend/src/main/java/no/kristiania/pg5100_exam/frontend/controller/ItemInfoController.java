package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.service.PlaceholderItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ItemInfoController {

    @Autowired
    PlaceholderItemService itemService;

    public PlaceholderItem getItem(Long id) {
        return itemService.getItem(id, false);
    }

    public List<PlaceholderItem> getAllItems() {
        return itemService.getAllItems(false);
    }

}
