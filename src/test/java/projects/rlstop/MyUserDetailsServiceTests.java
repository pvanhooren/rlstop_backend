package projects.rlstop;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import projects.rlstop.models.MyUserDetails;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.repositories.UserRepository;
import projects.rlstop.services.MyUserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MyUserDetailsServiceTests {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    @Test
    void loadByUserNameTest(){
        //Arrange
        User pim = new User("Pim", "pim@pim.pim", "pimpas", Platform.NINTENDOSWITCH, "SW-0000-0000-0000", "Nothing really");

        when(userRepository.findByUserName("Pim")).thenReturn(java.util.Optional.of(pim));

        //Act
        UserDetails actual = myUserDetailsService.loadUserByUsername("Pim");

        //Assert
        Optional<User> optionalPim = Optional.of(pim);
        UserDetails expected = optionalPim.map(MyUserDetails::new).get();
        assertEquals(expected.getUsername(), actual.getUsername());
    }
}
