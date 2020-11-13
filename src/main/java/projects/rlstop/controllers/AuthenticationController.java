package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.User;
import projects.rlstop.services.UserService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @GetMapping
    public @ResponseBody ResponseEntity<User> authenticate(@RequestParam String userName) {
        User user = userService.getUserByUserName(userName);

        if(user!=null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        throw new NotFoundException("There is no user in the database with the given username");
    }
}
