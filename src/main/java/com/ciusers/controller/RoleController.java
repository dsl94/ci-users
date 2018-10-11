package com.ciusers.controller;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.error.ErrorMessage;
import com.ciusers.error.exception.RoleException;
import com.ciusers.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    public ResponseEntity create(@Valid @RequestBody RoleDTO role) {
        try {
            roleService.create(role);
            return ResponseEntity.ok().body(null);
        } catch (RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @GetMapping("/role/all")
    public ResponseEntity all() {
        return ResponseEntity.ok(roleService.getAll());
    }
}
