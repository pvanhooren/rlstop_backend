package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.repositories.TradeRepository;
import projects.rlstop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    public ArrayList<Trade> getAllTrades(){
        Iterable<Trade> itrades = tradeRepository.findAll();
        ArrayList<Trade> trades = new ArrayList<>();

        for(Trade trade : itrades){
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            if(user.isPresent()) {
                trade.setUser(user.get());
            }
            trades.add(trade);
        }

        return trades;
    }

    public ArrayList<Trade> getTradesByUser(int id){
        ArrayList<Trade> trades = new ArrayList<>();

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            Iterable<Trade> allTrades = tradeRepository.findAllByUserUserId(id);


            for (Trade trade : allTrades) {
                Optional<User> user2 = userRepository.findById(trade.getUser().getUserId());
                if (user2.isPresent()) {
                    trade.setUser(user2.get());
                }
                trades.add(trade);
            }
        }
        return trades;
    }

    public ArrayList<Trade> getTradesByPlatform(String platform){
        ArrayList<Trade> trades = new ArrayList<>();
        if (!platform.equals("")) {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<Trade> allTrades = tradeRepository.findAllByUserPlatform(platform);

                for (Trade trade : allTrades) {
                    Optional<User> user = userRepository.findById(trade.getUser().getUserId());
                    if (user.isPresent()) {
                        trade.setUser(user.get());
                    }
                    trades.add(trade);
                }
            }
        }

        return trades;
    }

    public Trade getTradeById(int Id){
        Trade trade;
        Optional<Trade> optTrade = tradeRepository.findById(Id);
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

    public boolean deleteTrade(int Id){
        Optional<Trade> trade = tradeRepository.findById(Id);
        if (trade.isPresent()) {
            tradeRepository.delete(trade.get());
            return true;
        }

        return false;
    }

    public Trade createTrade(String wants, String offers, int userId){
        Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    Trade trade = new Trade(wants, offers, user.get());
                    Trade result = tradeRepository.save(trade);
                    return result;
                }

                return null;
    }

    public Trade updateTrade(int Id, String wants, String offers, int userId){
        Optional<Trade> optTrade = tradeRepository.findById(Id);
        if (optTrade.isPresent()) {
            Trade trade = optTrade.get();
            if (wants != null && !wants.isEmpty()) {
                trade.setWants(wants);
            }
            if (offers != null && !offers.isEmpty()) {
                trade.setOffers(offers);
            }
            if (userId != 0) {
                Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    trade.setUser(user.get());
                }
                tradeRepository.save(trade);
                return trade;
            }
        }
        return null;
    }

}
