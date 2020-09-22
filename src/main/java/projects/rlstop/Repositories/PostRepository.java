package projects.rlstop.Repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.Models.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
