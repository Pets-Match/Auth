package com.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class AuthDTO {

    @NotNull
    @NotEmpty
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthDTO() {    
    }
    
}
