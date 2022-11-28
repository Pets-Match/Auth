package com.auth.demo.controller;


import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.dto.AuthDTO;
import com.auth.demo.dto.RoleDTO;
import com.auth.demo.dto.RoleToUserDTO;
import com.auth.demo.dto.UserDTO;
import com.auth.demo.model.User;
import com.auth.demo.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<String> auth(@RequestBody AuthDTO auth) {
        User user = userService.findUserByEmail(auth.getEmail());

        if(auth.getEmail() == null || auth.getPassword() == null){
            return new ResponseEntity<String>("Invalid Body Request, either email or password is null ", HttpStatus.BAD_REQUEST);
        }

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
    public ResponseEntity<String> create(@RequestBody UserDTO userDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User(userDTO.getEmail(), newPassword);
        User savedUser = userService.saveUser(user);
        JSONObject userJson = new JSONObject(savedUser);
        
        return new ResponseEntity<String>(userJson.toString(), HttpStatus.CREATED);
        
    }
    
    @PostMapping("/user/add-role")
    public ResponseEntity<String> addRole(@RequestBody RoleToUserDTO roleToUserDTO){
        
        userService.addRoleToUser(roleToUserDTO.getEmail(), roleToUserDTO.getRoleName());
        return null;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

}