package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.Trade;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    Iterable<Trade> findAllByUserPlatform(String platform);

    Iterable<Trade> findAllByUserUserId(int userId);
}
