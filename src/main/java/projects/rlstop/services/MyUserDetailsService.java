package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projects.rlstop.models.MyUserDetails;
import projects.rlstop.models.database.User;
import projects.rlstop.repositories.UserRepository;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);

        if(user.isPresent()){
            var mapped = user.map(MyUserDetails::new);
            if(mapped.isPresent()){ return mapped.get(); }
        }
        throw new UsernameNotFoundException("Not found: " + userName);
    }
}