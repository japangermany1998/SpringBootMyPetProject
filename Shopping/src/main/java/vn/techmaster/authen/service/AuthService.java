package vn.techmaster.authen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.techmaster.authen.model.Event;
import vn.techmaster.authen.model.Role;
import vn.techmaster.authen.model.User;
import vn.techmaster.authen.repository.RoleRepository;
import vn.techmaster.authen.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthenSerVice{
    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder passwordencode;

    @Autowired
    RoleRepository roleRepository;

    private List<String> password=new ArrayList<>();

    public List<String> getPassword(){
        return password;
    }

    @Override
    public List<User> getAll(){
        List<User> users=new ArrayList<>();
                repository.findAll().forEach(e->users.add(e));
                return users;
    }

    @Override
    public void createAccount(User user){
        repository.save(user);
    }

    @Override
    public void updateAccount(long id, String username, String email, String password) throws AuthenException {
        Optional<User> result=repository.findById(id);
        int ID=repository.findAll().stream().map(User::getId).collect(Collectors.toList()).indexOf(id);
        if(result.isEmpty()){
            return;
        }
        User user=result.get();
        user.setEmail(email);
        user.setFullname(username);
        getPassword().set(ID,password);
        user.setHashPassword(hashPassword(password));
        try {
            repository.save(user);
        }catch (Exception ex){
            throw new AuthenException("Loi update");
        }
    }

    @Override
    public String hashPassword(String password) {
        return passwordencode.encode(password);
    }

    @Override
    public User findUserById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public User findUserByEmail(String email)
    {
        if(!repository.findUserByEmail(email).isEmpty()){
            return repository.findUserByEmail(email).get();
        }
        return null;
    }

    @Override
    public boolean isAdmin(long id) {
        return repository.findById(id).get().getRoles().contains(roleRepository.findByName("admin"));
    }

    @Override
    public boolean hasRole(long id, String role) {
        return repository.findById(id).get().getRoles().contains(roleRepository.findByName(role));
    }

    @Override
    public void updateRole(long id, List<String> roles) throws AuthenException{
        Optional<User> result=repository.findById(id);
        if(result.isEmpty()){
            return;
        }
        User user=result.get();
        Set<Role> setRole=new HashSet<>();
        for (String role:roles){
            setRole.add(roleRepository.findByName(role));
        }
        user.setRoles(setRole);
        try {
            repository.save(user);
        }catch (Exception ex){
            throw new AuthenException("Loi update");
        }
    }

    @Override
    public List<String> getRoles(long id) {
        return repository.findById(id).get().getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllRoles(){
        return roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public Role findRolebyName(String role){
        return roleRepository.findByName(role);
    }

    @Override
    public void addEvent(long id,String event) throws AuthenException{
        Optional<User> result=repository.findById(id);
        if(result.isEmpty()){
            return;
        }
        User user=result.get();
        Event event1=new Event();
        event1.setName(event);
        user.addEvent(event1);
        try {
            repository.save(user);
        }catch (Exception ex){
            throw new AuthenException("Loi update");
        }
    }

    @Override
    public boolean login(String email, String password) throws NullPointerException{
        int id=repository.findAll().stream().map(User::getEmail).collect(Collectors.toList()).indexOf(email);
       if(repository.findAll().stream().filter(u->u.getEmail().equals(email)).limit(1)
               .count()!=0){
           return getPassword().get(id).equals(password);
       }
       return false;
    }
}
