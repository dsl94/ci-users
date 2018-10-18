package com.ciusers.controller;

import com.ciusers.controller.dto.ResetPasswordRequestDTO;
import com.ciusers.controller.dto.UserDTO;
import com.ciusers.error.ErrorMessage;
import com.ciusers.error.exception.RoleException;
import com.ciusers.error.exception.UserException;
import com.ciusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity create(@Valid @RequestBody UserDTO userDTO) throws ValidationException {
        try {
            return ResponseEntity.ok(userService.create(userDTO));
        } catch (UserException | RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity all() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.get(id));
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity update(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.update(userDTO, id));
        } catch (UserException | RoleException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok(null);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @PostMapping("/password/forgotten")
    public ResponseEntity requestPasswordReset(@Valid @RequestBody ResetPasswordRequestDTO passwordReset) {
        return ResponseEntity.ok(userService.generatePasswordResetToken(passwordReset));
    }
}
