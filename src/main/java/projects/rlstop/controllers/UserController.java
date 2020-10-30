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
    public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();

        if(users == null){
            return new ResponseEntity("There are currently no users in the database.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @GetMapping(path="/filter")
    public @ResponseBody ResponseEntity<ArrayList<User>> getUsersByPlatform(@RequestParam String platform){
        ArrayList<User> users = userService.getUsersByPlatform(platform);

        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<User> getUserPath(@PathVariable int Id) {
        User user = userService.getUserById(Id);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } else {
            return new ResponseEntity("Please provide a valid user ID", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity deleteUser(@PathVariable int Id){
        boolean successful = userService.deleteUser(Id);

        if(successful){
            return new ResponseEntity("User has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity("User doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<User> createUser(@RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        if(name != null && !name.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty() && platform != null && !platform.isEmpty() && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            User result = userService.createUser(name, email, password, platform, platformID, wishlist);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }

        return new ResponseEntity("The user can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int Id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID) {
        User user = userService.updateUser(Id, name, email, platform, platformID);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK) ;
        }
        return new ResponseEntity("The user you are trying to update does not exist.", HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{Id}/password")
    public @ResponseBody ResponseEntity<User> changePassword(@PathVariable int Id, @RequestParam String oldPassword, @RequestParam String newPassword){
        User user = userService.changePassword(Id, oldPassword, newPassword);

        if(user!=null){
           return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity("The password was not changed because the old password was incorrect", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{Id}/add/{item}")
    public @ResponseBody ResponseEntity<User> addToWishlist(@PathVariable int Id, @PathVariable String item){
        User user = userService.addToWishlist(Id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }

    @PutMapping(path = "/{Id}/remove/{item}")
    public @ResponseBody ResponseEntity<User> removeFromWishlist(@PathVariable int Id, @PathVariable String item){
        User user = userService.removeFromWishlist(Id, item);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }

    @PutMapping(path = "/{Id}/clear")
    public @ResponseBody ResponseEntity<User> clearWishlist(@PathVariable int Id){
        User user = userService.clearWishlist(Id);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }
}
