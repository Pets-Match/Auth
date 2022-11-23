package com.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AuthDTO;
import com.model.User;
import com.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UserService userService;
    
    @GetMapping()
    public ResponseEntity<String> auth(@RequestBody AuthDTO auth) {
        
        
        
        JSONObject json = new JSONObject();
        json.put("email", auth.getPassword());
        
        
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthDTO auth){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(auth.getPassword());

        User u = new User(auth.getEmail(), newPassword);
        userService.saveUser(u);

        return null;

    }
}
