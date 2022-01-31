
package com.example.testApp.service;

import com.example.testApp.model.Author;
import com.example.testApp.model.User;
import com.example.testApp.repository.AuthorRepository;
import com.example.testApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User add(User newUser) {
        return userRepository.save(newUser);
    }

}

