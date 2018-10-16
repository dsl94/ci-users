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
import java.util.*;

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

    @Test
    public void testGetAllUsers() {
        List<User> expected = Arrays.asList(new User(), new User(), new User());
        Mockito.when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = underTest.getAll();

        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testGetOneUser() throws UserException {
        User expected = createUser();
        Mockito.when(userRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        User actual = underTest.get(expected.getId().toString());

        assertEquals(expected, actual);
    }

    @Test(expected = UserException.class)
    public void testGetOneNotFound() throws UserException {
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        underTest.get(id.toString());
    }

    @Test
    public void testUpdate() throws RoleException, UserException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user1@mail.com");
        userDTO.setUsername("username1");
        userDTO.setPassword("pass");
        userDTO.setRoles(Collections.singleton("ADMIN"));
        Role admin = new Role();
        User existing = createUser();

        Mockito.when(roleRepository.findByRoleIgnoreCase("ADMIN")).thenReturn(admin);
        Mockito.when(userRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        Mockito.when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("password");
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(existing);

        User edited = underTest.update(userDTO, existing.getId().toString());

        assertEquals(userDTO.getUsername(), edited.getUsername());
        assertEquals(userDTO.getEmail(), edited.getEmail());
    }

    @Test(expected = UserException.class)
    public void testUpdateUserNotFound() throws RoleException, UserException {
        UUID id = UUID.randomUUID();
        UserDTO dto = new UserDTO();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        underTest.update(dto, id.toString());
    }

    @Test
    public void testDelete() throws UserException {
        User user = createUser();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        boolean result = underTest.delete(user.getId().toString());

        assertTrue(result);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test(expected = UserException.class)
    public void testDeleteNotFound() throws UserException {
        UUID id = UUID.randomUUID();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        underTest.delete(id.toString());
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setUsername("username");

        return user;
    }

}
