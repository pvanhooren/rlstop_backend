package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeTests {
    @Test
    void createTradeTest(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time");

        //Act
        Trade trade = new Trade("2000c", "Interstellar", user);

        //Assert
        assertEquals("Interstellar", trade.getOffers());
    }

    @Test
    void userInTradeTest(){
        //Arrange
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", "XBox", "matthyas", "Tidal Stream");

        //Act
        Trade trade = new Trade("Fennec", "300c", user);

        //Assert
        assertEquals("Fifalosophy", trade.getUser().getUserName());
    }
}
