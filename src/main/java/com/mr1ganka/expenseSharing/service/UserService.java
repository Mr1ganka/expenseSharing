package com.mr1ganka.expenseSharing.service;

import com.mr1ganka.expenseSharing.exception.UserAlreadyExistsException;
import com.mr1ganka.expenseSharing.model.User;
import com.mr1ganka.expenseSharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw  new UserAlreadyExistsException("Email is already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
