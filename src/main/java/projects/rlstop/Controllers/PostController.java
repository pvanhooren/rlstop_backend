package projects.rlstop.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projects.rlstop.Models.Database.User;
import projects.rlstop.Models.Post;
import projects.rlstop.Repositories.PostRepository;
import projects.rlstop.Repositories.UserRepository;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/trades")
public class PostController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody ArrayList<Post> getAllTrades() {
        Iterable<Post> iposts = postRepository.findAll();
        ArrayList<Post> posts = new ArrayList<Post>();

        for(Post trade : iposts){
            Optional<User> user = userRepository.findById(trade.getUserId());
            if(user.isPresent()) {
                trade.setUser(user.get());
            }
            posts.add(trade);
        }
        return posts;
    }

    @GetMapping(path = "/filter")
    public @ResponseBody
    ArrayList<Post> getTradesByPlatform(@RequestParam String platform) {
        ArrayList<Post> trades = new ArrayList<Post>();
        if (platform != "") {
            if (platform.equals("NintendoSwitch") || platform.equals("PlayStation") || platform.equals("XBox") || platform.equals("PC")) {
                Iterable<Post> allTrades = postRepository.findAll();

                for (Post post : allTrades) {
                    Optional<User> optUser = userRepository.findById(post.getUserId());
                    if (optUser.isPresent()) {
                        if (optUser.get().getPlatform() == platform) {
                            trades.add(post);
                        }
                    }
                }
            }
        }
        return trades;
    }

    @GetMapping(path = "/{Id}")
    public @ResponseBody
    Post getTradePath(@PathVariable int Id) {
        //User user = fakeDatabase.getUser(Id);
        Optional<Post> optTrade = postRepository.findById(Id);
        if (optTrade.isPresent()) {
            Post trade = optTrade.get();
            Optional<User> user = userRepository.findById(trade.getUserId());
            if(user.isPresent()) {
                trade.setUser(user.get());
            }
            return trade;
        } else {
            //return "Please provide a valid user ID"
            return null;
        }
    }

    @DeleteMapping(path = "/{Id}")
    public @ResponseBody
    String deleteTrade(@PathVariable int Id) {
        Optional<Post> trade = postRepository.findById(Id);
        if (trade.isPresent()) {
            postRepository.delete(trade.get());
            return "Trade has successfully been deleted.";
        } else {
            return "Trade doesn't exist. Might have already been deleted.";
        }
    }

    @PostMapping(path = "/new")
    public @ResponseBody
    Post createTrade(@RequestParam String wants, @RequestParam String offers, @RequestParam int userId) {
        if (wants != "" && offers != "" && userId != 0) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                Post trade = new Post(wants, offers, user.get());
                Post result = postRepository.save(trade);
                return result;
            }
        }
        return null;
    }

    @PutMapping(path = "/{Id}")
    public @ResponseBody
    String updateTrade(@PathVariable int Id, @RequestParam String wants, @RequestParam String offers, @RequestParam int userId) {
        // Idempotent method. Always update (even if the resource has already been updated before).
        Optional<Post> optTrade = postRepository.findById(Id);
        if (optTrade.isPresent()) {
            Post trade = optTrade.get();
            if (wants != "") {
                trade.setWants(wants);
            }
            if (offers != "") {
                trade.setOffers(offers);
            }
            if (userId != 0) {
                Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    trade.setUserId(userId);
                    trade.setUser(user.get());
                }
                postRepository.save(trade);
                return "Trade successfully updated.";
            }
        }
        return "Trade was not updated because it was not complete.";
    }
}

