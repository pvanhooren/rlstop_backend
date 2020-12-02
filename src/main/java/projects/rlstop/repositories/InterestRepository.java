package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.Interest;

public interface InterestRepository extends CrudRepository<Interest, Integer> {
    Iterable<Interest> findAllByUserUserId(int userId);

    Iterable<Interest> findAllByTradeTradeId(int tradeId);
}
