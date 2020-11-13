package projects.rlstop.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.enums.Platform;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    Iterable<Trade> findAllByUserPlatform(Platform platform);

    Iterable<Trade> findAllByUserUserId(int userId);
}
