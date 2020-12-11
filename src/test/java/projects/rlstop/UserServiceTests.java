package projects.rlstop;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.InternalServerException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
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

    User user1 = new User("Pjuim", "nikkipim@gmail.com", "12345zes", Platform.NINTENDOSWITCH, "SW-1111-2222-3333", "Party Time,Emeralds");
    User user2 = new User("R3MC0", "remcovo@gmail.com", "voetbalman5", Platform.NINTENDOSWITCH, "SW-1234-5678-9000", "Dissolver");
    User user3 = new User("yourivdloo", "youri.yvdl@gmail.com", "yourivdloo", Platform.PC, "yourivdloo", "Fennec");

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

    @Test()
    void getAllUsersTest2(){
        //Arrange
        User[] users = {};

        Iterable<User> userList = Arrays.asList(users);

        when(userRepository.findAll()).thenReturn(userList);

        //Act

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.getAllUsers();
        });
    }

    @Test
    void getUsersByPlatformTest(){
        //Arrange
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAllByPlatform(Platform.NINTENDOSWITCH)).thenReturn(users);

        //Act
        List<User> actual = userService.getUsersByPlatform(Platform.NINTENDOSWITCH);

        //Assert
        assertEquals(users, actual);
    }

    @Test
    void getUsersByPlatformTest2(){
        //Arrange
        List<User> expected = new ArrayList<User>();

        //Act

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.getUsersByPlatform(null);
        });
    }

    @Test
    void getUsersByPlatformTest3(){
        //Arrange
        List<User> users = new ArrayList<>();
        when(userRepository.findAllByPlatform(Platform.XBOX)).thenReturn(users);

        //Act

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.getUsersByPlatform(Platform.XBOX);
        });
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

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.getUserById(4);
        });
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

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.getUserByUserName("JonSandman");
        });
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

        //Assert
        assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(5);
        });
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
    void updateUserTest(){
        //Arrange
        user1.setUserId(1);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));
        user1.setPlatformID("SW-0000-1111-2222");
        when(userRepository.save(user1)).thenReturn(user1);

        //Act
        User actual = userService.updateUser(user1);

        //Assert
        assertEquals(user1, actual);
    }

    @Test
    void updateUserTest2(){
        //Arrange
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("a")).thenReturn(Optional.empty());

        //Act
        User user4 = new User("Pjuim", "a", "a", Platform.PLAYSTATION, "a", "a");
        user4.setUserId(4);

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.updateUser(user4);
        });
    }

    @Test
    void updateUserTest3(){
        //Arrange
        when(userRepository.findByUserName("a")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));

        //Act
        User user4 = new User("a", "nikkipim@gmail.com", "a", Platform.PLAYSTATION, "a", "a");
        user4.setUserId(4);


        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.updateUser(user4);
        });
    }

    @Test
    void createUserTest(){
        //Arrange
        when(userRepository.findByUserName("yourivdloo")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("youri.yvdl@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(user3)).thenReturn(user3);

        //Act
        User actual = userService.createUser(user3);

        //Assert
        assertEquals(user3, actual);
    }

    @Test
    void createUserTest2(){
        //Arrange
        user1.setUserId(1);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("a")).thenReturn(Optional.empty());

        //Act
        User user4 = new User("Pjuim", "a", "a", Platform.PC, "a", "a");

        //Assert
        assertThrows(BadRequestException.class, () ->  {
           userService.createUser(user4);
        });
    }

    @Test
    void createUserTest3(){
        //Arrange
        when(userRepository.findByUserName("a")).thenReturn(Optional.empty());
        user1.setUserId(1);
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));

        //Act
        User user4 = new User("a", "nikkipim@gmail.com", "a", Platform.PC, "a", "a");

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.createUser(user4);
        });
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

        //Assert
        assertThrows(InternalServerException.class, () ->  {
            userService.addToWishlist(2, "Bubbly");
        });
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

        //Act

        //Assert
        assertThrows(InternalServerException.class, () ->  {
            userService.removeFromWishlist(1, "Emeralds");
        });
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

        //Act

        //Assert
        assertThrows(InternalServerException.class, () ->  {
            User actual = userService.clearWishlist(1);
        });
    }

}
