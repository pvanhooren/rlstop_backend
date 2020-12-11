package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.models.database.Interest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class InterestTests {
    @Test
    void createInterestTest(){
        //Arrange
        User author = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-3333-5555", "Party Time");
        author.setUserId(1);
        User interestedUser = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");
        interestedUser.setUserId(2);
        Trade trade = new Trade("2000c", "Interstellar", author);
        trade.setTradeId(1);

        //Act
        String message = "I'll do it for 1800c";
        Interest interest = new Interest(interestedUser, trade, message);
        interest.setInterestId(1);

        //Assert
        assertEquals(message, interest.getComment());
    }

    @Test
    void userInInterestTest(){
        //Arrange
        User author = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-3333-5555", "Party Time");
        author.setUserId(1);
        User interestedUser = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");
        interestedUser.setUserId(2);
        Trade trade = new Trade("2000c", "Interstellar", author);
        trade.setTradeId(1);

        //Act
        Interest interest = new Interest(interestedUser, trade, "I'll do it for 1800c");

        //Assert
        assertEquals("yourivdloo", interest.getUser().getUserName());
    }

    @Test
    void userInTradeInInterestTest(){
        //Arrange
        User author = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-3333-5555", "Party Time");
        author.setUserId(1);
        User interestedUser = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");
        interestedUser.setUserId(2);
        Trade trade = new Trade("2000c", "Interstellar", author);
        trade.setTradeId(1);

        //Act
        Interest interest = new Interest(interestedUser, trade, "I'll do it for 1800c");

        //Assert
        assertEquals("Pjuim", interest.getTrade().getUser().getUserName());
    }

    @Test
    void tradeInInterestTest(){
        //Arrange
        User author = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-3333-5555", "Party Time");
        author.setUserId(1);
        User interestedUser = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");
        interestedUser.setUserId(2);
        Trade trade = new Trade("2000c", "Interstellar", author);
        trade.setTradeId(1);

        //Act
        Interest interest = new Interest(interestedUser, trade, "I'll do it for 1800c");

        //Assert
        assertEquals("2000c", interest.getTrade().getWants());
    }
}
