package com.ciusers.service;

import com.ciusers.controller.dto.*;
import com.ciusers.entity.PasswordResetToken;
import com.ciusers.entity.Role;
import com.ciusers.entity.User;
import com.ciusers.error.exception.PasswordResetException;
import com.ciusers.error.exception.RoleException;
import com.ciusers.error.exception.TokenException;
import com.ciusers.error.exception.UserException;
import com.ciusers.repository.PasswordResetTokenRepository;
import com.ciusers.repository.RoleRepository;
import com.ciusers.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
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
        Pageable pageable = getPageable();
        long start = pageable.getOffset();
        long end = (start + pageable.getPageSize()) > expected.size() ? expected.size() : (start + pageable.getPageSize());
        Page<User> pages = new PageImpl<User>(expected.subList((int)start, (int)end), pageable, expected.size());
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pages);

        Page<User> actual = underTest.getAll(pageable);

        assertEquals(pages, actual);
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

    @Test
    public void testGenerateToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("TOKEN");
        RequestPasswordResetDTO dto = new RequestPasswordResetDTO();
        dto.setEmail("email@email.com");

        Mockito.when(userRepository.findByEmailIgnoreCase("email@email.com")).thenReturn(createUser());

        PasswordResetTokenResponseDTO actual = underTest.generatePasswordResetToken(dto);

        assertNotNull(actual);
    }

    @Test
    public void testPasswordReset() throws TokenException, PasswordResetException {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("TOKEN");
        User user = createUser();
        token.setUser(user);
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setPassword("pass");
        dto.setPasswordRetyped("pass");

        Mockito.when(passwordResetTokenRepository.findByToken("TOKEN")).thenReturn(token);
        Mockito.when(passwordEncoder.encode("pass")).thenReturn("pass");

        underTest.resetPassword("TOKEN", dto);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(passwordResetTokenRepository, Mockito.times(1)).delete(token);
    }

    @Test(expected = TokenException.class)
    public void testResetPasswordNotFoundToken() throws TokenException, PasswordResetException {
        Mockito.when(passwordResetTokenRepository.findByToken("TOKEN")).thenReturn(null);

        underTest.resetPassword("TOKEN", new ResetPasswordDTO());
    }

    @Test
    public void testAccountUpdate() {
        User user = createUser();
        AccountUpdateDTO dto = new AccountUpdateDTO();
        dto.setFirstName("FIRSTNAME");
        User excpectedUpdated = user;
        user.setFirstName(dto.getFirstName());

        Mockito.when(userRepository.findByUsernameIgnoreCase(user.getUsername())).thenReturn(user);
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(excpectedUpdated);

        User updated = underTest.updateAccount(user.getUsername(), dto);

        assertEquals(dto.getFirstName(), updated.getFirstName());
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setUsername("username");

        return user;
    }

    private Pageable getPageable() {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

    }

}
