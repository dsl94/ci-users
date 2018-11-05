package com.ciusers.service;

import com.ciusers.controller.dto.RoleDTO;
import com.ciusers.controller.dto.UserDTO;
import com.ciusers.entity.Role;
import com.ciusers.error.exception.RoleException;
import com.ciusers.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService underTest;

    @Test
    public void testCreate() throws RoleException {
        RoleDTO dto = new RoleDTO();
        dto.setName("Name");
        dto.setRole("role");

        Role expected = new Role();
        expected.setName(dto.getName());
        expected.setId(UUID.randomUUID());
        expected.setRole(dto.getRole());

        Mockito.when(roleRepository.findByRoleIgnoreCase(dto.getRole())).thenReturn(null);
        Mockito.when(roleRepository.save(any(Role.class))).thenReturn(expected);

        Role created = underTest.create(dto);

        assertNotNull(expected.getId());
        assertEquals(expected.getName(), created.getName());
        assertEquals(expected.getRole(), created.getRole());
    }

    @Test(expected = RoleException.class)
    public void testCreateDuplicate() throws RoleException {
        RoleDTO dto = new RoleDTO();
        dto.setName("Name");
        dto.setRole("role");

        Mockito.when(roleRepository.findByRoleIgnoreCase(dto.getRole())).thenReturn(new Role());

        underTest.create(dto);
    }

    @Test
    public void testGetAll() {
        List<Role> expected = Arrays.asList(new Role(), new Role());
        Pageable pageable = getPageable();
        long start = pageable.getOffset();
        long end = (start + pageable.getPageSize()) > expected.size() ? expected.size() : (start + pageable.getPageSize());
        Page<Role> pages = new PageImpl<Role>(expected.subList((int)start, (int)end), pageable, expected.size());
        Mockito.when(roleRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pages);

        Page<Role> actual = underTest.getAll(pageable);

        assertEquals(pages, actual);
    }

    @Test
    public void testGetOne() throws RoleException {
        Role expected = new Role();
        UUID id = UUID.randomUUID();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(expected));

        Role actual = underTest.get(id.toString());

        assertEquals(expected, actual);
    }

    @Test(expected = RoleException.class)
    public void testGetOneNotFound() throws RoleException {
        UUID id = UUID.randomUUID();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.empty());

        underTest.get(id.toString());
    }

    @Test
    public void testUpdate() throws RoleException {
        UUID id = UUID.randomUUID();
        Role existing = new Role();
        existing.setId(id);
        existing.setName("Name");
        existing.setRole("Role");

        RoleDTO dto = new RoleDTO();
        dto.setName("New name");
        dto.setRole("Role");

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(existing));

        existing = underTest.update(dto, id.toString());
        assertEquals(dto.getName(), existing.getName());
        assertEquals(dto.getRole(), existing.getRole());
    }

    @Test(expected = RoleException.class)
    public void testUpdateNotFound() throws RoleException {
        UUID id = UUID.randomUUID();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.empty());

        underTest.update(new RoleDTO(), id.toString());
    }

    @Test
    public void testDelete() throws RoleException {
        UUID id = UUID.randomUUID();
        Role role = new Role();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        boolean result = underTest.delete(id.toString());

        verify(roleRepository, times(1)).delete(role);
        assertTrue(result);
    }

    @Test(expected = RoleException.class)
    public void testDeleteNotFound() throws RoleException {
        UUID id = UUID.randomUUID();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.empty());

        underTest.delete(id.toString());
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
