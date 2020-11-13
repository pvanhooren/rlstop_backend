package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.exceptions.BadRequestException;
import projects.rlstop.exceptions.NotFoundException;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.services.TradeService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/trades")
public class TradeController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private TradeService tradeService;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<List<Trade>> getAllTrades() {
        List<Trade> trades = tradeService.getAllTrades();

        if(trades.isEmpty()){
            throw new NotFoundException("There are currently no complete trades in the database");
        }
        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public @ResponseBody ResponseEntity<List<Trade>> getTradesByUser(@RequestParam int id){
        List<Trade> trades = tradeService.getTradesByUser(id);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/filter")
    public @ResponseBody ResponseEntity<List<Trade>> getTradesByPlatform(@RequestParam Platform platform) {
        List<Trade> trades = tradeService.getTradesByPlatform(platform);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Trade> getTradePath(@PathVariable int id) {
        Trade trade = tradeService.getTradeById(id);

            if(trade !=null){
                return new ResponseEntity<>(trade, HttpStatus.OK);
        } else {
            throw new NotFoundException("Trade was not found. Please provide a valid trade ID.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteTrade(@PathVariable int id) {
        boolean successful = tradeService.deleteTrade(id);

        if(successful){
            return new ResponseEntity<>("Trade has successfully been deleted.", HttpStatus.OK);
        } else {
            throw new NotFoundException("Trade doesn't exist. Might have already been deleted.");
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Trade> createTrade(@RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
            if (wants != null && !wants.isEmpty() && offers != null && !offers.isEmpty() && userId != 0) {
                Trade trade = new Trade(wants, offers, null);
                Trade result = tradeService.saveTrade(trade, userId);

                if(result!=null) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else{
                    throw new BadRequestException("The trade can not be added because the linked user doesn't exist");
                }
            }

        throw new BadRequestException("The trade can not be added because it is not complete");
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Trade> updateTrade(@PathVariable int id, @RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
        Trade trade = tradeService.getTradeById(id);

        if(trade!=null) {
            if (wants != null && !wants.isEmpty()) {
                trade.setWants(wants);
            }
            if (offers != null && !offers.isEmpty()) {
                trade.setOffers(offers);
            }
        } else {
            throw new NotFoundException("The trade was not updated because it does not exist.");
        }

        Trade result = tradeService.saveTrade(trade, userId);

        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        throw new BadRequestException("The trade was not updated because it was not complete.");
    }

}

