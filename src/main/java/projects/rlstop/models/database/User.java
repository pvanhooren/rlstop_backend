package projects.rlstop.models.database;



import projects.rlstop.helpers.StringListConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<String> wishlist = new ArrayList<>();


    public User(String userName, String emailAddress, String password, String platform, String platformID, String wishlist){
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.passwordHash = Objects.hash(password);
        this.platform = platform;
        this.platformID = platformID;

        if(wishlist.contains(",")) {
            String[] elements = wishlist.split(",");
            List<String> fixedLenghtList = Arrays.asList(elements);
            this.wishlist = new ArrayList<>(fixedLenghtList);
        } else {
            this.wishlist.add(wishlist);
        }
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

    public List<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<String> wishlist) {
        this.wishlist = wishlist;
    }

    public void addToWishlist(String item) {
        if(item.contains(",")) {
            String[] elements = item.split(",");
            List<String> fixedLenghtList = Arrays.asList(elements);
            this.wishlist = new ArrayList<>(fixedLenghtList);
        } else {
            this.wishlist.add(item);
        }
    }

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
