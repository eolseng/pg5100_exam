package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

@Service
public class DefaultDataInitializerService {

    @Autowired
    private UserService userService;

    @Autowired
    private PlaceholderItemService itemService;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    public void initialize() {
        User user1 = attempt(() -> userService.createUser("Foo", "Bar"));
        assert user1 != null;
        User user2 = attempt(() -> userService.createUser("James", "Bond"));
        assert user2 != null;
        User user3 = attempt(() -> userService.createUser("Fus", "Ro'Dah"));
        assert user3 != null;

        Long item1Id = attempt(() -> itemService.createItem("Pistol"));
        Long item2Id = attempt(() -> itemService.createItem("Dagger"));
        Long item3Id = attempt(() -> itemService.createItem("Whiskey"));

        attempt(() -> transactionService.registerTransaction(user1.getUsername(), item1Id));
        attempt(() -> transactionService.registerTransaction(user1.getUsername(), item2Id));
        attempt(() -> transactionService.registerTransaction(user1.getUsername(), item3Id));
        attempt(() -> transactionService.registerTransaction(user2.getUsername(), item2Id));
        attempt(() -> transactionService.registerTransaction(user2.getUsername(), item3Id));
        attempt(() -> transactionService.registerTransaction(user3.getUsername(), item3Id));
    }

    private  <T> T attempt(Supplier<T> lambda){
        try{
            return lambda.get();
        }catch (Exception e){
            return null;
        }
    }

}
