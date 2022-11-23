package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
 
    public int getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }

    public void setEmail(String email){
       this.email = email;
    }

    public void setPassword(String password){
       this.password = password;
    }

    
}