package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import projects.rlstop.models.AuthResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthTests {

    @Test
    void createResponseTest(){
        //Arrange
        String token = "token";
        String name= "Pim";
        int id=1;
        String admin = "BigSplash";

        //Act
        AuthResponse response = new AuthResponse(token,name,id, "BigSplash");

        //Assert
        assertEquals(token, response.getToken());
        assertEquals(name, response.getUserName());
        assertEquals(id, response.getUserId());
        assertEquals(admin, response.getAdminCode());
    }
}
