package projects.rlstop.Models.Database;



import projects.rlstop.Helpers.StringListConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private int userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="email_address")
    private String emailAddress;

    @Column(name="password_hash")
    private int passwordHash;

    @Column(name="platform")
    private String platform;

    @Column(name="platformid")
    private String platformID;

    @Convert(converter = StringListConverter.class)
    @Column(name="wishlist")
    private ArrayList<String> wishlist = new ArrayList<String>();


    public User(String userName, String emailAddress, String password, String platform, String platformID, String newWishlistItem){
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.passwordHash = Objects.hash(password);
        this.platform = platform;
        this.platformID = platformID;
        this.wishlist.add(newWishlistItem);
    }

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    public void addToWishlist(String item) { this.wishlist.add(item); }

    public void clearWishlist() { this.wishlist.clear(); }

    public void removeFromWishlist(String item) { this.wishlist.remove(item); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.getUserId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "User (" + userId + ") {" + "\n" +
                "Email Address = " + emailAddress + "\n" +
                "UserName = " + userName + "\n" +
                platform + " ID = " + platformID + "\n" +
                "Wishlist :" + wishlist + "\n" +
                "}" + "\n";
    }
}
