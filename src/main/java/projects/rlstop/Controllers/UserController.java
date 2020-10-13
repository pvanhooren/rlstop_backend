package projects.rlstop.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.Models.Database.User;
import projects.rlstop.Repositories.UserRepository;

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
    private UserRepository userRepository;

    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        int size = getIterableSize(users);

        if(size == 0){
            return new ResponseEntity("There are currently no users in the database.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Iterable<User>>(users, HttpStatus.FOUND);
    }

    @GetMapping(path="/filter")
    public @ResponseBody ResponseEntity<ArrayList<User>> getUsersByPlatform(@RequestParam String platform){
        ArrayList<User> users = new ArrayList<User>();

        if (platform.equals("")) {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<User> usersIterable = userRepository.findAllByPlatform(platform);
                for(User user : usersIterable){
                    if(user !=null) {
                        users.add(user);
                    }
                }
            }
        }

        return new ResponseEntity<ArrayList<User>>(users, HttpStatus.FOUND);
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<User> getUserPath(@PathVariable int Id) {
        Optional<User> user = userRepository.findById(Id);
        if (user.isPresent()){
            return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity("Please provide a valid user ID", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity deleteUser(@PathVariable int Id){
        Optional<User> user = userRepository.findById(Id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return new ResponseEntity("User has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity("User doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<User> createUser(@RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        if(name != null && !name.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty() && platform != null && !platform.isEmpty() && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            User user = new User(name, email, password, platform, platformID, wishlist);
            User result = userRepository.save(user);
            return new ResponseEntity<User>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity("The user can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int Id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) String password, @RequestParam(required= false) String platform, @RequestParam(required= false) String platformID, @RequestParam(required= false) String wishlist) {
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()){
            User user = optUser.get();
            if(name!=null && !name.isEmpty()) { user.setUserName(name); }
            if(email!=null && !email.isEmpty()) { user.setEmailAddress(email); }
            if(password!=null && !password.isEmpty()) { user.setPasswordHash(Objects.hash(password)); }
            if(platform!=null && !platform.isEmpty()) { user.setPlatform(platform); }
            if(platformID!=null && !platformID.isEmpty()) { user.setUserName(platformID); }
            if(wishlist!=null && !wishlist.isEmpty()) { user.addToWishlist(wishlist); }
            User updatedUser = userRepository.save(user);
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK) ;
        }
        return new ResponseEntity("The user you are trying to update does not exist.", HttpStatus.NOT_FOUND);
    }

    public int getIterableSize(Iterable<User> users){
        int size = 0;

        for(User user : users){
            size++;
        }

        return size;
    }
}
