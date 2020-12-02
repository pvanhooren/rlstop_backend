package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;

@SpringBootTest
class UserTests {
    @Test
    void setPasswordHashTest(){
        //Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User("a", "a","a", Platform.PC,"a","a");
        String expected = "pimpas";

        //Act
        user.setPasswordHash("pimpas");

        //Assert
        assertEquals(true, encoder.matches(expected, user.getPasswordHash()));
    }

    @Test
    void hashPasswordTest(){
        //Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String expected = "pimpas";

        //Act
        User user = new User("a", "a","pimpas",Platform.PC,"a","a");

        //Assert
        assertEquals(true, encoder.matches(expected, user.getPasswordHash()));
    }

    @Test
    void createUserTest(){
        //Act
        User user = new User("Kaasje", "koen@gmail.com", "kaasje123", Platform.PLAYSTATION, "hutskoen", "Road Hog XL");

        //Assert
        assertEquals("Kaasje", user.getUserName());
    }

    @Test
    void addToWishlistTest(){
        //Arrange
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", Platform.XBOX, "matthyas", "Tidal Stream");

        //Act
        user.addToWishlist("20XX");

        //Assert
        assertEquals("20XX", user.getWishlist().get(1));
    }

    @Test
    void multiWishlistTest(){
        //Act
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-0123-4567-8910", "Party Time,Emeralds");
        user.addToWishlist("Dot Rush,RLCS X Fennec");

        List<String> expected = new ArrayList<>();
        expected.add("Party Time");
        expected.add("Emeralds");
        expected.add("Dot Rush");
        expected.add("RLCS X Fennec");

        //Assert
        assertEquals(expected, user.getWishlist());
    }

    @Test
    void removeFromWishlistTest(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-0123-4567-8910", "Party Time,Emeralds");
        List<String> expected = new ArrayList<>();
        expected.add("Emeralds");

        //Act
        user.removeFromWishlist("Party Time");

        //Assert
        assertEquals(expected, user.getWishlist());
    }

    @Test
    void clearWishlistTest(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-0123-4567-8910", "Party Time,Emeralds");
        List<String> expected = new ArrayList<>();

        //Act
        user.clearWishlist();

        //Assert
        assertEquals(expected, user.getWishlist());
    }
}
