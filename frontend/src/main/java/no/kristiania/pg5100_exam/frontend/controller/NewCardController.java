package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class NewCardController {

    @Autowired
    ItemService itemService;

    String name;
    String latinName;
    int painLevel;
    String description;
    int value;

    public String createCard() {
        itemService.createItem(name, latinName, painLevel, description, value);
        return "/admin/admin.jsf?faces-redirect=true&created=true";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
