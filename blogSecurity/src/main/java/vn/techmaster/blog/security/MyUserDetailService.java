package vn.techmaster.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.repository.UserRepository;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User not authorized.");
        }
    }
}
