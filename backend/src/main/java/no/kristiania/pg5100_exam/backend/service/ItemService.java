package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.repository.ItemRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository repo;

    public Long createCard(String name, String latinName, int painLevel, String description, int value) {

        // Trip names must be unique
        if (repo.existsByName(name)) {
            throw new IllegalArgumentException("A item with that name already exists: " + name);
        }

        Item item = new Item();
        item.setName(name);
        item.setLatinName(latinName);
        item.setPainLevel(painLevel);
        item.setDescription(description);
        item.setValue(value);
        repo.save(item);

        return item.getId();
    }

    public Item getCard(Long id, boolean withCopies) {

        Optional<Item> card = repo.findById(id);
        if (card.isEmpty()) {
            throw new IllegalArgumentException("Item with that ID does not exists: " + id);
        }
        if (withCopies) {
            Hibernate.initialize(card.get().getCopies());
        }
        return card.get();
    }

    public List<Item> getAllCards(boolean withCopies) {

        List<Item> items = repo.findAll();
        if(withCopies) {
            items.forEach(item -> Hibernate.initialize(item.getCopies()));
        }
        return items;
    }
}
