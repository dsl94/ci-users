package com.ciusers.controller.dto;

import com.ciusers.entity.Role;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RoleDTO {
    @NotNull
    @Length(min = 3)
    private String name;
    @NotNull
    @Length(min = 3)
    private String role;

    public RoleDTO() {
    }

    public RoleDTO(Role role) {
        this.name = role.getName();
        this.role = role.getRole();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role toEntity() {
        Role role = new Role();
        role.setName(this.name);
        role.setRole(this.role);

        return role;
    }
}
