package com.ciusers.service;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.entity.Role;
import com.ciusers.error.ErrorCode;
import com.ciusers.error.exception.RoleException;
import com.ciusers.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleDTO create(RoleDTO roleDTO) throws RoleException {
        if (roleRepository.findByRoleIgnoreCase(roleDTO.getRole()) != null) {
            throw new RoleException("Role already exist", ErrorCode.ROLE_EXIST);
        }

        Role created = roleRepository.save(roleDTO.toEntity());

        return new RoleDTO(created);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role get(String id) throws RoleException {
        if (roleRepository.findById(UUID.fromString(id)).isPresent()) {
            return roleRepository.findById(UUID.fromString(id)).get();
        } else {
            throw new RoleException("Role with that id not found", ErrorCode.NOT_FOUND);
        }
    }

    public Role update(RoleDTO roleDTO, String id) throws RoleException {
        if (roleRepository.findById(UUID.fromString(id)).isPresent()) {
            Role existing = roleRepository.findById(UUID.fromString(id)).get();
            existing.setRole(roleDTO.getRole());
            existing.setName(roleDTO.getName());
            return existing;
        } else {
            throw new RoleException("Role with that id not found", ErrorCode.NOT_FOUND);
        }
    }
}
