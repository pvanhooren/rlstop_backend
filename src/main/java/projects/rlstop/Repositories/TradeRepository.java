package projects.rlstop.Repositories;

import org.springframework.data.repository.CrudRepository;
import projects.rlstop.Models.Database.Trade;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    Iterable<Trade> findAllByUserPlatform(String platform);
}
