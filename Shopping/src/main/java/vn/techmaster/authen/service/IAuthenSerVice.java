package vn.techmaster.authen.service;

import vn.techmaster.authen.model.Event;
import vn.techmaster.authen.model.Role;
import vn.techmaster.authen.model.User;

import java.util.List;


public interface IAuthenSerVice {
    public List<String> getPassword();
        public List<User> getAll();
    public void createAccount(User user);

    void updateAccount(long id, String username, String email, String password) throws AuthenException;

    public String hashPassword(String password);
    public User findUserById(long id);
    public User findUserByEmail(String email);
    public boolean isAdmin(long id);
    public boolean hasRole(long id,String role);
    public void updateRole(long id,List<String> roles) throws AuthenException;
    public List<String> getRoles(long id);
    public List<String> getAllRoles();

    Role findRolebyName(String role);

    public void addEvent(long id, String event) throws AuthenException;
    boolean login(String email, String password);
}
