package com.johnwstump.incentivizer.services.user.impl;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.user.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> possibleUser = userRepository.findByEmail(email);

        if (possibleUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found for email: " + email);
        }

        User user = possibleUser.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getEncryptedPassword(), getAuthorities(new ArrayList<>()));
    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        Optional<User> possibleUser = userRepository.findById(id);

        if (possibleUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found for id: " + id);
        }

        User user = possibleUser.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getEncryptedPassword(), getAuthorities(new ArrayList<>()));
    }

    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
