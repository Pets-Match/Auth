package com.auth.demo.service;

import com.auth.demo.interfaces.UserDetailsService;
import com.auth.demo.model.Role;
import com.auth.demo.model.User;
import com.auth.demo.repositories.RoleRepository;
import com.auth.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = { Exception.class })
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);

    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NullPointerException("User not found");
        }

        // return new
        // org.springframework.security.core.userdetails.User(user.getEmail(),
        // user.getPassword());
        return null;
    }
}
