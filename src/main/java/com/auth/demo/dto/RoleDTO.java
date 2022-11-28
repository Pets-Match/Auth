package com.auth.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoleDTO {

    
    @NotNull
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    
}
