package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.models.database.User;
import projects.rlstop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        Iterable<User> iusers = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for(User user : iusers){
            users.add(user);
        }

        return users;
    }

    public List<User> getUsersByPlatform(String platform){
        ArrayList<User> users = new ArrayList<>();

            if (!platform.equals("") && platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<User> usersIterable = userRepository.findAllByPlatform(platform);
                for(User user : usersIterable){
                    if(user !=null) {
                        users.add(user);
                    }
                }
            }

        return users;
    }

    public User getUserById(int id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public boolean deleteUser(int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }

        return false;
    }

    public User createUser(String name, String email, String password, String platform, String platformID, String wishlist){
        User user = new User(name, email, password, platform, platformID, wishlist);
        userRepository.save(user);
        return user;
    }

    public User updateUser(int id, String name, String email, String platform, String platformID){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            if (name != null && !name.isEmpty()) {
                user.setUserName(name);
            }
            if (email != null && !email.isEmpty()) {
                user.setEmailAddress(email);
            }
            if (platform != null && !platform.isEmpty()) {
                user.setPlatform(platform);
            }
            if (platformID != null && !platformID.isEmpty()) {
                user.setUserName(platformID);
            }
            userRepository.save(user);
            return user;
        }

        return null;
    }

    public User changePassword(int id, String oldPassword, String newPassword){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();

            if (Objects.hash(oldPassword) == (user.getPasswordHash())) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    user.setPasswordHash(Objects.hash(newPassword));
                }
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }

    public User addToWishlist(int id, String item){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.addToWishlist(item);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User removeFromWishlist(int id, String item){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.removeFromWishlist(item);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User clearWishlist(int id){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.clearWishlist();
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
