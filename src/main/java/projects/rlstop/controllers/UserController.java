package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.models.AuthResponse;
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
    public @ResponseBody ResponseEntity<AuthResponse> createUser(@RequestParam(required= false) String creds, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        AuthResponse response = userService.createUser(creds, email, platform, platformID, wishlist);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID) {
        User result = userService.updateUser(id, name, email, platform, platformID);
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

    @GetMapping("/auth")
    public @ResponseBody ResponseEntity<AuthResponse> authenticate(@RequestParam(required=false) String creds){
        AuthResponse response = userService.authenticate(creds);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
