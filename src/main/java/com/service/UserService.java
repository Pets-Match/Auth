package com.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.model.User;
import com.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository repository;


    public void saveUser(User user){
        repository.save(user);
    }
}
