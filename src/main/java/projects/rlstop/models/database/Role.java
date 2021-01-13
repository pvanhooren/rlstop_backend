package projects.rlstop.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import projects.rlstop.models.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name= "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Enumerated(EnumType.ORDINAL)
    private UserRole roleName;
    
    @JsonIgnore
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="roles")
    private Collection<User> users = new ArrayList<>();

    public Role(UserRole roleName) {
        this.roleName = roleName;
    }

    public Role(){

    }

    public UserRole getRoleName() {
        return roleName;
    }
}
