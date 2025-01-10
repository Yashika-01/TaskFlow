package com.taskflow.taskflow.service;

import com.taskflow.taskflow.model.User;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("Loaded user: " + user.getUsername());
        System.out.println(("Password: " + user.getPassword()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Ensure this is the hashed password
                new ArrayList<>()
        );
    }
}

