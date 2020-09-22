package projects.rlstop.Models;

import projects.rlstop.Models.Database.User;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_id")
    private int postId;

    @Column(name="wants")
    private String wants;

    @Column(name="offers")
    private String offers;

    @Column(name="user_id")
    private int userId;

    @Transient
    private User user;

    public Post(String wants, String offers, User user){
        this.wants = wants;
        this.offers = offers;
        this.userId = user.getUserId();
        this.user = user;
    }

    public Post() {

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.getPostId();
    }

    @Override
    public String toString() {
        return "Post (" + postId + ") {" + "\n" +
                user.getUserName() + "\n" +
                "Wants: " + wants + "\n" +
                "Offers: " + offers + "\n" +
                user.getPlatform().toString() + " ID: " + user.getPlatformID() + "\n" +
                "}" + "\n" ;
    }
}

