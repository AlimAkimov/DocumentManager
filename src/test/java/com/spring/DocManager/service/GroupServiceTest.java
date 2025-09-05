package com.spring.DocManager.service;

import com.spring.DocManager.model.Group;
import com.spring.DocManager.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    private Group group1;
    private Group group2;

    @BeforeEach
    void setUp() {
        group1 = new Group();
        group1.setId(1L);
        group1.setName("Группа 1");
        group1.setColor("#FFFFFF");

        group2 = new Group();
        group2.setId(2L);
        group2.setName("Группа 2");
        group2.setColor("#000000");
    }

    @Test
    void testFindAllGroups() {
        List<Group> groups = Arrays.asList(group1, group2);
        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> result = groupService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(group1));
        assertTrue(result.contains(group2));
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testFindGroupById_WhenExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group1));

        Optional<Group> result = groupService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(group1, result.get());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void testFindGroupById_WhenNotExists() {
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Group> result = groupService.findById(999L);

        assertFalse(result.isPresent());
        verify(groupRepository, times(1)).findById(999L);
    }

    @Test
    void testSaveGroup() {
        when(groupRepository.save(group1)).thenReturn(group1);

        Group result = groupService.save(group1);

        assertEquals(group1, result);
        verify(groupRepository, times(1)).save(group1);
    }

    @Test
    void testDeleteGroupById() {
        groupService.deleteById(1L);

        verify(groupRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateGroup_WhenExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group1));
        when(groupRepository.save(group1)).thenReturn(group1);

        Group updatedGroup = new Group();
        updatedGroup.setId(1L);
        updatedGroup.setName("updated group");
        updatedGroup.setColor("#FF0000");

        groupService.updateGroup(updatedGroup);

        assertEquals("updated group", group1.getName());
        assertEquals("#FF0000", group1.getColor());
        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).save(group1);
    }

    @Test
    void testUpdateGroup_WhenNotExists_ShouldThrowException() {
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());
        Group group = new Group();
        group.setId(999L);

        assertThrows(IllegalArgumentException.class, () -> groupService.updateGroup(group));
        verify(groupRepository, times(1)).findById(999L);
        verify(groupRepository, never()).save(any());
    }
}