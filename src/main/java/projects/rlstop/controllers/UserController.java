package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.helpers.JwtUtil;
import projects.rlstop.models.AuthRequest;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        String error = "Not all fields have been filled in. Please fill in the missing fields.";

        if(creds != null && !creds.isEmpty() && email != null && !email.isEmpty() && platform != null && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            AuthRequest authRequest = checkCreds(creds);

            User user = new User(authRequest.getUserName(), email, authRequest.getPassword(), platform, platformID, wishlist);
            User result = userService.createUser(user);

            String token = jwtUtil.generateToken(authRequest.getUserName());
            AuthResponse response = new AuthResponse(token, result.getUserName(), result.getUserId());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new BadRequestException(error);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable int id, @RequestParam(required= false) String name, @RequestParam(required= false) String email, @RequestParam(required= false) Platform platform, @RequestParam(required= false) String platformID) {
        User user = userService.getUserById(id);

        if (email != null && !email.isEmpty() && name != null && !name.isEmpty() && platform != null && platformID != null && !platformID.isEmpty()){
            user.setUserName(name);
            user.setEmailAddress(email);
            user.setPlatform(platform);
            user.setPlatformID(platformID);

            User result = userService.updateUser(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        throw new BadRequestException("Not all fields have been filled in. Please fill in the missing fields.");
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
        AuthRequest authRequest = checkCreds(creds);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new BadRequestException("Invalid username/password");
        }

        String token = jwtUtil.generateToken(authRequest.getUserName());
        User user = userService.getUserByUserName(authRequest.getUserName());
        AuthResponse response = new AuthResponse(token, user.getUserName(), user.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public AuthRequest checkCreds(String creds){
        if(!creds.isEmpty() && creds != null) {
            String credentials = new String(Base64.getDecoder().decode(creds.getBytes()));
            final StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
            final String name;
            final String password;

            try {
                name = tokenizer.nextToken();
                password = tokenizer.nextToken();
            } catch (NoSuchElementException n) {
                throw new BadRequestException("Something went wrong ");
            }

            return new AuthRequest(name, password);
        }

        throw new BadRequestException("Please provide your credentials");
    }
}
