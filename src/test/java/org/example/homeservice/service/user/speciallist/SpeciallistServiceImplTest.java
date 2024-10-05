package org.example.homeservice.service.user.speciallist;

import jakarta.validation.ValidationException;
import org.example.homeservice.Exception.FileNotFoundException;
import org.example.homeservice.Exception.ImageTooLargeException;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.entity.Specialist;
import org.example.homeservice.entity.enums.SpecialistStatus;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.user.speciallist.SpeciallistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class SpeciallistServiceImplTest {
    @Mock
    private SpecialistRepo specialistRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private SpecialistMapper specialistMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private SpeciallistServiceImpl underTest;

    private SpecialistRequest specialistRequest;
    private SpecialistResponse specialistResponse;
    private Specialist specialist;

    @BeforeEach
    void setUp() {

        specialistRequest = new SpecialistRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password123",
                SpecialistStatus.APPROVED,
                4.5,
                new byte[0]
        );

        specialistResponse = new SpecialistResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                SpecialistStatus.APPROVED,
                4.5,
                new byte[0]
        );

        specialist = new Specialist();
        specialist.setId(1L);
        specialist.setFirstName("John");
        specialist.setLastName("Doe");
        specialist.setEmail("john.doe@example.com");
        specialist.setSpecialistStatus(SpecialistStatus.APPROVED);
        specialist.setRate(4.5);
        specialist.setPersonalImage(new byte[0]);
    }

    @Test
    void testFindById() {
        when(specialistRepo.findById(1L)).thenReturn(Optional.of(specialist));
        when(specialistMapper.toDto(specialist)).thenReturn(specialistResponse);

        Optional<SpecialistResponse> result = underTest.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(specialistResponse, result.get());
        verify(specialistRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveSpecialist_Successful() {
        when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.empty());
        when(specialistMapper.toEntity(specialistRequest)).thenReturn(specialist);
        when(specialistRepo.save(specialist)).thenReturn(specialist);
        when(specialistMapper.toDto(specialist)).thenReturn(specialistResponse);

        Optional<SpecialistResponse> result = underTest.save(specialistRequest);

        assertTrue(result.isPresent());
        assertEquals(specialistResponse, result.get());
        verify(specialistRepo, times(1)).findByEmail(specialistRequest.email());
        verify(specialistRepo, times(1)).save(specialist);
    }

    @Test
    void testSaveSpecialist_EmailAlreadyExists() {
        when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.of(specialist));

        ValidationException exception = assertThrows(ValidationException.class, () -> underTest.save(specialistRequest));

        assertEquals("Customer with this email already exists", exception.getMessage());
        verify(specialistRepo, times(1)).findByEmail(specialistRequest.email());
        verify(specialistRepo, never()).save(any());
    }

    @Test
    void testProcessImage_FileNotFound() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> underTest.processImage("invalidPath.jpg"));

        assertEquals("Image file not found at the  path.", exception.getMessage());
    }

    @Test
    void testProcessImage_TooLarge() {
        File imageFile = mock(File.class);
        when(imageFile.length()).thenReturn(400L * 1024); // Exceeds 300KB
        when(imageFile.exists()).thenReturn(true);

        ImageTooLargeException exception = assertThrows(ImageTooLargeException.class, () -> underTest.processImage(imageFile.getPath()));

        assertEquals("Image exceeds 300KB, cannot store it.", exception.getMessage());
    }

    @Test
    void testGetAvailableOrders() {
        Long specialistId = 1L;
        List<OrderResponse> expectedOrders = List.of(
                new OrderResponse(1L, 2L, 3L, "Order 1", LocalDateTime.now(), 100.0),
                new OrderResponse(2L, 3L, 4L, "Order 2", LocalDateTime.now().plusDays(1), 200.0)
        );

        when(orderService.findWaitingOrdersBySpecialist(specialistId)).thenReturn(expectedOrders);

        List<OrderResponse> result = underTest.getAvilableOrders(specialistId);

        assertEquals(expectedOrders, result);
        verify(orderService, times(1)).findWaitingOrdersBySpecialist(specialistId);
    }
}