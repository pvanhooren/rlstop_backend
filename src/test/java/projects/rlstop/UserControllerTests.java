package projects.rlstop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.controllers.UserController;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.models.AuthRequest;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserControllerTests {
    UserController userController = new UserController();
    Base64.Encoder encoder = Base64.getEncoder();

    @Test
    void checkCredsTest(){
        //Arrange
        String name = "Pim";
        String limiter= ":";
        String password= "pimpas";
        String creds = name + limiter + password;
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act
        AuthRequest actual = userController.checkCreds(encodedCreds);

        //Assert
        assertEquals(name, actual.getUserName());
        assertEquals(password, actual.getPassword());
    }

    @Test
    void checkCredsTest2(){
        //Arrange
        String name = "Pim";
        String limiter= ":";
        String password= "";
        String creds = name + limiter + password;
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            userController.checkCreds(encodedCreds);
        });
    }

    @Test
    void checkCredsTest3(){
        //Arrange

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            userController.checkCreds("");
        });
    }
}
