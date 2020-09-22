package projects.rlstop.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.Models.Database.User;
import projects.rlstop.Repositories.UserRepository;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path="/filter")
    public @ResponseBody Iterable<User> getUsersByPlatform(@RequestParam String platform){
        if (platform != "") {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                return userRepository.findAllByPlatform(platform);
            }
        }
        return userRepository.findAll();
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody
    User getUserPath(@PathVariable int Id) {
        //User user = fakeDatabase.getUser(Id);
        Optional<User> optUser = userRepository.findById(Id);
        if (optUser.isPresent()){
            return optUser.get();
        } else {
            //return "Please provide a valid user ID"
            return null;
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody String deleteUser(@PathVariable int Id){
        Optional<User> user = userRepository.findById(Id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return "User has successfully been deleted.";
        } else {
            return "User doesn't exist. Might have already been deleted.";
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody User createUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String platform, @RequestParam String platformID, @RequestParam String wishlist) {
        if(name != "" && email != "" && password != "" && platform != "" && platformID != "" && wishlist != "") {
            User user = new User(name, email, password, platform, platformID, wishlist);
            User result = userRepository.save(user);
            return result;
        }
        return null;
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody String updateUser(@PathVariable int Id, @RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String platform, @RequestParam String platformID, @RequestParam String wishlist) {
        // Idempotent method. Always update (even if the resource has already been updated before).
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()){
            User user = optUser.get();
            if(name!="") { user.setUserName(name); }
            if(email!="") { user.setEmailAddress(email); }
            if(password!="") { user.setPasswordHash(Objects.hash(password)); }
            if(platform!="") { user.setPlatform(platform); }
            if(platformID!="") { user.setUserName(platformID); }
            if(wishlist!="") { user.addToWishlist(wishlist); }
            userRepository.save(user);
            return "User successfully updated.";
        }
        return "User was not updated because it was not complete.";
    }

}
