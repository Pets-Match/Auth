package com.auth.demo.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.dto.AuthDTO;
import com.auth.demo.dto.RoleToUserDTO;
import com.auth.demo.dto.UserDTO;
import com.auth.demo.model.User;
import com.auth.demo.service.UserService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController

@RequestMapping(value = "/auth")
public class AuthController {
    static String secret = "yjI5BMKPBV55bhp4hqIiVUSxWFiYElL2HU213Y7128JS1289IKO";

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<String> auth(@RequestBody AuthDTO auth) {
        User usuario = userService.findUserByEmail(auth.getEmail());

        if (usuario == null) {
            return new ResponseEntity<String>("Credentials not found ", HttpStatus.BAD_REQUEST);
        }

        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Boolean verify = passwordEncoder.matches(auth.getPassword(), usuario.getPassword());

            if (verify) {
                String jwtToken = Jwts.builder()
                        .setSubject(usuario.getEmail())
                        .setIssuer("localhost:8080")
                        .setIssuedAt(new Date())
                        .setExpiration(
                                Date.from(
                                        LocalDateTime.now().plusMinutes(15L)
                                                .atZone(ZoneId.systemDefault())
                                                .toInstant()))
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
                JSONObject json = new JSONObject();
                json.put("token", jwtToken);
                json.put("id", usuario.getId());

                return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Credentials not found", HttpStatus.BAD_REQUEST);

            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserDTO userDTO) {
        User verifier = userService.findUserByEmail(userDTO.getEmail());
        if (verifier != null) {
            return new ResponseEntity<String>("Email already registered", HttpStatus.BAD_REQUEST);
        }
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String newPassword = passwordEncoder.encode(userDTO.getPassword());

            User user = new User(userDTO.getEmail(), newPassword);
            User savedUser = userService.saveUser(user);
            JSONObject userJson = new JSONObject(savedUser);

            JSONObject json = new JSONObject(userDTO);
            json.put("id", savedUser.getId());

            var request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3030/owner"))
                    .header("Content-type", "application/json")
                    .POST(BodyPublishers.ofString(json.toString()))
                    .timeout(Duration.ofSeconds(15))
                    .build();
            var client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201 && response.statusCode() != 200) {
                JSONObject errorJSON = new JSONObject(response.body());
                throw new Exception(errorJSON.getString("error"));
            }

            return new ResponseEntity<String>(userJson.toString(), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/user/add-role")
    public ResponseEntity<String> addRole(@RequestBody RoleToUserDTO roleToUserDTO) {

        userService.addRoleToUser(roleToUserDTO.getEmail(), roleToUserDTO.getRoleName());
        return null;
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        try {
            if (middleware(request).getStatusCodeValue() == 200) {
                JSONObject middlewareResponseBody = new JSONObject(middleware(request).getBody());

                Optional<User> user = userService.findUserById(middlewareResponseBody.getInt("id"));

                if (!user.isEmpty()) {

                    var requestToService = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:3030/owner"))
                            .header("Content-type", "application/json")
                            .DELETE()
                            .headers("Authorization", request.getHeader("Authorization"))
                            .timeout(Duration.ofSeconds(15))
                            .build();
                    var client = HttpClient.newHttpClient();

                    HttpResponse<String> response = client.send(requestToService,
                            HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        userService.deleteUserById(middlewareResponseBody.getInt("id"));
                        JSONObject toResponse = new JSONObject();
                        toResponse.put("message",
                                "Done! We have deleted all your data (such as owner info and pet info");
                        return new ResponseEntity<String>("", HttpStatus.OK);
                    } else {
                        JSONObject errorJSON = new JSONObject(response.body());
                        throw new Exception(errorJSON.toString());
                    }

                } else {
                    return new ResponseEntity<String>("not feito, id not found", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<String>("verify your token", middleware(request).getStatusCode());
            }

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {

        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/middleware")
    public ResponseEntity<String> middleware(HttpServletRequest request) {
        try {

            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring("Bearer".length()).trim();

            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            JSONObject json = new JSONObject(jwt);
            JSONObject jsonCringe = (JSONObject) json.get("body");

            User user = userService.findUserByEmail(jsonCringe.getString("sub"));

            JSONObject response = new JSONObject(user);

            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
        }
    }

}