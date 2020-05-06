package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CardPackService {

    public static final int CARD_PACK_SIZE = 3;
    public static final int CARD_PACK_PRICE = 500;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    CopyService copyService;

    public List<Copy> openCardPack(String username) {

        User user = userService.getUser(username, false);
        if (user.getCardPacks() <= 0) {
            throw new IllegalArgumentException("User does not have any unopened card packs");
        }
        user.setCardPacks(user.getCardPacks() - 1);

        List<Copy> copies = new ArrayList<>(CARD_PACK_SIZE);
        List<Item> items = itemService.getRandomItems(CARD_PACK_SIZE, false);
        items.forEach(item -> {
            Copy copy = copyService.registerCopy(username, item.getId());
            copies.add(copy);
        });

        return copies;
    }

    public void purchaseCardPack(String username) {
        User user = userService.getUser(username, false);
        if (user.getBalance() < CARD_PACK_PRICE) {
            throw new IllegalArgumentException("User does not have enough money to purchase a new Card Pack");
        }

        user.setBalance(user.getBalance() - CARD_PACK_PRICE);
        userService.addCardPack(username);
    }
}
