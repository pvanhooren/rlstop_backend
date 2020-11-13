package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeTests {
    @Test
    void createTradeTest(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-3333-5555", "Party Time");

        //Act
        Trade trade = new Trade("2000c", "Interstellar", user);

        //Assert
        assertEquals("Interstellar", trade.getOffers());
    }

    @Test
    void tradeCtorTest(){
        //Arrange
        Trade trade = new Trade("2000c", "Interstellar", null);

        //Act
        String wants = trade.getWants();

        //Assert
        assertEquals("2000c", wants);
    }

    @Test
    void userInTradeTest(){
        //Arrange
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", Platform.XBOX, "matthyas", "Tidal Stream");

        //Act
        Trade trade = new Trade("Fennec", "300c", user);

        //Assert
        assertEquals("Fifalosophy", trade.getUser().getUserName());
    }
}
