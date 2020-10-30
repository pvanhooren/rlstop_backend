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
import java.util.ArrayList;

@CrossOrigin
@Controller
@RequestMapping("/trades")
public class TradeController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private TradeService tradeService;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<ArrayList<Trade>> getAllTrades() {
        ArrayList<Trade> trades = tradeService.getAllTrades();

        if(trades.isEmpty()){
            return new ResponseEntity("There are currently no complete trades in the database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(trades, HttpStatus.FOUND);
    }

    @GetMapping(path = "/user")
    public @ResponseBody ResponseEntity<ArrayList<Trade>> getTradesByUser(@RequestParam int id){
        ArrayList<Trade> trades = tradeService.getTradesByUser(id);

        return new ResponseEntity<>(trades, HttpStatus.FOUND);
    }

    @GetMapping(path = "/filter")
    public @ResponseBody ResponseEntity<ArrayList<Trade>> getTradesByPlatform(@RequestParam String platform) {
        ArrayList<Trade> trades = tradeService.getTradesByPlatform(platform);

        return new ResponseEntity<>(trades, HttpStatus.FOUND);
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<Trade> getTradePath(@PathVariable int Id) {
        Trade trade = tradeService.getTradeById(Id);

            if(trade !=null){
                return new ResponseEntity<>(trade, HttpStatus.FOUND);
        } else {
            return new ResponseEntity("Please provide a valid trade ID.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity deleteTrade(@PathVariable int Id) {
        boolean successful = tradeService.deleteTrade(Id);

        if(successful){
            return new ResponseEntity("Trade has successfully been deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity("Trade doesn't exist. Might have already been deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Trade> createTrade(@RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
            if (wants != null && !wants.isEmpty() && offers != null && !offers.isEmpty() && userId != 0) {
                Trade result = tradeService.createTrade(wants, offers, userId);

                if(result!=null) {
                    return new ResponseEntity<>(result, HttpStatus.CREATED);
                } else{
                    return new ResponseEntity("The trade can not be added because the linked user doesn't exist", HttpStatus.CONFLICT);
                }
            }

        return new ResponseEntity("The trade can not be added because it is not complete", HttpStatus.CONFLICT);
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody ResponseEntity<Trade> updateTrade(@PathVariable int Id, @RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
        Trade trade = tradeService.updateTrade(Id, wants, offers, userId);

        if(trade != null) {
            return new ResponseEntity<>(trade, HttpStatus.OK);
        }

        return new ResponseEntity("The trade was not updated because it was not complete.", HttpStatus.NOT_MODIFIED);
    }

}

