package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.models.database.User;
import projects.rlstop.services.UserService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping("/users")
public class UserController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private UserService userService;

    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<Object> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if(users.isEmpty()){
            return new ResponseEntity<>("There are currently no users in the database.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<User>> getUsersByPlatform(@RequestParam String platform){
        List<User> users = userService.getUsersByPlatform(platform);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Object> getUserPath(@PathVariable int id) {
        User user = userService.getUserById(id);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Please provide a valid user ID", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteUser(@PathVariable int id){
        boolean successful = userService.deleteUser(id);

        if(successful){
            return new ResponseEntity<>("User has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Object> createUser(@RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        if(name != null && !name.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty() && platform != null && !platform.isEmpty() && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            User result = userService.createUser(name, email, password, platform, platformID, wishlist);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>("The user can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Object> updateUser(@PathVariable int id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID) {
        User user = userService.updateUser(id, name, email, platform, platformID);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK) ;
        }
        return new ResponseEntity<>("The user you are trying to update does not exist.", HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}/password")
    public @ResponseBody ResponseEntity<Object> changePassword(@PathVariable int id, @RequestParam String oldPassword, @RequestParam String newPassword){
        User user = userService.changePassword(id, oldPassword, newPassword);

        if(user!=null){
           return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("The password was not changed because the old password was incorrect", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{id}/add/{item}")
    public @ResponseBody ResponseEntity<User> addToWishlist(@PathVariable int id, @PathVariable String item){
        User user = userService.addToWishlist(id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }

    @PutMapping(path = "/{id}/remove/{item}")
    public @ResponseBody ResponseEntity<User> removeFromWishlist(@PathVariable int id, @PathVariable String item){
        User user = userService.removeFromWishlist(id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }

    @PutMapping(path = "/{id}/clear")
    public @ResponseBody ResponseEntity<User> clearWishlist(@PathVariable int id){
        User user = userService.clearWishlist(id);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }
}
