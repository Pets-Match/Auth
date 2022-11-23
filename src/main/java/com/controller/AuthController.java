package com.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AuthDTO;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @GetMapping()
    public ResponseEntity<String> auth(@RequestBody AuthDTO auth) {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(auth.getPassword());

        JSONObject json = new JSONObject();
        json.put("email", auth.getPassword());
        json.put("password",newPassword);


        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    @PostMapping("")
}
