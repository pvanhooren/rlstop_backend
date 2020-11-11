package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
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
        }
        return trades;
    }

    public List<Trade> getTradesByPlatform(String platform){
        ArrayList<Trade> trades = new ArrayList<>();
            if (!platform.equals("") && platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<Trade> allTrades = tradeRepository.findAllByUserPlatform(platform);

                for (Trade trade : allTrades) {
                    Optional<User> user = userRepository.findById(trade.getUser().getUserId());
                    user.ifPresent(trade::setUser);
                    trades.add(trade);
                }
            }

        return trades;
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
                return null;
            }
        } else {
            return null;
        }

        return trade;
    }

    public boolean deleteTrade(int id){
        Optional<Trade> trade = tradeRepository.findById(id);
        if (trade.isPresent()) {
            tradeRepository.delete(trade.get());
            return true;
        }

        return false;
    }

    public Trade saveTrade(Trade trade, int userId){
        Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    trade.setLastModified(LocalDateTime.now());
                    trade.setUser(user.get());
                    tradeRepository.save(trade);
                    return trade;
                }

                return null;
    }

}
