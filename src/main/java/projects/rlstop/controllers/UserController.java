package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.InternalServerException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.services.UserService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/users")
public class UserController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private UserService userService;

    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if(users.isEmpty()){
            throw new NotFoundException("There are currently no users in the database.");
        }

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

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new NotFoundException("User was not found. Please provide a valid user ID");
        }
    }

    @GetMapping(path= "/name/{userName}")
    public @ResponseBody ResponseEntity<User> getUserByUserName(@PathVariable String userName){
        User user = userService.getUserByUserName(userName);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new NotFoundException("User was not found. Please provide a valid username.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteUser(@PathVariable int id){
        boolean successful = userService.deleteUser(id);

        if(successful){
            return new ResponseEntity<>("User has successfully been deleted.", HttpStatus.OK);
        } else {
            throw new NotFoundException("User doesn't exist. Might have already been deleted.");
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<User> createUser(@RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        if(name != null && !name.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty() && platform != null && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            User user = new User(name, email, password, platform, platformID, wishlist);
            User result = userService.createUser(user);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        throw new BadRequestException("The user can not be added because it is not complete");
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID) {
        User user = userService.getUserById(id);

        if(user != null){
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
        } else {
            throw new NotFoundException("The user you are trying to update does not exist.");
        }

        User result = userService.updateUser(user);

        if(result != null){
            return new ResponseEntity<>(result, HttpStatus.OK) ;
        }
        throw new BadRequestException("An error occurred trying to update the trade.");
    }

    @PutMapping(path = "/{id}/add/{item}")
    public @ResponseBody ResponseEntity<User> addToWishlist(@PathVariable int id, @PathVariable String item){
        User user = userService.addToWishlist(id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new InternalServerException("The item was not added, an error occurred.");
    }

    @PutMapping(path = "/{id}/remove/{item}")
    public @ResponseBody ResponseEntity<User> removeFromWishlist(@PathVariable int id, @PathVariable String item){
        User user = userService.removeFromWishlist(id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new InternalServerException("The item was not removed, an error occured.");
    }

    @PutMapping(path = "/{id}/clear")
    public @ResponseBody ResponseEntity<User> clearWishlist(@PathVariable int id){
        User user = userService.clearWishlist(id);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new InternalServerException("The wishlist was not cleared, an error occured.");
    }
}
