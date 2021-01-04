package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.InternalServerException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.helpers.JwtUtil;
import projects.rlstop.models.AuthRequest;
import projects.rlstop.models.AuthResponse;
import projects.rlstop.models.database.Role;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.models.enums.UserRole;
import projects.rlstop.repositories.UserRepository;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<User> getAllUsers() {
        Iterable<User> iusers = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for (User user : iusers) {
            users.add(user);
        }

        if (users.isEmpty()) {
            throw new NotFoundException("There are currently no users in the database.");
        }

        return users;
    }

    public List<User> getUsersByPlatform(Platform platform) {
        ArrayList<User> users = new ArrayList<>();

        if (platform != null) {
            Iterable<User> usersIterable = userRepository.findAllByPlatform(platform);

            for (User user : usersIterable) {
                users.add(user);
            }

            if (!users.isEmpty()) {
                return users;
            }
        }

        throw new NotFoundException("There are no users with this platform in the database.");
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User was not found. Please provide a valid user ID");
        }
    }

    public User getUserByUserName(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User was not found. Please provide a valid username.");
        }
    }

    public boolean deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
        return true;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(int id, String name, String email, Platform platform, String platformID) {
        User user = getUserById(id);

        if (email != null && !email.isEmpty() && name != null && !name.isEmpty() && platform != null && platformID != null && !platformID.isEmpty()) {
            user.setUserName(name);
            user.setEmailAddress(email);
            user.setPlatform(platform);
            user.setPlatformID(platformID);

            nameExist(user.getUserId(), user.getUserName());
            emailExist(user.getUserId(), user.getEmailAddress());

            return saveUser(user);
        }

        throw new BadRequestException("Not all fields have been filled in. Please fill in the missing fields.");
    }

    public AuthResponse createUser(String creds, String email, Platform platform, String platformID, String wishlist) {
        if(creds != null && !creds.isEmpty() && email != null && !email.isEmpty() && platform != null && platformID != null && !platformID.isEmpty() && wishlist != null && !wishlist.isEmpty()) {
            AuthRequest authRequest = checkCreds(creds);

            nameExist(0, authRequest.getUserName());
            emailExist(0, email);

            User user = new User(authRequest.getUserName(), email, authRequest.getPassword(), platform, platformID, wishlist);
            User result = saveUser(user);

            String token = jwtUtil.generateToken(authRequest.getUserName());
            return new AuthResponse(token, result.getUserName(), result.getUserId(), false);
        }

        throw new BadRequestException("Not all fields have been filled in. Please fill in the missing fields.");
    }

    public AuthResponse authenticate(String creds){
        AuthRequest authRequest = checkCreds(creds);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new BadRequestException("Invalid username/password");
        }

        String token = jwtUtil.generateToken(authRequest.getUserName());
        User user = getUserByUserName(authRequest.getUserName());
        boolean isAdmin = false;

        for(Role role : user.getRoles()){
            if(role.getRoleName() == UserRole.ROLE_ADMIN){
                isAdmin = true;
            }
        }

        return new AuthResponse(token, user.getUserName(), user.getUserId(), isAdmin);
    }

    public User addToWishlist(int id, String item) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.addToWishlist(item);
            userRepository.save(user);
            return user;
        }
        throw new InternalServerException("The item was not added, an error occurred.");
    }

    public User removeFromWishlist(int id, String item) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.removeFromWishlist(item);
            userRepository.save(user);
            return user;
        }
        throw new InternalServerException("The item was not removed, an error occured.");
    }

    public User clearWishlist(int id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.clearWishlist();
            userRepository.save(user);
            return user;
        }
        throw new InternalServerException("The wishlist was not cleared, an error occured.");
    }

    private boolean emailExist(int id, String email) {
        Optional<User> user = userRepository.findByEmailAddress(email);
        if (user.isPresent() && id != user.get().getUserId()) {
            throw new BadRequestException("There is already a user with that email address.");
        }

        return false;
    }

    private boolean nameExist(int id, String name) {
        Optional<User> user = userRepository.findByUserName(name);
        if (user.isPresent() && id != user.get().getUserId()) {
            throw new BadRequestException("There is already a user with that username.");
        }

        return false;
    }

    public AuthRequest checkCreds(String creds){
        if(!creds.isEmpty()) {
            String credentials = new String(Base64.getDecoder().decode(creds.getBytes()));
            final StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
            final String name;
            final String password;

            try {
                name = tokenizer.nextToken();
                password = tokenizer.nextToken();
            } catch (NoSuchElementException n) {
                throw new BadRequestException("Something went wrong.");
            }

            return new AuthRequest(name, password);
        }

        throw new BadRequestException("Please provide your credentials");
    }
}
