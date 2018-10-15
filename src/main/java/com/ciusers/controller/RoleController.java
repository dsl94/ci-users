package com.ciusers.controller;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.error.ErrorMessage;
import com.ciusers.error.exception.RoleException;
import com.ciusers.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/role/{id}")
    public ResponseEntity get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(roleService.get(id));
        } catch (RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @PutMapping("/role/{id}")
    public ResponseEntity update(@PathVariable String id, @Valid @RequestBody RoleDTO role) {
        try {
            return ResponseEntity.ok(roleService.update(role, id));
        } catch (RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            roleService.delete(id);
            return ResponseEntity.ok(null);
        } catch (RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }
}
