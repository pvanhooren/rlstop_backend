package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findAllByPlatform(Platform platform);

    Optional<User> findByUserName(String userName);
}
