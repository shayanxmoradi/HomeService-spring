package org.example.homeservice.service.admin;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {


    @Mock
    private SpecialistRepo specialistRepo;

    @Mock
    private SpecialistMapper specialistMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testSaveSpecialist_SpecialistDoesNotExist() {
        SpecialistRequest specialistRequest = new SpecialistRequest("john.doe@example.com", "John", "Doe@gmail.com",
                "12345678", SpecialistStatus.NEW,00.0,null);
        when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.empty());
        Specialist specialist = new Specialist();
        when(specialistMapper.toEntity(specialistRequest)).thenReturn(specialist);

        adminService.saveSpecialist(specialistRequest);

        verify(specialistRepo).save(specialist);
    }

    @Test
    public void testSaveSpecialist_SpecialistEmailExists() {
        SpecialistRequest specialistRequest = new SpecialistRequest("john.doe@example.com", "John", "Doe@gmail.com",
                "12345678", SpecialistStatus.NEW,00.0,null);
        when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.of(new Specialist()));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            adminService.saveSpecialist(specialistRequest);
        });
        assertEquals("Specialist email already exists", exception.getMessage());
        verify(specialistRepo, never()).save(any());
    }

    @Test
    public void testDeleteSpecialistById_SpecialistExists() {
        Long specialistId = 1L;
        when(specialistRepo.existsById(specialistId)).thenReturn(true);

        adminService.deleteSpecialistById(specialistId);

        verify(specialistRepo).deleteById(specialistId);
    }

    @Test
    public void testDeleteSpecialistById_SpecialistDoesNotExist() {
        Long specialistId = 1L;
        when(specialistRepo.existsById(specialistId)).thenReturn(false);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            adminService.deleteSpecialistById(specialistId);
        });
        assertEquals("Specialist not found", exception.getMessage());
        verify(specialistRepo, never()).deleteById(specialistId);
    }

//    @Test
//    public void testGetAllSpecialists() {
//        List<Specialist> specialists = new ArrayList<>();
//        specialists.add(new Specialist());
//        when(specialistRepo.findAll()).thenReturn(specialists);
//        when(specialistMapper.toDto(specialists)).thenReturn(new ArrayList<>());
//
//        List<SpecialistResponse> result = adminService.getAllSpecialists();
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        verify(specialistRepo).findAll();
//    }

    @Test
    public void testGetAllSpecialists_NoSpecialistsFound() {
        when(specialistRepo.findAll()).thenReturn(new ArrayList<>());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            adminService.getAllSpecialists();
        });
        assertEquals("No specialists found", exception.getMessage());
        verify(specialistRepo).findAll();
    }
}
