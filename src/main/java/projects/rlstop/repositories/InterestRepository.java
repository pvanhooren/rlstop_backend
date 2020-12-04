package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.Interest;

import java.util.Optional;

public interface InterestRepository extends CrudRepository<Interest, Integer> {
    Iterable<Interest> findAllByUserUserId(int userId);

    Iterable<Interest> findAllByTradeTradeId(int tradeId);

    Optional<Interest> findByUserUserIdAndTradeTradeId(int userId, int tradeId);
}
