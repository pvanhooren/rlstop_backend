package projects.rlstop.Repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.Models.Database.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    public Iterable<User> findAllByPlatform(String Platform);
}
