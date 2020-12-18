package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.repositories.TradeRepository;
import projects.rlstop.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Trade> getAllTrades(){
        Iterable<Trade> itrades = tradeRepository.findAllByOrderByLastModifiedDesc();
        List<Trade> trades = checkIterable(itrades);

        if(trades.isEmpty()) {
            throw new NotFoundException("There are currently no complete trades in the database");
        }

        return trades;
    }

    public List<Trade> getTradesByUser(int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            Iterable<Trade> allTrades = tradeRepository.findAllByUserUserIdOrderByLastModifiedDesc(id);
            List<Trade> trades = checkIterable(allTrades);

            if(!trades.isEmpty()) {
                return trades;
            }
        }

        throw new NotFoundException("There are no trades posted by this user in the database.");
    }

    public List<Trade> getTradesByPlatform(Platform platform){
        if(platform!=null){
            Iterable<Trade> allTrades = tradeRepository.findAllByUserPlatformOrderByLastModifiedDesc(platform);
            List<Trade> trades = checkIterable(allTrades);

            if(!trades.isEmpty()) {
                return trades;
            }
        }

        throw new NotFoundException("There are no trades with this platform in the database.");
    }

    public Trade getTradeById(int id){
        Trade trade;
        Optional<Trade> optTrade = tradeRepository.findById(id);
        if (optTrade.isPresent()) {
            trade = optTrade.get();
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            if (user.isPresent()) {
                trade.setUser(user.get());
            } else {
                throw new NotFoundException("User linked to the trade was not found. Please provide a valid trade.");
            }
        } else {
            throw new NotFoundException("Trade was not found. Please provide a valid trade ID.");
        }

        return trade;
    }

    public boolean deleteTrade(int id){
        Trade trade = getTradeById(id);
        tradeRepository.delete(trade);
        return true;
    }

    public Trade saveTrade(Trade trade, int userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            trade.setUser(user.get());
        } else {
            throw new BadRequestException("The linked user can't be found.");
        }

        trade.setLastModified(LocalDateTime.now());
        return tradeRepository.save(trade);
    }

    public Trade createTrade(String wants, String offers, int userId){
        if (wants != null && !wants.isEmpty() && offers != null && !offers.isEmpty() && userId != 0) {
            Trade trade = new Trade(wants, offers, null);

            return saveTrade(trade, userId);
        }

        throw new BadRequestException("The trade can not be added because it is not complete");
    }

    public Trade updateTrade(int id, String wants, String offers, int userId){
        if (id != 0) {
            Trade trade = getTradeById(id);

            if (wants != null && !wants.isEmpty()) {
                trade.setWants(wants);
            }

            if (offers != null && !offers.isEmpty()) {
                trade.setOffers(offers);
            }

            return saveTrade(trade, userId);
        }

        throw new BadRequestException("The trade can not be updated because it is not complete");
    }

    public List<Trade> checkIterable(Iterable<Trade> allTrades){
        List<Trade> trades = new ArrayList<>();
        for (Trade trade : allTrades) {
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            user.ifPresent(trade::setUser);
            trades.add(trade);
        }

        return trades;
    }

}
