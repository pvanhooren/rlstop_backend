package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.models.database.User;
import projects.rlstop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        int size = getIterableSize(users);

        if (size == 0) {
            return null;
        }

        return users;
    }

    public ArrayList<User> getUsersByPlatform(String platform){
        ArrayList<User> users = new ArrayList<>();

        if (!platform.equals("")) {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<User> usersIterable = userRepository.findAllByPlatform(platform);
                for(User user : usersIterable){
                    if(user !=null) {
                        users.add(user);
                    }
                }
            }
        }

        return users;
    }

    public User getUserById(int Id){
        Optional<User> user = userRepository.findById(Id);
        if (user.isPresent()){
            return user.get();
        }

        return null;
    }

    public boolean deleteUser(int Id){
        Optional<User> user = userRepository.findById(Id);
        if(user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }

        return false;
    }

    public User createUser(String name, String email, String password, String platform, String platformID, String wishlist){
        User user = new User(name, email, password, platform, platformID, wishlist);
        User result = userRepository.save(user);
        return result;
    }

    public User updateUser(int Id, String name, String email, String platform, String platformID){
        Optional<User> optUser = userRepository.findById(Id);
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
            User updatedUser = userRepository.save(user);
            return updatedUser;
        }

        return null;
    }

    public User changePassword(int Id, String oldPassword, String newPassword){
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()) {
            User user = optUser.get();

            if (Objects.hash(oldPassword) == (user.getPasswordHash())) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    user.setPasswordHash(Objects.hash(newPassword));
                }
                User updatedUser = userRepository.save(user);
                return user;
            }
        }
        return null;
    }

    public User addToWishlist(int Id, String item){
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.addToWishlist(item);
            User updatedUser = userRepository.save(user);
            return updatedUser;
        }
        return null;
    }

    public User removeFromWishlist(int Id, String item){
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.removeFromWishlist(item);
            User updatedUser = userRepository.save(user);
            return user;
        }
        return null;
    }

    public User clearWishlist(int Id){
        Optional<User> optUser = userRepository.findById(Id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.clearWishlist();
            User updatedUser = userRepository.save(user);
            return user;
        }
        return null;
    }

    public int getIterableSize(Iterable<User> users){
        int size = 0;

        for(User user : users){
            size++;
        }

        return size;
    }
}
