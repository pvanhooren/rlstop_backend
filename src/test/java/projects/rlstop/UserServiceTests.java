package projects.rlstop;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.InternalServerException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.helpers.JwtUtil;
import projects.rlstop.models.AuthRequest;
import projects.rlstop.models.AuthResponse;
import projects.rlstop.models.database.Role;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.models.enums.UserRole;
import projects.rlstop.repositories.UserRepository;
import projects.rlstop.services.UserService;

import java.util.*;

@SpringBootTest
class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    UserService userService;

    Base64.Encoder encoder = Base64.getEncoder();

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
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));
        user1.setPlatformID("SW-0000-1111-2222");
        when(userRepository.save(user1)).thenReturn(user1);

        //Act
        User actual = userService.updateUser(user1.getUserId(), user1.getUserName(), user1.getEmailAddress(), user1.getPlatform(), user1.getPlatformID());

        //Assert
        assertEquals(user1, actual);
    }

    @Test
    void updateUserTest2(){
        //Arrange
        User user4 = new User("Pjuim", "a", "a", Platform.PLAYSTATION, "a", "a");
        user4.setUserId(4);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("a")).thenReturn(Optional.empty());
        when(userRepository.findById(4)).thenReturn(Optional.of(user4));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.updateUser(4, "Pjuim", "a", Platform.PLAYSTATION, "a");
        });
    }

    @Test
    void updateUserTest3(){
        //Arrange
        User user4 = new User("a", "nikkipim@gmail.com", "a", Platform.PLAYSTATION, "a", "a");
        user4.setUserId(4);
        when(userRepository.findByUserName("a")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));
        when(userRepository.findById(4)).thenReturn(Optional.of(user4));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.updateUser(4, "a", "nikkipim@gmail.com", Platform.PLAYSTATION, "a");
        });
    }

    @Test
    void updateUserTest4(){
        //Arrange
        User user4 = new User("a", "nikkipim@gmail.com", "a", Platform.PLAYSTATION, "", "a");
        user4.setUserId(4);
        when(userRepository.findById(4)).thenReturn(Optional.of(user4));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.updateUser(4, "a", "nikkipim@gmail.com", Platform.PLAYSTATION, "");
        });

    }

    @Test
    void createUserTest(){
        //Arrange
        user3.setUserId(3);
        when(userRepository.findByUserName("yourivdloo")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("youri.yvdl@gmail.com")).thenReturn(Optional.empty());
        when(jwtUtil.generateToken("yourivdloo")).thenReturn("token");
        when(userRepository.save(any(User.class))).thenReturn(user3);
        String creds= "yourivdloo:yourivdloo";
        AuthResponse expected = new AuthResponse("token", "yourivdloo", 3, false);

        //Act
        AuthResponse actual = userService.createUser(encoder.encodeToString(creds.getBytes()), user3.getEmailAddress(), user3.getPlatform(), user3.getPlatformID(), "Fennec");

        //Assert
        assertEquals(expected.getToken(), actual.getToken());
    }

    @Test
    void createUserTest2(){
        //Arrange
        user1.setUserId(1);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmailAddress("a")).thenReturn(Optional.empty());
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
           userService.createUser(encodedCreds, "a", Platform.PC, "a", "a");
        });
    }

    @Test
    void createUserTest3(){
        //Arrange
        when(userRepository.findByUserName("a")).thenReturn(Optional.empty());
        user1.setUserId(1);
        when(userRepository.findByEmailAddress("nikkipim@gmail.com")).thenReturn(Optional.of(user1));
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.createUser(encodedCreds, "nikkipim@gmail.com", Platform.PC, "a", "a");
        });
    }

    @Test
    void createUserTest4(){
        //Arrange
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.createUser(encodedCreds, "nikkipim@gmail.com",  Platform.PC, "", "a");
        });
    }

    @Test
    void deactivateUserTest(){
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act
        boolean actual = userService.deactivateUser(1);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void deactivateUserTest2(){
        //Arrange
        user1.setUserId(1);
        user1.setActive(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.deactivateUser(1);
        });
    }

    @Test
    void reactivateUserTest(){
        user1.setUserId(1);
        user1.setActive(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act
        boolean actual = userService.reactivateUser(1);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void reactivateUserTest2(){
        //Arrange
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.reactivateUser(1);
        });
    }

    @Test
    void makeAdminTest(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        user1.setRoles(roles);
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act
        boolean actual = userService.makeAdmin(1);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void makeAdminTest2(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        roles.add(new Role(UserRole.ROLE_ADMIN));
        user1.setRoles(roles);
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.makeAdmin(1);
        });
    }

    @Test
    void removeAdminTest(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        roles.add(new Role(UserRole.ROLE_ADMIN));
        user1.setRoles(roles);
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act
        boolean actual = userService.removeAdmin(1);

        //Assert
        assertEquals(true, actual);
    }

    @Test
    void removeAdminTest2(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        user1.setRoles(roles);
        user1.setUserId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //Act

        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.removeAdmin(1);
        });
    }

    @Test
    void authenticateTest(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        roles.add(new Role(UserRole.ROLE_ADMIN));
        user1.setRoles(roles);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(jwtUtil.generateToken("Pjuim")).thenReturn("token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        AuthResponse expected = new AuthResponse("token", "Pjuim", 1, true);
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act
        AuthResponse actual = userService.authenticate(encodedCreds);

        //Assert
        assertEquals(expected.getUserName(), actual.getUserName());
    }

    @Test
    void authenticateTest2(){
        //Arrange
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(jwtUtil.generateToken("Pjuim")).thenReturn("token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException());
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act

        //Assert
        assertThrows(NotFoundException.class, () ->  {
            userService.authenticate(encodedCreds);
        });
    }

    @Test
    void authenticateTest3(){
        //Arrange
        List<Role> roles = new ArrayList();
        roles.add(new Role(UserRole.ROLE_USER));
        roles.add(new Role(UserRole.ROLE_ADMIN));
        user1.setRoles(roles);
        user1.setActive(false);
        when(userRepository.findByUserName("Pjuim")).thenReturn(Optional.of(user1));
        when(jwtUtil.generateToken("Pjuim")).thenReturn("token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        AuthResponse expected = new AuthResponse("token", "Pjuim", 1, true);
        String creds = "Pjuim:pimpas";
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act


        //Assert
        assertThrows(BadRequestException.class, () ->  {
            userService.authenticate(encodedCreds);
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

    @Test
    void checkCredsTest(){
        //Arrange
        String name = "Pim";
        String limiter= ":";
        String password= "pimpas";
        String creds = name + limiter + password;
        String encodedCreds = encoder.encodeToString(creds.getBytes());

        //Act
        AuthRequest actual = userService.checkCreds(encodedCreds);

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
            userService.checkCreds(encodedCreds);
        });
    }

    @Test
    void checkCredsTest3(){
        //Arrange

        //Act

        //Assert
        Assertions.assertThrows(BadRequestException.class, () ->{
            userService.checkCreds("");
        });
    }

}
