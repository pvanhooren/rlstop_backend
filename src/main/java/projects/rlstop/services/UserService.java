package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.InternalServerException;
import projects.rlstop.exceptions.NotFoundException;
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

    public User updateUser(User user) {
        nameExist(user.getUserId(), user.getUserName());
        emailExist(user.getUserId(), user.getEmailAddress());

        return saveUser(user);
    }

    public User createUser(User user) {
        nameExist(0, user.getUserName());
        emailExist(0, user.getEmailAddress());
        return saveUser(user);
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
}
