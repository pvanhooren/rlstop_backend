package projects.rlstop;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import projects.rlstop.models.database.User;
import projects.rlstop.repositories.UserRepository;
import projects.rlstop.services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user1 = new User("Pjuim", "nikkipim@gmail.com", "12345zes", "NintendoSwitch", "SW-1111-2222-3333", "Party Time,Emeralds");
    User user2 = new User("R3MC0", "remcovo@gmail.com", "voetbalman5", "NintendoSwitch", "SW-1234-5678-9000", "Dissolver");
    User user3 = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", "PC", "yourivdloo", "Fennec");

    @Test
    void getAllUsersTest(){
        //Arrange
        User[] users = {user1, user2, user3};

        Iterable<User> userList = Arrays.asList(users);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> expected = new ArrayList<User>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);

        //Act
        List<User> actual = userService.getAllUsers();

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void getUsersByPlatformTest(){
        //Arrange
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAllByPlatform("NintendoSwitch")).thenReturn(users);

        //Act
        List<User> actual = userService.getUsersByPlatform("NintendoSwitch");

        //Assert
        assertEquals(users, actual);
    }

    @Test
    void getUserByIdTest(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));

        //Act
        User user = userService.getUserById(1);

        //Assert
        assertEquals(user1, user);
    }

    @Test
    void getUserByIdTest2(){
        //Arrange
        when(userRepository.findById(4)).thenReturn(Optional.empty());

        //Act
        User user = userService.getUserById(4);

        //Assert
        assertEquals(null, user);
    }

    @Test
    void getUserByUserNameTest(){
        //Arrange
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.ofNullable(user1));

        //Act
        User user = userService.getUserByUserName("Pjuim");

        //Assert
        assertEquals(user1, user);
    }

    @Test
    void getUserByUserNameTest2(){
        //Arrange
        when(userRepository.findByUserName("JonSandman")).thenReturn(Optional.empty());

        //Act
        User user = userService.getUserByUserName("JonSandman");

        //Assert
        assertEquals(null, user);
    }

    @Test
    void deleteUserTest(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));

        //Act
        boolean actual = userService.deleteUser(1);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void deleteUserTest2(){
        //Arrange
        when(userRepository.findById(5)).thenReturn(java.util.Optional.empty());

        //Act
        boolean actual = userService.deleteUser(5);

        //Assert
        assertEquals(false, actual);
    }

    @Test
    void saveUserTest(){
        //Arrange
        when(userRepository.save(user2)).thenReturn(user2);

        //Act
        User actual = userService.saveUser(user2);

        //Assert
        assertEquals(user2, actual);
    }

    @Test
    void addToWishlistTest(){
        //Arrange
        when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));
        when(userRepository.save(user2)).thenReturn(user2);

        user2.addToWishlist("Bubbly");

        //Act
        User actual = userService.addToWishlist(2, "Bubbly");

        //Assert
        assertEquals(user2, actual);
    }

    @Test
    void addToWishlistTest2(){
        //Arrange
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        //Act
        User actual = userService.addToWishlist(2, "Bubbly");

        //Assert
        assertEquals(null, actual);
    }

    @Test
    void removeFromWishlistTest(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        user1.removeFromWishlist("Emeralds");

        //Act
        User actual = userService.removeFromWishlist(1, "Emeralds");

        //Assert
        assertEquals(user1, actual);
    }

    @Test
    void removeFromWishlistTest2(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        user1.removeFromWishlist("Emeralds");

        //Act
        User actual = userService.removeFromWishlist(1, "Emeralds");

        //Assert
        assertEquals(null, actual);
    }

    @Test
    void clearWishlistTest(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        user1.clearWishlist();

        //Act
        User actual = userService.clearWishlist(1);

        //Assert
        assertEquals(user1, actual);
    }

    @Test
    void clearWishlistTest2(){
        //Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        user1.clearWishlist();

        //Act
        User actual = userService.clearWishlist(1);

        //Assert
        assertEquals(null, actual);
    }

}
