package projects.rlstop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.Interest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.repositories.InterestRepository;
import projects.rlstop.repositories.TradeRepository;
import projects.rlstop.repositories.UserRepository;
import projects.rlstop.services.InterestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class InterestServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    TradeRepository tradeRepository;

    @Mock
    InterestRepository interestRepository;

    @InjectMocks
    InterestService interestService;

    User user1 = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-2222-3333", "Party Time,Emeralds");
    User user2 = new User("R3MC0", "remcovo@gmail.com", "voetbalman5", Platform.NINTENDOSWITCH, "SW-1234-5678-9000", "Dissolver");
    User user3 = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");
    User user4 = new User("MrSandman", "jonsandman@gmail.com", "codesand", Platform.PC, "jonsandman", "Duelling Dragons");

    Trade trade1 = new Trade("Octane: Dot Rush", "50c", user1);
    Trade trade2 = new Trade("Stipple Gait", "Storm Watch + Chameleon", user2);
    Trade trade3 = new Trade("Fennec", "Imperator DT5", user3);
    Trade trade4 = new Trade("Party Time", "Cactus Hat", user3);

    Interest interest1 = new Interest(user1, trade4, "Just kidding, not interested.");
    Interest interest2 = new Interest(user2, trade4, "Add 1000c and maybe.");
    Interest interest3 = new Interest(user3, trade1, "If you pay my trading tax");
    Interest interest4 = new Interest(user1, trade2, "Add 100c");

    @Test
    void getInterestsByUserTest(){
        //Arrange
        List<Interest> interestsByUser1 = new ArrayList<>();
        interestsByUser1.add(interest1);
        interestsByUser1.add(interest4);

        when(interestRepository.findAllByUserUserId(1)).thenReturn(interestsByUser1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));

        //Act
        List<Interest> actual = interestService.getInterestsByUser(1);

        //Assert
        assertEquals(interestsByUser1, actual);
    }

    @Test
    void getInterestsByUserTest2(){
        //Arrange
        when(userRepository.findById(0)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            interestService.getInterestsByUser(0);
        });
    }

    @Test
    void getInterestsByUserTest3(){
        //Arrange
        List<Interest> interestsByUser4 = new ArrayList<>();

        when(interestRepository.findAllByUserUserId(4)).thenReturn(interestsByUser4);
        when(userRepository.findById(4)).thenReturn(Optional.ofNullable(user4));

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            interestService.getInterestsByUser(4);
        });
    }

    @Test
    void getInterestsByTradeTest(){
        //Arrange
        List<Interest> interestsOnTrade4 = new ArrayList<>();
        interestsOnTrade4.add(interest1);
        interestsOnTrade4.add(interest2);

        when(interestRepository.findAllByTradeTradeId(4)).thenReturn(interestsOnTrade4);
        when(tradeRepository.findById(4)).thenReturn(Optional.ofNullable(trade4));

        //Act
        List<Interest> actual = interestService.getInterestsByTrade(4);

        //Assert
        assertEquals(interestsOnTrade4, actual);
    }

    @Test
    void getInterestsByTradeTest2(){
        //Arrange
        when(tradeRepository.findById(0)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            interestService.getInterestsByTrade(0);
        });
    }

    @Test
    void getInterestsByTradeTest3(){
        List<Interest> interestsOnTrade3 = new ArrayList<>();

        when(interestRepository.findAllByTradeTradeId(3)).thenReturn(interestsOnTrade3);
        when(tradeRepository.findById(3)).thenReturn(Optional.ofNullable(trade3));

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            interestService.getInterestsByTrade(3);
        });
    }

    @Test
    void saveInterestTest(){
        //Arrange
        user1.setUserId(1);
        trade4.setTradeId(4);
        when(interestRepository.save(interest1)).thenReturn(interest1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));
        when(tradeRepository.findById(4)).thenReturn(Optional.ofNullable(trade4));

        //Act
        Interest actual = interestService.saveInterest(interest1, 1, 4);

        //Assert
        assertEquals(interest1, actual);
    }

    @Test
    void saveInterestTest2(){
        //Arrange
        when(interestRepository.save(interest3)).thenReturn(interest3);
        when(userRepository.findById(3)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            interestService.saveInterest(interest3, 3,1);
        });
    }

    @Test
    void saveInterestTest3(){
        //Arrange
        when(interestRepository.save(interest3)).thenReturn(interest3);
        when(userRepository.findById(3)).thenReturn(Optional.ofNullable(user3));
        when(tradeRepository.findById(3)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            interestService.saveInterest(interest3, 3, 3);
        });
    }

    @Test
    void deleteInterestTest(){
        //Arrange
        when(interestRepository.findById(4)).thenReturn(Optional.ofNullable(interest4));

        //Act
        boolean actual = interestService.deleteInterest(4);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void deleteInterestTest2(){
        //Arrange
        when(interestRepository.findById(5)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            interestService.deleteInterest(5);
        });
    }
}
