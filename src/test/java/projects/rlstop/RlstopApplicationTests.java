package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RlstopApplicationTests {

    @Test
    void hashPasswordTest() {
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time");
        assertEquals(Objects.hash("12345zes"), user.getPasswordHash());
    }

    @Test
    void createUserTest(){
        User user = new User("Kaasje", "koen@gmail.com", "kaasje123", "PlayStation", "hutskoen", "Road Hog XL");
        assertEquals("Kaasje", user.getUserName());
    }

    @Test
    void addToWishlistTest(){
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", "XBox", "matthyas", "Tidal Stream");
        user.addToWishlist("20XX");
        assertEquals("20XX", user.getWishlist().get(1));
    }

    @Test
    void createTradeTest(){
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time");
        Trade trade = new Trade("2000c", "Interstellar", user);
        assertEquals("Interstellar", trade.getOffers());
    }

    @Test
    void userInTradeTest(){
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", "XBox", "matthyas", "Tidal Stream");
        Trade trade = new Trade("Fennec", "300c", user);
        assertEquals("Fifalosophy", trade.getUser().getUserName());
    }
}
