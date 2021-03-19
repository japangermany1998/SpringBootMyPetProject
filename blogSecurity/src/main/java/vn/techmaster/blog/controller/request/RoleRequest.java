package vn.techmaster.blog.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.techmaster.blog.model.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private long id;
    private Set<Role> roles;
}
