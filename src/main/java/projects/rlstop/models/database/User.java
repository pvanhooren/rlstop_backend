package projects.rlstop.models.database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projects.rlstop.helpers.StringListConverter;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.models.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="email_address")
    private String emailAddress;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="platform")
    @Enumerated(EnumType.ORDINAL)
    private Platform platform;

    @Column(name="platformid")
    private String platformID;

    @Column(name="active")
    private boolean active;

    @Convert(converter = StringListConverter.class)
    @Column(name="wishlist")
    private List<String> wishlist = new ArrayList<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_roles",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "roleId") })
    private Collection<Role> roles = new ArrayList<>();

    public User(String userName, String emailAddress, String password, Platform platform, String platformID, String wishlist){
        this.userName = userName;
        this.emailAddress = emailAddress;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.passwordHash = encoder.encode(password);
        this.platform = platform;
        this.platformID = platformID;
        this.active = true;
        roles.add(new Role(UserRole.ROLE_USER));

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.passwordHash = encoder.encode(password);
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public boolean getActive(){ return this.active;}

    public void setActive(boolean active) { this.active = active; }

    public List<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<String> wishlist) {
        this.wishlist = wishlist;
    }

    public void addToWishlist(String item) {
        if(this.wishlist.get(0).equals("")){
            this.clearWishlist();
        }

        if(item.contains(",")) {
            String[] elements = item.split(",");
            for(String s : elements){
                this.wishlist.add(s);
            }
        } else {
            this.wishlist.add(item);
        }
    }

    public void clearWishlist() { this.wishlist.clear(); }

    public void removeFromWishlist(String item) { this.wishlist.remove(item); }

    public boolean isActive() {
        return active;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

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
