package com.ciusers.service;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.entity.Role;
import com.ciusers.error.UserErrorCode;
import com.ciusers.error.exception.RoleException;
import com.ciusers.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role create(RoleDTO roleDTO) throws RoleException {
        if (roleRepository.findByRoleIgnoreCase(roleDTO.getRole()) != null) {
            throw new RoleException("Role already exist", UserErrorCode.ROLE_EXIST);
        }

        return roleRepository.save(roleDTO.toEntity());
    }

    public Page<Role> getAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public Role get(String id) throws RoleException {
        Optional<Role> role = roleRepository.findById(UUID.fromString(id));
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new RoleException("Role with that id not found", UserErrorCode.NOT_FOUND);
        }
    }

    public Role update(RoleDTO roleDTO, String id) throws RoleException {
        if (roleRepository.findById(UUID.fromString(id)).isPresent()) {
            Role existing = roleRepository.findById(UUID.fromString(id)).get();
            existing.setRole(roleDTO.getRole());
            existing.setName(roleDTO.getName());
            return existing;
        } else {
            throw new RoleException("Role with that id not found", UserErrorCode.NOT_FOUND);
        }
    }

    public boolean delete(String id) throws RoleException {
        Optional<Role> role = roleRepository.findById(UUID.fromString(id));
        if (role.isPresent()) {
            roleRepository.delete(role.get());
            return true;
        } else {
            throw new RoleException("Role with that id not found", UserErrorCode.NOT_FOUND);
        }
    }
}
