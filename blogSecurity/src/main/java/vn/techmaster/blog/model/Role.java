package vn.techmaster.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "role")
@Table(name = "role")
@NoArgsConstructor
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users=new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Role(String name) {
        this.name = name;
    }

    public int hashCode() {
        return Objects.hash(this.name);
    }

    public boolean equals(Object o) {
        if (o == this) { return true; }
        if (o == null || !(o instanceof Role) ) { return false; }
        return Objects.equals(this.name, ((Role) o).name);
    }
}
