package projects.rlstop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.Interest;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.database.User;
import projects.rlstop.repositories.InterestRepository;
import projects.rlstop.repositories.TradeRepository;
import projects.rlstop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterestService {
    @Autowired
    InterestRepository interestRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    UserRepository userRepository;

    public List<Interest> getInterestsByUser(int userId) {
        ArrayList<Interest> interests = new ArrayList<>();

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Iterable<Interest> allInterests = interestRepository.findAllByUserUserId(userId);

            for (Interest interest : allInterests) {
                Optional<User> user2 = userRepository.findById(interest.getUser().getUserId());
                user2.ifPresent(interest::setUser);
                interests.add(interest);
            }

            if (!interests.isEmpty()) {
                return interests;
            }

            throw new NotFoundException("There are no trades that this user is interested in the database.");
        }

        throw new NotFoundException("User was not found. Please provide a valid user ID");
    }

    public List<Interest> getInterestsByTrade(int tradeId) {
        ArrayList<Interest> interests = new ArrayList<>();

        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isPresent()) {
            Iterable<Interest> allInterests = interestRepository.findAllByTradeTradeId(tradeId);

            for (Interest interest : allInterests) {
                Optional<Trade> trade2 = tradeRepository.findById(interest.getUser().getUserId());
                trade2.ifPresent(interest::setTrade);
                interests.add(interest);
            }

            if (!interests.isEmpty()) {
                return interests;
            }

            throw new NotFoundException("There are no users interested in this trade in the database.");
        }

        throw new NotFoundException("Trade was not found. Please provide a valid trade ID.");
    }

    public Interest getSpecificInterest(int userId, int tradeId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {

            Optional<Trade> trade = tradeRepository.findById(tradeId);
            if (trade.isPresent()) {
                Optional<Interest> existingInterest =  interestRepository.findByUserUserIdAndTradeTradeId(userId, tradeId);

                if(existingInterest.isPresent()){
                    return existingInterest.get();
                }

                throw new NotFoundException("The linked user is not yet interested in the linked trade.");
            }

            throw new NotFoundException("The linked trade can't be found.");
        }

        throw new NotFoundException("The linked user can't be found.");
    }

    public Interest saveInterest(Interest interest, int userId, int tradeId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            interest.setUser(user.get());

            Optional<Trade> trade = tradeRepository.findById(tradeId);
            if (trade.isPresent()) {

                Optional<Interest> existingInterest =  interestRepository.findByUserUserIdAndTradeTradeId(userId, tradeId);

                if(existingInterest.isPresent()){
                    throw new BadRequestException("This user is already listed as interested on this trade.");
                }

                interest.setTrade(trade.get());
                interestRepository.save(interest);
                return interest;
            }

            throw new BadRequestException("The linked trade can't be found.");
        }

        throw new BadRequestException("The linked user can't be found.");
    }

    public boolean deleteInterest(int id) {
        Optional<Interest> interest = interestRepository.findById(id);

        if (interest.isPresent()) {
            interestRepository.delete(interest.get());
            return true;
        }

        throw new NotFoundException("Interest doesn't exist. Might have already been deleted.");
    }
}
