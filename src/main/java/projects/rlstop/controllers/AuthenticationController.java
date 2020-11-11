package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import projects.rlstop.models.database.User;
import projects.rlstop.services.UserService;

import javax.ws.rs.Path;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @GetMapping
    public @ResponseBody ResponseEntity<Object> authenticate(String userName) {
        //throw new RuntimeException("Some Error has Happened! Contact Support at ***-***");
        User user = userService.getUserByUserName(userName);

        if(user!=null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>("There is no user in the database with the given username", HttpStatus.NOT_FOUND);
    }
}
