package com.easylearning.services.jwtService;

import com.easylearning.models.User;
import com.easylearning.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = userRepository.findFirstByEmail(email);
        if(user == null ){
            throw new UsernameNotFoundException("User not found");
        }

        // Logging UserDetails information
        log.info("UserDetails loaded for user with email: {}", email);
        log.info("Username: {}", user.getEmail());
        log.info("Password: {}", user.getPassword());
        log.info("Roles: {}", user.getRoles());

        // Convert roles to GrantedAuthorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        /*
        If a user is found, it constructs a UserDetails object representing that user.
        This object is used by Spring Security for authentication and authorization purposes.
        */
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
