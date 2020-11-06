package projects.rlstop.models.database;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return tradeId == trade.getTradeId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Post (" + tradeId + ") {" + "\n" +
                user.getUserName() + "\n" +
                "Wants: " + wants + "\n" +
                "Offers: " + offers + "\n" +
                user.getPlatform() + " ID: " + user.getPlatformID() + "\n" +
                "}" + "\n" ;
    }
}

