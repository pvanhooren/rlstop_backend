package projects.rlstop;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import projects.rlstop.helpers.JwtUtil;
import projects.rlstop.helpers.StringListConverter;

@SpringBootTest
class HelperTests {
    StringListConverter slt = new StringListConverter();

    JwtUtil jwtUtil = new JwtUtil();

    @Test
    void convertToStringTest(){
        //Arrange
        List<String> wishlist = new ArrayList<>();
        wishlist.add("Dissolver");
        wishlist.add("20XX");
        wishlist.add("Bubbly");

        //Act
        String actual = slt.convertToDatabaseColumn(wishlist);

        //Assert
        String expected = "Dissolver,20XX,Bubbly";
        assertEquals(expected, actual);
    }

    @Test
    void convertToListTest(){
        //Arrange
        String wishlist = "Interstellar,Ballistic,Voltaic";

        //Act
        List<String> actual = slt.convertToEntityAttribute(wishlist);

        //Assert
        List<String> expected = new ArrayList<String>();
        expected.add("Interstellar");
        expected.add("Ballistic");
        expected.add("Voltaic");
        assertEquals(expected, actual);
    }

    @Test
    void emptyListTest(){
        //Arrange
        List<String> wishlist = new ArrayList<>();

        //Act
        String actual = slt.convertToDatabaseColumn(wishlist);

        //Assert
        String expected = "";
        assertEquals(expected, actual);
    }
}
