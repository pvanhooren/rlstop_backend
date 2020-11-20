package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
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

    public List<User> getUsersByPlatform(Platform platform){
        ArrayList<User> users = new ArrayList<>();

                if(platform!=null){
                    Iterable<User> usersIterable = userRepository.findAllByPlatform(platform);
                for(User user : usersIterable){
                        users.add(user);
                }
            }

        return users;
    }

    public User getUserById(int id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByUserName(String userName){
        Optional<User> user = userRepository.findByUserName(userName);
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

    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    public User updateUser(User user){
        if(nameExist(user.getUserName())){
            if(userRepository.findByUserName(user.getUserName()).get().getUserId() != user.getUserId()){
                return null;
            }
        }

        if(emailExist(user.getEmailAddress())){
            if(userRepository.findByEmailAddress(user.getEmailAddress()).get().getUserId() != user.getUserId()){
                return null;
            }
        }

        return saveUser(user);
    }

    public User createUser(User user){
        if(nameExist(user.getUserName()) || emailExist(user.getEmailAddress())){
            return null;
        }
            return saveUser(user);
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

    private boolean emailExist(String email) {
        return userRepository.findByEmailAddress(email).isPresent();
    }

    private boolean nameExist(String name){
        return userRepository.findByUserName(name).isPresent();
    }
}
