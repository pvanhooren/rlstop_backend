package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;

import projects.rlstop.models.database.User;

@SpringBootTest
class UserTests {

    @Test
    void hashPasswordTest() {
        //Act
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time");

        //Assert
        assertEquals(Objects.hash("12345zes"), user.getPasswordHash());
    }

    @Test
    void createUserTest(){
        //Act
        User user = new User("Kaasje", "koen@gmail.com", "kaasje123", "PlayStation", "hutskoen", "Road Hog XL");

        //Assert
        assertEquals("Kaasje", user.getUserName());
    }

    @Test
    void addToWishlistTest(){
        //Arrange
        User user = new User("Fifalosophy", "matthy@gmail.com", "duimpiee", "XBox", "matthyas", "Tidal Stream");

        //Act
        user.addToWishlist("20XX");

        //Assert
        assertEquals("20XX", user.getWishlist().get(1));
    }

    @Test
    void multiWishlistTest(){
        //Act
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time,Emeralds");
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
    void removeFromWishlist(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time,Emeralds");
        List<String> expected = new ArrayList<>();
        expected.add("Emeralds");

        //Act
        user.removeFromWishlist("Party Time");

        //Assert
        assertEquals(expected, user.getWishlist());
    }

    @Test
    void clearWishlist(){
        //Arrange
        User user = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-0123-4567-8910", "Party Time,Emeralds");
        List<String> expected = new ArrayList<>();

        //Act
        user.clearWishlist();

        //Assert
        assertEquals(expected, user.getWishlist());
    }
}
