package vn.techmaster.authen.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "person")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column(nullable = false)
    private String Fullname;
    @NaturalId
    @Column(nullable = false, unique = true,name = "Email")
    private String email;
    @Transient
    private String password;
    @Column(name = "Password",nullable = false)
    private String HashPassword;

    @ManyToMany()
    @JoinTable(
            name ="user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles= new HashSet<>();

    public void addRole(Role role){
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role){
        roles.remove(role);
        role.getUsers().remove(this);
    }

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_id")
    List<Event> events=new ArrayList<>();

    public void addEvent(Event event){
        events.add(event);
        event.setUser(this);
    }
}
