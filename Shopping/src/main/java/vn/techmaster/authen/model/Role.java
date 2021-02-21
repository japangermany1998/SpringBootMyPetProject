package vn.techmaster.authen.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id private long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    List<User> users=new ArrayList<>();
}
