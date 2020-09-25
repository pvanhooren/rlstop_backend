package projects.rlstop.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import projects.rlstop.Models.Database.Trade;
import projects.rlstop.Models.Database.User;
import projects.rlstop.Repositories.TradeRepository;
import projects.rlstop.Repositories.UserRepository;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/trades")
public class TradeController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<ArrayList<Trade>> getAllTrades() {
        Iterable<Trade> itrades = tradeRepository.findAll();
        ArrayList<Trade> trades = new ArrayList<Trade>();

        for(Trade trade : itrades){
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            if(user.isPresent()) {
                trade.setUser(user.get());
            }
            trades.add(trade);
        }

        if(trades.size() == 0){
            return new ResponseEntity("There are currently no complete trades in the database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ArrayList<Trade>>(trades, HttpStatus.FOUND);
    }

    @GetMapping(path = "/filter")
    public @ResponseBody ResponseEntity<ArrayList<Trade>> getTradesByPlatform(@RequestParam String platform) {
        ArrayList<Trade> trades = new ArrayList<Trade>();
        if (platform != "") {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<Trade> allTrades = tradeRepository.findAllByUserPlatform(platform);

                for (Trade trade : allTrades) {
                    Optional<User> user = userRepository.findById(trade.getUser().getUserId());
                        if(user.isPresent()) {
                            trade.setUser(user.get());
                        }
                    trades.add(trade);
                }
            } else {
                return new ResponseEntity("Please provide a valid platform to filter on", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity("Please provide a valid platform to filter on", HttpStatus.NOT_FOUND);
        }

        if(trades.size() == 0){
            return new ResponseEntity("There are currently no trades on the given platform in the database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ArrayList<Trade>>(trades, HttpStatus.FOUND);
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<Trade> getTradePath(@PathVariable int Id) {
        Optional<Trade> optTrade = tradeRepository.findById(Id);
        if (optTrade.isPresent()) {
            Trade trade = optTrade.get();
            Optional<User> user = userRepository.findById(trade.getUser().getUserId());
            if(user.isPresent()) {
                trade.setUser(user.get());
            }
            return new ResponseEntity<Trade>(trade, HttpStatus.FOUND);
        } else {
            return new ResponseEntity("Please provide a valid user ID.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity deleteTrade(@PathVariable int Id) {
        Optional<Trade> trade = tradeRepository.findById(Id);
        if (trade.isPresent()) {
            tradeRepository.delete(trade.get());
            return new ResponseEntity("Trade has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity("Trade doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Trade> createTrade(@RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
            if (wants != null && offers != null && userId != 0) {
                Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    Trade trade = new Trade(wants, offers, user.get());
                    Trade result = tradeRepository.save(trade);
                    return new ResponseEntity<Trade>(result, HttpStatus.CREATED);
                }
            }

        return new ResponseEntity("The trade can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<Trade> updateTrade(@PathVariable int Id, @RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
            Optional<Trade> optTrade = tradeRepository.findById(Id);
            if (optTrade.isPresent()) {
                Trade trade = optTrade.get();
                if (wants != null) {
                    trade.setWants(wants);
                }
                if (offers != null) {
                    trade.setOffers(offers);
                }
                if (userId != 0) {
                    Optional<User> user = userRepository.findById(userId);
                    if (user.isPresent()) {
                        trade.setUser(user.get());
                    }
                    tradeRepository.save(trade);
                    return new ResponseEntity<Trade>(trade, HttpStatus.OK);
                }
            }
        return new ResponseEntity("The trade was not updated because it was not complete.", HttpStatus.NOT_MODIFIED);
    }

}

