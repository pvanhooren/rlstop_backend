package projects.rlstop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.repositories.TradeRepository;
import projects.rlstop.repositories.UserRepository;
import projects.rlstop.services.TradeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    TradeRepository tradeRepository;

    @InjectMocks
    TradeService tradeService;

    User user1 = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-2222-3333", "Party Time,Emeralds");
    User user2 = new User("R3MC0", "remcovo@gmail.com", "voetbalman5", Platform.NINTENDOSWITCH, "SW-1234-5678-9000", "Dissolver");
    User user3 = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");

    Trade trade1 = new Trade("Octane: Dot Rush", "50c", user1);
    Trade trade2 = new Trade("Stipple Gait", "Storm Watch + Chameleon", user2);
    Trade trade3 = new Trade("Fennec", "Imperator DT5", user3);
    Trade trade4 = new Trade("Party Time", "Cactus Hat", user3);

    @Test
    void getAllTradesTest(){
        //Arrange
        Trade[] trades = {trade1, trade2, trade3, trade4};

        Iterable<Trade> tradeList = Arrays.asList(trades);

        when(tradeRepository.findAllByOrderByLastModifiedDesc()).thenReturn(tradeList);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(user2));
        when(userRepository.findById(3)).thenReturn(java.util.Optional.ofNullable(user3));

        List<Trade> expected = new ArrayList<>();
        expected.add(trade1);
        expected.add(trade2);
        expected.add(trade3);
        expected.add(trade4);

        //Act
        List<Trade> actual = tradeService.getAllTrades();

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllTradesTest2(){
        //Arrange
        Trade[] trades = {};

        Iterable<Trade> tradeList = Arrays.asList(trades);

        when(tradeRepository.findAllByOrderByLastModifiedDesc()).thenReturn(tradeList);

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->  {
            tradeService.getAllTrades();
        });
    }

    @Test
    void getTradesByUserTest(){
        //Arrange
        List<Trade> tradesByUser3 = new ArrayList<>();
        tradesByUser3.add(trade3);
        tradesByUser3.add(trade4);

        when(tradeRepository.findAllByUserUserIdOrderByLastModifiedDesc(3)).thenReturn(tradesByUser3);
        when(userRepository.findById(3)).thenReturn(Optional.ofNullable(user3));

        //Act
        List<Trade> actual = tradeService.getTradesByUser(3);

        //Assert
        assertEquals(tradesByUser3, actual);
    }

    @Test
    void getTradesByUserTest2(){
        //Arrange
        when(userRepository.findById(0)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradesByUser(0);
        });
    }

    @Test
    void getTradesByUserTest3(){
        //Arrange
        List<Trade> tradesByUser3 = new ArrayList<>();

        when(tradeRepository.findAllByUserUserIdOrderByLastModifiedDesc(3)).thenReturn(tradesByUser3);
        when(userRepository.findById(3)).thenReturn(Optional.ofNullable(user3));

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradesByUser(3);
        });
    }

    @Test
    void getTradesByPlatformTest(){
        //Arrange
        List<Trade> tradesOnSwitch = new ArrayList<>();
        tradesOnSwitch.add(trade1);
        tradesOnSwitch.add(trade2);

        when(tradeRepository.findAllByUserPlatformOrderByLastModifiedDesc(Platform.NINTENDOSWITCH)).thenReturn(tradesOnSwitch);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(user2));

        //Act
        List<Trade> actual = tradeService.getTradesByPlatform(Platform.NINTENDOSWITCH);

        //Assert
        assertEquals(tradesOnSwitch, actual);
    }

    @Test
    void getTradesByPlatformTest2(){
        //Arrange

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradesByPlatform(null);
        });
    }

    @Test
    void getTradesByPlatformTest3(){
        //Arrange
        List<Trade> tradesonPS = new ArrayList<>();

        when(tradeRepository.findAllByUserPlatformOrderByLastModifiedDesc(Platform.PLAYSTATION)).thenReturn(tradesonPS);

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradesByPlatform(Platform.PLAYSTATION);
        });
    }

    @Test
    void getTradeByIdTest(){
        //Arrange
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));
        when(tradeRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(trade1));

        //Act
        Trade actual = tradeService.getTradeById(1);

        //Assert
        assertEquals(trade1, actual);
    }

    @Test
    void getTradeByIdTest2(){
        //Arrange
        when(tradeRepository.findById(5)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradeById(5);
        });
    }

    @Test
    void getTradeByIdTest3(){
        //Arrange
        when(tradeRepository.findById(2)).thenReturn(Optional.ofNullable(trade4));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(NotFoundException.class, () ->{
            tradeService.getTradeById(2);
        });
    }

    @Test
    void deleteTradeTest(){
        //Arrange
        user2.setUserId(2);
        when(tradeRepository.findById(2)).thenReturn(Optional.ofNullable(trade2));
        when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));

        //Act
        boolean actual = tradeService.deleteTrade(2);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void saveTradeTest(){
        //Arrange
        user2.setUserId(2);
        when(tradeRepository.save(trade2)).thenReturn(trade2);
        when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));

        //Act
        Trade actual = tradeService.saveTrade(trade2, 2);

        //Assert
        assertEquals(trade2, actual);
    }

    @Test
    void saveTradeTest2(){
        //Arrange
        when(tradeRepository.save(trade2)).thenReturn(trade2);
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            tradeService.saveTrade(trade2, 2);
        });
    }
}
