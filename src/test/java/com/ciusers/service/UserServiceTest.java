package com.ciusers.service;

import com.ciusers.controller.dto.UserDTO;
import com.ciusers.entity.Role;
import com.ciusers.entity.User;
import com.ciusers.error.exception.RoleException;
import com.ciusers.error.exception.UserException;
import com.ciusers.repository.RoleRepository;
import com.ciusers.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService underTest;

    @Test
    public void testCreate() throws RoleException, ValidationException, UserException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@mail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("pass");
        userDTO.setRoles(Collections.singleton("ADMIN"));
        Role admin = new Role();

        Mockito.when(roleRepository.findByRoleIgnoreCase("ADMIN")).thenReturn(admin);
        Mockito.when(userRepository.findByEmailIgnoreCase(userDTO.getEmail())).thenReturn(null);
        Mockito.when(userRepository.findByUsernameIgnoreCase(userDTO.getUsername())).thenReturn(null);
        Mockito.when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("password");
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(createUser());


        User real = underTest.create(userDTO);
        assertNotNull(real);
        assertEquals(userDTO.getUsername(), real.getUsername());
    }

    @Test(expected = UserException.class)
    public void testCreateUserUsernameInvalid() throws RoleException, ValidationException, UserException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        Mockito.when(userRepository.findByUsernameIgnoreCase(userDTO.getUsername())).thenReturn(new User());

        underTest.create(userDTO);
    }

    @Test(expected = UserException.class)
    public void testCreateUserEmailInvalid() throws RoleException, ValidationException, UserException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@mail.com");

        Mockito.when(userRepository.findByEmailIgnoreCase(userDTO.getEmail())).thenReturn(new User());

        underTest.create(userDTO);
    }

    @Test(expected = RoleException.class)
    public void testCreateUserRoleInvalid() throws RoleException, ValidationException, UserException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPassword("password");
        userDTO.setRoles(Collections.singleton("ROLE"));

        Mockito.when(userRepository.findByEmailIgnoreCase(userDTO.getEmail())).thenReturn(null);
        Mockito.when(userRepository.findByUsernameIgnoreCase(userDTO.getUsername())).thenReturn(null);
        Mockito.when(roleRepository.findByRoleIgnoreCase("ROLE")).thenReturn(null);

        underTest.create(userDTO);
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setUsername("username");

        return user;
    }

}
