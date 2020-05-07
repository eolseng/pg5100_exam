package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.repository.ItemRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ItemService {

    @Autowired
    private EntityManager em;

    @Autowired
    private ItemRepository repo;

    public Long createItem(String name, String latinName, int painLevel, String description, int value) {

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

    public Item getItem(Long id, boolean withCopies) {

        Optional<Item> item = repo.findById(id);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Item with that ID does not exists: " + id);
        }
        if (withCopies) {
            Hibernate.initialize(item.get().getCopies());
        }
        return item.get();
    }

    public List<Item> getAllItems(boolean withCopies) {

        List<Item> items = repo.findAll();
        if (withCopies) {
            items.forEach(item -> Hibernate.initialize(item.getCopies()));
        }
        return items;
    }

    public Item getRandomItem(boolean withCopies) {

        long size = repo.count();
        Random random = new Random();
        int rnd = random.nextInt((int) size);

        TypedQuery<Item> query = em
                .createQuery("SELECT item FROM Item item", Item.class)
                .setFirstResult(rnd)
                .setMaxResults(1);
        Item item = query.getSingleResult();

        if (withCopies) {
            Hibernate.initialize(item.getCopies());
        }

        return item;
    }

    public List<Item> getRandomItems(int amount, boolean withCopies) {
        // Returns 'amount' random cards. Can contain duplicates.
        List<Item> items = new ArrayList<>(amount);
        while (items.size() < amount) {
            items.add(getRandomItem(withCopies));
        }
        return items;
    }
}
