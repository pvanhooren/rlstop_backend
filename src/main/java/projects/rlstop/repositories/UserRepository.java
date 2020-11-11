package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findAllByPlatform(String platform);

    Optional<User> findByUserName(String userName);
}
