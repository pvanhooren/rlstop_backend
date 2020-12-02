package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.services.UserService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<User>> getUsersByPlatform(@RequestParam Platform platform){
        List<User> users = userService.getUsersByPlatform(platform);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path= "/name/{userName}")
    public @ResponseBody ResponseEntity<User> getUserByUserName(@PathVariable String userName){
        User user = userService.getUserByUserName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User has successfully been deleted.", HttpStatus.OK);
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<User> createUser( @RequestParam(required= false) String creds, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        if(creds != null && !creds.isEmpty() && email != null && !email.isEmpty() && platform != null && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            String credentials = new String(Base64.getDecoder().decode(creds.getBytes()));
            final StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
            final String name = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
            User user = new User(name, email, password, platform, platformID, wishlist);
            User result = userService.createUser(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        throw new BadRequestException("The user can not be added because it is not complete");
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID) {
        User user = userService.getUserById(id);

            if (name != null && !name.isEmpty()) {
                user.setUserName(name);
            }
            if (email != null && !email.isEmpty()) {
                user.setEmailAddress(email);
            }
            if (platform != null) {
                user.setPlatform(platform);
            }
            if (platformID != null && !platformID.isEmpty()) {
                user.setPlatformID(platformID);
            }

            User result = userService.updateUser(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/add/{item}")
    public @ResponseBody ResponseEntity<User> addToWishlist(@PathVariable int id, @PathVariable String item) {
        User user = userService.addToWishlist(id, item);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping(path = "/{id}/remove/{item}")
    public @ResponseBody ResponseEntity<User> removeFromWishlist(@PathVariable int id, @PathVariable String item){
        User user = userService.removeFromWishlist(id, item);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/clear")
    public @ResponseBody ResponseEntity<User> clearWishlist(@PathVariable int id){
        User user = userService.clearWishlist(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
