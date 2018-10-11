package com.ciusers.service;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.entity.Role;
import com.ciusers.error.ErrorCode;
import com.ciusers.error.exception.RoleException;
import com.ciusers.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
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

    public List<RoleDTO> getAll() {
        List<Role> entites = roleRepository.findAll();
        List<RoleDTO> roles = entites.stream().map(RoleDTO::new).collect(Collectors.toList());

        return roles;
    }
}
