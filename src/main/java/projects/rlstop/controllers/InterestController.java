package projects.rlstop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.models.database.Interest;
import projects.rlstop.services.InterestService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping("/interests")
public class InterestController {
    @Autowired
    InterestService interestService;

    @GetMapping()
    public @ResponseBody
    ResponseEntity<Interest> getSpecificInterest(@RequestParam int user, @RequestParam int trade) {
        Interest interest = interestService.getSpecificInterest(user, trade);
        return new ResponseEntity<>(interest, HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public @ResponseBody
    ResponseEntity<List<Interest>> getInterestsByUser(@RequestParam int id) {
        List<Interest> interests = interestService.getInterestsByUser(id);
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    @GetMapping(path = "/trade")
    public @ResponseBody
    ResponseEntity<List<Interest>> getInterestsByTrade(@RequestParam int id) {
        List<Interest> interests = interestService.getInterestsByTrade(id);
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    @PostMapping(path = "/new")
    public @ResponseBody
    ResponseEntity<Interest> createInterest(@RequestParam(required = false) int user, @RequestParam(required = false) int trade, @RequestParam(required = false) String comment) {
        Interest result = interestService.saveInterest(user, trade, comment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity<String> deleteInterest(@PathVariable int id) {
        interestService.deleteInterest(id);
        return new ResponseEntity<>("Interest has successfully been deleted.", HttpStatus.OK);
    }

    @DeleteMapping()
    public @ResponseBody
    ResponseEntity<String> deleteSpecificInterest(@RequestParam int user, @RequestParam int trade) {
        Interest interest = interestService.getSpecificInterest(user, trade);
        interestService.deleteInterest(interest.getInterestId());
        return new ResponseEntity<>("Interest has successfully been deleted.", HttpStatus.OK);
    }
}
