package vn.techmaster.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.DTO.UserMapper;
import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.controller.request.RegisterRequest;
import vn.techmaster.blog.model.Role;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.repository.RoleRepository;
import vn.techmaster.blog.repository.UserRepository;
import vn.techmaster.blog.security.CustomUserDetails;
import vn.techmaster.blog.security.MyUserDetailService;

@Service
public class AuthenService implements IAuthenService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MyUserDetailService myUserDetailService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void generateUsersRoles() {
    Role roleAdmin = new Role("ADMIN");
    Role roleAuthor = new Role("AUTHOR");
    Role roleEditor = new Role("EDITOR");

    roleRepository.save(roleAdmin);
    roleRepository.save(roleAuthor);
    roleRepository.save(roleEditor);
    roleRepository.flush();

    User admin = new User();
    admin.setFullname("Admin");
    admin.setEmail("admin@gmail.com");

    admin.setPassword(passwordEncoder.encode("abc"));

    userRepository.save(admin);

    admin.addRole(roleAdmin);
    admin.addRole(roleAuthor);
    userRepository.flush();
  }

  @Override
  public List<Role> getAllRoles(){
    return roleRepository.findAll();
  }

  @Override
  public Role getRole(String name){
    Optional<Role> optionalRole=roleRepository.findByName(name);
    if(optionalRole.isPresent()){
      return optionalRole.get();
    }
    return null;
  }

  @Override
  public void updateRole(Set<Role> roles, long id){
    Optional<User> userO = userRepository.findById(id);
    if (!userO.isPresent()) {
      throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.");
    }
    User user=userO.get();
    System.out.println(roles);
    user.setRoles(roles);
    userRepository.flush();
  }

  @Override
  public List<User> getAll(){
    return userRepository.findAll();
  }

  @Override
  public User findUserById(long id){
    return userRepository.findById(id).get();
  }

  @Override
  public User register(RegisterRequest request){
    Optional<User> userO = userRepository.findByEmail(request.getEmail());
    if (userO.isPresent()) {
      throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.");
    }
    User user=new User();
    user.setFullname(request.getFullname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);

    user.addRole(roleRepository.findByName("AUTHOR").get());

    userRepository.flush();
    return user;
  }
}
