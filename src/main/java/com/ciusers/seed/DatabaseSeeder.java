package com.ciusers.seed;

import com.ciusers.entity.Role;
import com.ciusers.entity.User;
import com.ciusers.repository.RoleRepository;
import com.ciusers.repository.UserRepository;
import com.ciusers.security.RolesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DatabaseSeeder {

    private Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event){
        seedRolesTable();
        seedUsersTable();
    }

    private void seedRolesTable() {
        logger.info("Seeding roles 1");
        if (roleRepository.findAll().size() == 0) {
            logger.info("Seeding roles 2");
            Role superAdmin = new Role();
            superAdmin.setName("Superadmin");
            superAdmin.setRole(RolesConstants.ROLE_SUPERADMIN.name());
            roleRepository.save(superAdmin);

            Role admin = new Role();
            admin.setName("Admin");
            admin.setRole(RolesConstants.ROLE_ADMIN.name());
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("User");
            user.setRole(RolesConstants.ROLE_USER.name());
            roleRepository.save(user);
        }
    }

    private void seedUsersTable() {
        if (userRepository.findAll().size() == 0) {
            Role superAdminRole = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_SUPERADMIN.name());
            Role adminRole = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_ADMIN.name());
            Role userRole = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());

            User superadmin = new User();
            superadmin.setFirstName("Super");
            superadmin.setLastName("Admin");
            superadmin.setEmail("superadmin@mail.com");
            superadmin.setUsername("superadmin");
            superadmin.setPassword(passwordEncoder.encode("superadmin"));
            superadmin.setActive(true);
            superadmin.setRoles(Collections.singleton(superAdminRole));
            userRepository.save(superadmin);

            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@mail.com");
            admin.setUsername("admin");
            admin.setRoles(Collections.singleton(adminRole));
            admin.setActive(true);
            admin.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(admin);

            User user = new User();
            user.setFirstName("User");
            user.setLastName("User");
            user.setEmail("user@mail.com");
            user.setUsername("user");
            user.setRoles(Collections.singleton(userRole));
            user.setActive(true);
            user.setPassword(passwordEncoder.encode("user"));
            userRepository.save(user);
        }
    }
}
