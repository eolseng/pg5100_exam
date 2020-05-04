package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional
public class PlaceholderItemService {

    @Autowired
    private EntityManager em;

    public Long createItem(String name) {

        PlaceholderItem item = new PlaceholderItem();
        item.setName(name);

        em.persist(item);

        return item.getId();

    }

    public PlaceholderItem getItem(Long id, boolean withTransactions) {
        PlaceholderItem item = em.find(PlaceholderItem.class, id);
        if (withTransactions && item != null) {
            Hibernate.initialize(item.getTransactions());
        }
        return item;
    }

    public List<PlaceholderItem> getAllItems(boolean withTransactions) {

        TypedQuery<PlaceholderItem> query = em.createQuery("SELECT i FROM PlaceholderItem i", PlaceholderItem.class);
        List<PlaceholderItem> items = query.getResultList();

        if(withTransactions && items != null) {
            items.forEach(item -> Hibernate.initialize(item.getTransactions()));
        }

        return items;

    }

}
