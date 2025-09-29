package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder;
    }

    public User register(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        if (u.getRole() == null) u.setRole("USER");
        return userRepository.save(u);
    }

    public List<User> findAll() { return userRepository.findAll(); }

    public void deleteById(Long id) { userRepository.deleteById(id); }
}
