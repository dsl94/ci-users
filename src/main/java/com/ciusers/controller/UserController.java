package com.ciusers.controller;

import com.ciusers.controller.dto.AccountUpdateDTO;
import com.ciusers.controller.dto.RequestPasswordResetDTO;
import com.ciusers.controller.dto.ResetPasswordDTO;
import com.ciusers.controller.dto.UserDTO;
import com.ciusers.entity.User;
import com.ciusers.error.ErrorMessage;
import com.ciusers.error.exception.PasswordResetException;
import com.ciusers.error.exception.RoleException;
import com.ciusers.error.exception.TokenException;
import com.ciusers.error.exception.UserException;
import com.ciusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import javax.xml.bind.ValidationException;
import javax.xml.ws.Response;

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
    public ResponseEntity requestPasswordReset(@Valid @RequestBody RequestPasswordResetDTO passwordReset) {
        return ResponseEntity.ok(userService.generatePasswordResetToken(passwordReset));
    }

    @PostMapping("/password/reset/{token}")
    public ResponseEntity resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordDTO resetPassword) {
        try {
            userService.resetPassword(token, resetPassword);
            return ResponseEntity.ok(null);
        } catch (TokenException | PasswordResetException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getErrorCode()));
        }
    }

    @PutMapping("/account")
    public ResponseEntity updateAccount(Authentication authentication, @Valid @RequestBody AccountUpdateDTO accountUpdate) {
        return ResponseEntity.ok(userService.updateAccount(authentication.getName(), accountUpdate));
    }
}
