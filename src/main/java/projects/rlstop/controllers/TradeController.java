package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.models.database.Trade;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.services.TradeService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/trades")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<List<Trade>> getAllTrades() {
        List<Trade> trades = tradeService.getAllTrades();
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
        return new ResponseEntity<>(trade, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteTrade(@PathVariable int id) {
        tradeService.deleteTrade(id);
        return new ResponseEntity<>("Trade has successfully been deleted.", HttpStatus.OK);
    }

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<Trade> createTrade(@RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
        Trade result = tradeService.createTrade(wants, offers, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Trade> updateTrade(@PathVariable int id, @RequestParam(required= false) String wants, @RequestParam(required= false) String offers, @RequestParam int userId) {
        Trade result = tradeService.updateTrade(id, wants, offers, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

