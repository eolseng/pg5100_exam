package no.kristiania.pg5100_exam.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

@Service
public class DefaultDataInitializerService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CardPackService cardPackService;

    @PostConstruct
    public void initialize() {

        /*
            Need to check if data already exists in the database to avoid crashing the application.
         */
        int registeredUsers = userService.getAllUsers(false).size();
        int registeredItems = itemService.getAllItems(false).size();

        if(registeredUsers == 0 && registeredItems == 0){
            createCards();
            createAdmin();
        }
    }

    private void createAdmin() {
        String adminUsername = "admin";
        String adminPassword = "admin";
        userService.createUser(adminUsername, adminPassword);
        userService.promoteToAdmin(adminUsername);
    }

    private void createCards() {

        attempt(() -> itemService.createItem(
                "Honey Wasp",
                "Brachygastra mellifica",
                20,
                "Spicy, blistering. A cotton swap dipped in habanero sauce has been pushed up your nose.",
                200
        ));

        attempt(() -> itemService.createItem(
                "Baldfaced Hornet",
                "Dolichovespula maculata",
                20,
                "Rich, hearty, slightly crunchy. Similar to getting your hand mashed in a revolving door.",
                200
        ));

        attempt(() -> itemService.createItem(
                "Indian Jumping Ant",
                "Harpegnathos saltator",
                10,
                "Ah, that wonderful wake-up feeling, like coffee but oh so bitter.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Water-Walking Wasp",
                "Euodynerus crypticus",
                10,
                "Clever but trivial? A little like magic in that you cannot quite figure out the difference between pain and illusion.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Ferocious Polybia Wasp",
                "Polybia rejecta",
                15,
                "Like a trick gone wrong. Your posterior is a target for a BB gun. Bull's-eye, over and over.",
                150
        ));

        attempt(() -> itemService.createItem(
                "Suturing Army Ant",
                "Eciton burchellii",
                15,
                "A cut on your elbow, stitched with a rusty needle.",
                150
        ));

        attempt(() -> itemService.createItem(
                "Iridescent Cockroach Hunter",
                "Chlorion cyaneum",
                10,
                "Itchy with a hint of sharpness. A single stinging nettle pricked your hand.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Asian Needle Ant",
                "Brachyponera chinensis",
                10,
                "Nightfall following a day at the beach. You forgot the sunscreen. Your burned nose lets you know.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Yellow Fire Wasp",
                "Agelaia myrmecophila",
                25,
                "An odd, distressing pain. Tiny blowtorches kiss your arms and legs.",
                250
        ));

        attempt(() -> itemService.createItem(
                "Red-Headed Paper Wasp",
                "Polistes erythrocephalus",
                30,
                "Immediate, irrationally intense, and unrelenting. This is the closest you will come to seeing the blue of a flame from within the fire.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Nocturnal Hornet",
                "Probespa sp.",
                25,
                "Rude, insulting. An ember from your campfire glued to your forearm.",
                250
        ));

        attempt(() -> itemService.createItem(
                "Florida Harvester Ant",
                "Pogonomyrmex badius",
                30,
                "Bold and unrelenting. Somebody is using a power drill to excavate your ingrown toenail.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Velvet Ant",
                "Dasymutilla klugii",
                30,
                "Explosive and long lasting, you sound insane as you scream. Hot oil from the deep fryer spilling all over your entire hand.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Tarantula Hawk",
                "Pepsis spp.",
                40,
                "Blinding, fierce, shockingly electric. A running hair dryer has been dropped into your bubble bath. A bolt out of the heavens. Lie down and scream.",
                500
        ));

        attempt(() -> itemService.createItem(
                "Bullet Ant",
                "Paraponera clavata",
                40,
                "Pure, intense, brilliant pain. Like walking over flaming charcoal with a 3-inch nail embedded in your heel.",
                500
        ));

        attempt(() -> itemService.createItem(
                "Warrior Warp",
                "Synoeca septentrionalis",
                40,
                "Torture. You are chained in the flow of an active volcano. Why did I start this list?",
                500
        ));

        attempt(() -> itemService.createItem(
                "Western Honey Bee",
                "Apis mellifera",
                20,
                "Burning, corrosive, but you can handle it. A flaming match head lands on your arm and is quenched first with lye and then sulfuric acid.",
                200
        ));

        attempt(() -> itemService.createItem(
                "Unstable Paper Wasp",
                "Polistes instabilis",
                20,
                "Like a dinner guest who stays muych too long, the pain drones on. A hot Dutch oven lands on your hand and you can't get it off.",
                200
        ));

        attempt(() -> itemService.createItem(
                "Red Paper Wasp",
                "Polistes canadensis",
                30,
                "Caustic and burning. Distinctly bitter aftertaste. Like spilling a beaker of hydrochloric acid on a paper cut.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Maricopa Harvester Ant",
                "Pogonomyrmex maricopa",
                30,
                "After eight unrelenting hours of drilling into that ingrown toenail, you find the drill is wedged in the toe.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Giant Paper Wasp",
                "Megapolistes sp.",
                30,
                "There are gods, and they do throw thunderbolts. Poseidon has rammed his trident into your breast.",
                300
        ));

        attempt(() -> itemService.createItem(
                "Fierce Black Polybia Wasp",
                "Polybia Simillima",
                25,
                "A ritual gone wrong, Satanic. The gas lamp in the old church explodes in your face when you light it.",
                250
        ));

        attempt(() -> itemService.createItem(
                "Trap-Jaw Ant",
                "Odontomachus spp.",
                25,
                "Instantaneous and excruciating. A rat trap snaps your index fingernail.",
                250
        ));

        attempt(() -> itemService.createItem(
                "Little Wasp",
                "Polybia occidentalis",
                10,
                "Sharp meets spice. A slender cactus spine brushed a buffalo wing before it poked your arm.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Red Fire Ant",
                "Solenopsis invicta",
                10,
                "Sharp, sudden, mildly alarming. Like walking across a shag carpet and reaching for the light switch.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Sweat Bee",
                "Lasioglossum spp.",
                10,
                "Light and ephemeral, almost fruity. A tiny spark has singed a single hair on your arm.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Slender Twig Ant",
                "Tetraponera sp.",
                10,
                "A skinny bully's punch. It's too weak to hurt but you suspect a cheap trick might be coming.",
                100
        ));

        attempt(() -> itemService.createItem(
                "Artistic Wasp",
                "Parachartergus fraternus",
                20,
                "Pure, then messy, then corrosive. Love and marriage followed by divorce.",
                200
        ));

        attempt(() -> itemService.createItem(
                "Glorious Velvet Ant",
                "Dasymutilla gloriosa",
                20,
                "Instantaneous, like the surprise of being stabbed. Is this what shrapnel feels like?",
                200
        ));

        attempt(() -> itemService.createItem(
                "Giant Sweat Bee",
                "Dienuomia heteropoda",
                15,
                "Size matters but it isn't everything. A silver tablespoon drops squarely onto your big toenail, sending you hopping.",
                150
        ));

    }

    private <T> T attempt(Supplier<T> lambda) {
        try {
            return lambda.get();
        } catch (Exception e) {
            return null;
        }
    }

}
