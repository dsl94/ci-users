package com.ciusers.service;

import com.ciusers.controller.dto.PasswordResetTokenResponseDTO;
import com.ciusers.controller.dto.ResetPasswordRequestDTO;
import com.ciusers.controller.dto.UserDTO;
import com.ciusers.entity.PasswordResetToken;
import com.ciusers.entity.Role;
import com.ciusers.entity.User;
import com.ciusers.error.ErrorCode;
import com.ciusers.error.exception.RoleException;
import com.ciusers.error.exception.UserException;
import com.ciusers.repository.PasswordResetTokenRepository;
import com.ciusers.repository.RoleRepository;
import com.ciusers.repository.UserRepository;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public User create(UserDTO userDTO) throws UserException, RoleException, ValidationException {
        if (userRepository.findByUsernameIgnoreCase(userDTO.getUsername()) != null) {
            throw new UserException("User with that username already exist", ErrorCode.USERNAME_EXIST);
        }
        if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()) != null) {
            throw new UserException("User with that email already exist", ErrorCode.EMAIL_EXIST);
        }
        if (userDTO.getPassword() == null) {
            throw new ValidationException("Password is mandatory");
        }

        User user = userDTO.toEntity();
        Set<Role> roles = new HashSet<>();
        extractRoles(userDTO, roles);

        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(String id) throws UserException {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        if (!user.isPresent()) {
            throw new UserException("User with that id not found", ErrorCode.NOT_FOUND);
        } else {
            return user.get();
        }
    }

    public User update(UserDTO userDTO, String id) throws UserException, RoleException {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        if (user.isPresent()) {
            if (!user.get().getUsername().equals(userDTO.getUsername()) && userRepository.findByUsernameIgnoreCase(userDTO.getUsername()) != null) {
                throw new UserException("User with that username already exist", ErrorCode.USERNAME_EXIST);
            }
            if (!user.get().getEmail().equals(userDTO.getEmail()) && userRepository.findByEmailIgnoreCase(userDTO.getEmail()) != null) {
                throw new UserException("User with that email already exist", ErrorCode.EMAIL_EXIST);
            }

            Set<Role> roles = new HashSet<>();
            extractRoles(userDTO, roles);

            user.get().setRoles(roles);

            if (userDTO.getPassword() != null) {
                user.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            user.get().setUsername(userDTO.getUsername());
            user.get().setEmail(userDTO.getEmail());
            user.get().setFirstName(userDTO.getFirstName());
            user.get().setLastName(userDTO.getLastName());

            return userRepository.save(user.get());
        } else {
            throw new UserException("User with that id not found", ErrorCode.NOT_FOUND);
        }
    }

    public boolean delete(String id) throws UserException {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        if (!user.isPresent()) {
            throw new UserException("User with that id does not exist", ErrorCode.NOT_FOUND);
        }

        userRepository.delete(user.get());
        return true;
    }

    // IMPORTANT - WE DON'T THROW EXCEPTION IF USER IS NOT FOUND, JUST RETURN TOKEN IF USER IS FOUND
    public PasswordResetTokenResponseDTO generatePasswordResetToken(ResetPasswordRequestDTO dto) {
        User user = userRepository.findByEmailIgnoreCase(dto.getEmail());
        if (user != null) {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            RandomStringGenerator randomStringGenerator =
                    new RandomStringGenerator.Builder()
                            .withinRange('0', 'z')
                            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                            .build();

            passwordResetToken.setToken(randomStringGenerator.generate(20));
            passwordResetToken.setUser(user);

            passwordResetTokenRepository.save(passwordResetToken);

            PasswordResetTokenResponseDTO response = new PasswordResetTokenResponseDTO();
            response.setToken(passwordResetToken.getToken());
            return response;
        }

        return null;
    }

    private void extractRoles(UserDTO userDTO, Set<Role> roles) throws RoleException {
        for (String roleName : userDTO.getRoles()) {
            Role role = roleRepository.findByRoleIgnoreCase(roleName);
            if (role == null) {
                throw  new RoleException("Role " + roleName +" does not exist", ErrorCode.NOT_FOUND);
            }
            roles.add(role);
        }
    }
}
