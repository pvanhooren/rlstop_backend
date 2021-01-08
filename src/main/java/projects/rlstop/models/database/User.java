package projects.rlstop.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projects.rlstop.helpers.StringListConverter;
import projects.rlstop.models.enums.Platform;
import projects.rlstop.models.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    @JsonIgnore
    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="platform")
    @Enumerated(EnumType.ORDINAL)
    private Platform platform;

    @Column(name="platformid")
    private String platformID;

    @Column(name="active")
    private boolean active;

    @Column(name="admin")
    private boolean admin;

    @Convert(converter = StringListConverter.class)
    @Column(name="wishlist")
    private List<String> wishlist = new ArrayList<>();

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy="user")
    private List<Interest> interests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy="user")
    private List<Trade> trades = new ArrayList<>();

    public User(String userName, String emailAddress, String password, Platform platform, String platformID, String wishlist){
        this.userName = userName;
        this.emailAddress = emailAddress;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.passwordHash = encoder.encode(password);
        this.platform = platform;
        this.platformID = platformID;
        this.active = true;
        this.admin = false;
        this.roles.add(new Role(UserRole.ROLE_USER));

        if(wishlist!=null && !wishlist.equals("")) {
            if (wishlist.contains(",")) {
                String[] elements = wishlist.split(",");
                List<String> fixedLengthList = Arrays.asList(elements);
                this.wishlist = new ArrayList<>(fixedLengthList);
            } else {
                this.wishlist.add(wishlist);
            }
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<String> getWishlist() {
        return wishlist;
    }

    public void addToWishlist(String item) {
        if(this.wishlist.get(0).equals("")){
            this.clearWishlist();
        }

        if(item.contains(",")) {
            String[] elements = item.split(",");
            this.wishlist.addAll(Arrays.asList(elements));
        } else {
            this.wishlist.add(item);
        }
    }

    public void clearWishlist() { this.wishlist.clear(); }

    public void removeFromWishlist(String item) { this.wishlist.remove(item); }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.admin=false;

        for(Role role : roles){
            if(role.getRoleName() == UserRole.ROLE_ADMIN){
                this.admin = true;
            }
        }

        this.roles = roles;
    }
}
