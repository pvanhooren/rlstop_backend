package projects.rlstop.Models.Database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name="post_id")
    private int postId;

    @Column(name="wants")
    private String wants;

    @Column(name="offers")
    private String offers;

//    @Column(name="user_id")
//    @NotNull
//    private int userId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Trade(String wants, String offers, User user){
        this.wants = wants;
        this.offers = offers;
//        this.userId = user.getUserId();
        this.user = user;
    }

    public Trade() {

    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

//    public int getUserId() {
//        return user.getUserId();
//    }

//    public void setUserId(int userId) {
//        this.user = userId;
//    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return postId == trade.getPostId();
    }

    @Override
    public String toString() {
        return "Post (" + postId + ") {" + "\n" +
                user.getUserName() + "\n" +
                "Wants: " + wants + "\n" +
                "Offers: " + offers + "\n" +
                user.getPlatform() + " ID: " + user.getPlatformID() + "\n" +
                "}" + "\n" ;
    }
}

