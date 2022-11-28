package com.auth.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoleToUserDTO {
    
    @NotNull
    @NotEmpty
    private String email;
    private String roleName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public RoleToUserDTO(String email, String roleName) {
        this.email = email;
        this.roleName = roleName;    
    }
}
