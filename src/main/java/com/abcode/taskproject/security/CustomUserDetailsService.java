package com.abcode.taskproject.security;

import com.abcode.taskproject.entity.Users;
import com.abcode.taskproject.exception.UserNotFound;
import com.abcode.taskproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFound(String.format("User with email %s not found", email))
        );
        // Here we need the roles of the user to be passed to the User object
        Set<String> roles = new HashSet<String>(); // hash set is a collection that does not allow duplicates
        roles.add("ROLE_ADMIN");
        return new User(user.getEmail(), user.getPassword(), userAuthorities(roles));
    }

    private Collection<? extends GrantedAuthority> userAuthorities(Set<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
