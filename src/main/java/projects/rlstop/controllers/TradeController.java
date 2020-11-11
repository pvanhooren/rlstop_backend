package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.models.database.Trade;
import projects.rlstop.services.TradeService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/trades")
public class TradeController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private TradeService tradeService;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Object> getAllTrades() {
        List<Trade> trades = tradeService.getAllTrades();

        if(trades.isEmpty()){
            return new ResponseEntity<>("There are currently no complete trades in the database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public @ResponseBody ResponseEntity<List<Trade>> getTradesByUser(@RequestParam int id){
        List<Trade> trades = tradeService.getTradesByUser(id);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/filter")
    public @ResponseBody ResponseEntity<List<Trade>> getTradesByPlatform(@RequestParam String platform) {
        List<Trade> trades = tradeService.getTradesByPlatform(platform);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Object> getTradePath(@PathVariable int id) {
        Trade trade = tradeService.getTradeById(id);

            if(trade !=null){
                return new ResponseEntity<>(trade, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Please provide a valid trade ID.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteTrade(@PathVariable int id) {
        boolean successful = tradeService.deleteTrade(id);

        if(successful){
            return new ResponseEntity<>("Trade has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Trade doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Object> createTrade(@RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
            if (wants != null && !wants.isEmpty() && offers != null && !offers.isEmpty() && userId != 0) {
                Trade trade = new Trade(wants, offers, null);
                Trade result = tradeService.saveTrade(trade, userId);

                if(result!=null) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else{
                    return new ResponseEntity<>("The trade can not be added because the linked user doesn't exist", HttpStatus.CONFLICT);
                }
            }

        return new ResponseEntity<>("The trade can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Object> updateTrade(@PathVariable int id, @RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
        Trade trade = tradeService.getTradeById(id);

        if(trade!=null) {
            if (wants != null && !wants.isEmpty()) {
                trade.setWants(wants);
            }
            if (offers != null && !offers.isEmpty()) {
                trade.setOffers(offers);
            }
        }

        Trade result = tradeService.saveTrade(trade, userId);

        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>("The trade was not updated because it was not complete.", HttpStatus.NOT_MODIFIED);
    }

}

