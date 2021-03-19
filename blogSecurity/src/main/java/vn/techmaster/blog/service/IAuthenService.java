package vn.techmaster.blog.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.controller.request.RegisterRequest;
import vn.techmaster.blog.model.Role;
import vn.techmaster.blog.model.User;

import java.util.List;
import java.util.Set;

public interface IAuthenService {
    @Transactional
    void generateUsersRoles();

    List<Role> getAllRoles();

    Role getRole(String name);

    void updateRole(Set<Role> roles, long id);

    List<User> getAll();

    User findUserById(long id);

    User register(RegisterRequest request);
}
