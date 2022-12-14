package com.auth.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}