package com.auth.demo.controller;


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

import com.auth.demo.dto.AuthDTO;
import com.auth.demo.model.User;
import com.auth.demo.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<String> auth(@RequestBody AuthDTO auth) {
        User user = userService.findUserByEmail(auth.getEmail());

        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Boolean verify = passwordEncoder.matches(auth.getPassword(), user.getPassword());

            if(verify){
                JSONObject json = new JSONObject(user);
                return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
            }

            return new ResponseEntity<String>("Not found", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>("Not found", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthDTO auth) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(auth.getPassword());

        User u = new User(auth.getEmail(), newPassword);
        userService.saveUser(u);

        return null;

    }
}