package com.auth.demo.service;


import com.auth.demo.model.User;
import com.auth.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public User findUserByEmail(String email){
        return repository.findByEmail(email);
    }

    public void saveUser(User user){
        repository.save(user);
    }
}
