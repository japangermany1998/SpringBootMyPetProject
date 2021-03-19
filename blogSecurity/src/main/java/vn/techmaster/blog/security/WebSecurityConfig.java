package vn.techmaster.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.repository.UserRepository;
import vn.techmaster.blog.service.IAuthenService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private UserRepository repository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JWT_TOKEN")
                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/posts", true);
//                .failureUrl("/login_error");

        http.authorizeRequests()
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAuthority("ADMIN")
                .antMatchers("/login").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/author").hasAnyAuthority("AUTHOR")
                .antMatchers("/editor").hasAnyAuthority("EDITOR")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().ignoringAntMatchers("/h2-console/**") //https://jessitron.com/2020/06/15/spring-security-for-h2-console/
                .and().headers().frameOptions().sameOrigin();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/css/**", "/script/**", "/image/**", "/vendor/**", "/favicon.ico", "/adminlte/**", "/media/static/**");
    }
}
