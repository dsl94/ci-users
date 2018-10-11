package com.ciusers.security.service;

import com.ciusers.entity.Role;
import com.ciusers.entity.User;
import com.ciusers.repository.UserRepository;
import com.ciusers.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user - {}", username);

        UserDetails userDetails;

        User user;

        try {

            if (logger.isDebugEnabled())
                logger.debug("userService-{}", userRepository);

            user = userRepository.findByUsernameIgnoreCase(username);

            if (logger.isDebugEnabled())
                logger.debug("user - {}", user);

            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            userDetails = new JwtUser(
                    user
                    , getAuthorities(user)
                    , (user.isActive()));

            if (userDetails.getAuthorities().isEmpty()) {
                logger.debug("User - {}, Permission count - 0", username);
                throw new UsernameNotFoundException(username);
            }

            return userDetails;

        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Service call error", e);
            throw new UsernameNotFoundException(username);
        }

    }

    public Collection<GrantedAuthority> getAuthorities(User user) {

        List<GrantedAuthority> authList = new ArrayList<>();
        for (Role role: user.getRoles()) {
            authList.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authList;

    }

}

