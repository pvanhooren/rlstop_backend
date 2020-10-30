package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findAllByPlatform(String platform);
}
