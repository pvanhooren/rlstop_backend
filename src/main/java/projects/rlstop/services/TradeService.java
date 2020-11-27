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
        Iterable<Trade> itrades = tradeRepository.findAll();
        ArrayList<Trade> trades = new ArrayList<>();

        for(Trade trade : itrades){
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            user.ifPresent(trade::setUser);
            trades.add(trade);
        }

        if(trades.isEmpty()) {
            throw new NotFoundException("There are currently no complete trades in the database");
        }

        return trades;
    }

    public List<Trade> getTradesByUser(int id){
        ArrayList<Trade> trades = new ArrayList<>();

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            Iterable<Trade> allTrades = tradeRepository.findAllByUserUserId(id);

            for (Trade trade : allTrades) {
                Optional<User> user2 = userRepository.findById(trade.getUser().getUserId());
                user2.ifPresent(trade::setUser);
                trades.add(trade);
            }

            if(!trades.isEmpty()) {
                return trades;
            }
        }

        throw new NotFoundException("There are no trades posted by this user in the database.");
    }

    public List<Trade> getTradesByPlatform(Platform platform){
        ArrayList<Trade> trades = new ArrayList<>();

        if(platform!=null){
                Iterable<Trade> allTrades = tradeRepository.findAllByUserPlatform(platform);

                for (Trade trade : allTrades) {
                    Optional<User> user = userRepository.findById(trade.getUser().getUserId());
                    user.ifPresent(trade::setUser);
                    trades.add(trade);
                }

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
        Optional<Trade> trade = tradeRepository.findById(id);
        if (trade.isPresent()) {
            tradeRepository.delete(trade.get());
            return true;
        }

        throw new NotFoundException("Trade doesn't exist. Might have already been deleted.");
    }

    public Trade saveTrade(Trade trade, int userId){
        Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    trade.setLastModified(LocalDateTime.now());
                    trade.setUser(user.get());
                    tradeRepository.save(trade);
                    return trade;
                }

        throw new BadRequestException("The linked user can't be found.");
    }

}
