package com.ciusers.controller;

import com.ciusers.security.JwtTokenUtil;
import com.ciusers.security.dto.JwtAuthenticationDto;
import com.ciusers.security.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthenticationToken(
            @RequestBody JwtAuthenticationDto jwtAuthenticationDto)
            throws Exception {

        // Checking username|email and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtAuthenticationDto.getUsername(), jwtAuthenticationDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationDto.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return ResponseEntity.ok(new JwtResponse(token, dateFormat.format(expiration)));

    }

    @RequestMapping("/current")
    public ResponseEntity<UserDetails> getCurrent() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authenticatedUserName = authentication.getName();
        if(authenticatedUserName.equals("anonymousUser"))
            // TODO - Throw better exception
            throw new Exception(authenticatedUserName);
        else
            return ResponseEntity.ok((UserDetails)authentication.getPrincipal());
    }

}

