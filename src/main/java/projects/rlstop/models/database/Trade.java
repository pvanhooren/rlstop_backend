package projects.rlstop.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trade_id")
    private int tradeId;

    @Column(name="wants")
    private String wants;

    @Column(name="offers")
    private String offers;

    @Column(name="date_last_modified")
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy="trade")
    private List<Interest> interests = new ArrayList<>();

    public Trade(String wants, String offers, User user){
        this.wants = wants;
        this.offers = offers;
        this.user = user;
    }

    public Trade() {

    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int postId) {
        this.tradeId = postId;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getWants() {
        return wants;
    }

    public void setWants(String wants) {
        this.wants = wants;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}

