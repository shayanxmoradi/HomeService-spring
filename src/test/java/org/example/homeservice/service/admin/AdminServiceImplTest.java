package org.example.homeservice.service.admin;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.service.admin.AdminServiceImpl;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    // Mocks for all dependencies of AdminServiceImpl
    @Mock
    private SpecialistRepo specialistRepo;
    @Mock
    private SpecialistMapper specialistConverter;
    @Mock
    private ServiceService serviceService;
    @Mock
    private SpeciallistService speciallistService;

    // The instance of the class we are testing, with mocks injected
    @InjectMocks
    private AdminServiceImpl adminService;

    // Test data objects
    private Specialist specialist;
    private SpecialistRequest specialistRequest;
    private SpecialistResponse specialistResponse;

    @BeforeEach
    void setUp() {
        // Correctly instantiate DTOs based on their record definitions
        specialistRequest = new SpecialistRequest(
                null, // id
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                "password123", // password
                SpecialistStatus.NEW, // specialistStatus
                0.0, // rate
                null // personalImage
        );

        specialist = new Specialist();
        specialist.setId(1L);
        specialist.setEmail(specialistRequest.email());
        specialist.setSpecialistStatus(SpecialistStatus.APPROVED);

        specialistResponse = new SpecialistResponse(
                1L, // id
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                SpecialistStatus.NEW, // specialistStatus
                150.0, // rate
                0, // numberOfRate
                null, // personalImage
                201L, // walletId
                true // isActive
        );
    }

    @Nested
    @DisplayName("Specialist Creation and Deletion Tests")
    class SpecialistCreationDeletionTests {

        @Test
        @DisplayName("saveSpecialist should succeed when email does not exist")
        void saveSpecialist_whenEmailIsUnique_shouldSaveSpecialist() {
            // Arrange
            when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.empty());
            when(specialistConverter.toEntity(specialistRequest)).thenReturn(specialist);

            // Act
            adminService.saveSpecialist(specialistRequest);

            // Assert
            verify(specialistRepo).findByEmail(specialistRequest.email());
            verify(specialistConverter).toEntity(specialistRequest);
            verify(specialistRepo).save(specialist);
        }

        @Test
        @DisplayName("saveSpecialist should throw ValidationException when email already exists")
        void saveSpecialist_whenEmailExists_shouldThrowException() {
            // Arrange
            when(specialistRepo.findByEmail(specialistRequest.email())).thenReturn(Optional.of(specialist));

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () ->
                    adminService.saveSpecialist(specialistRequest)
            );
            assertEquals("Specialist email already exists", exception.getMessage());
            verify(specialistRepo, never()).save(any(Specialist.class));
        }

        @Test
        @DisplayName("deleteSpecialistById should succeed when specialist exists")
        void deleteSpecialistById_whenSpecialistExists_shouldDelete() {
            // Arrange
            when(specialistRepo.existsById(1L)).thenReturn(true);

            // Act
            adminService.deleteSpecialistById(1L);

            // Assert
            verify(specialistRepo).existsById(1L);
            verify(specialistRepo).deleteById(1L);
        }

        @Test
        @DisplayName("deleteSpecialistById should throw ValidationException when specialist not found")
        void deleteSpecialistById_whenSpecialistNotFound_shouldThrowException() {
            // Arrange
            when(specialistRepo.existsById(99L)).thenReturn(false);

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () ->
                    adminService.deleteSpecialistById(99L)
            );
            assertEquals("Specialist not found", exception.getMessage());
            verify(specialistRepo, never()).deleteById(anyLong());
        }
    }


    @Nested
    @DisplayName("Fetching Specialists Tests")
    class FetchingSpecialistsTests {

        @Test
        @DisplayName("getAllSpecialists should return list when specialists exist")
        void getAllSpecialists_whenFound_shouldReturnSpecialistList() {
            // Arrange
            List<Specialist> specialists = List.of(specialist);
            when(specialistRepo.findAll()).thenReturn(specialists);
            when(specialistConverter.toDto(specialists)).thenReturn(List.of(specialistResponse));

            // Act
            List<SpecialistResponse> result = adminService.getAllSpecialists();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            verify(specialistRepo).findAll();
        }

        @Test
        @DisplayName("getAllSpecialists should throw ValidationException when no specialists exist")
        void getAllSpecialists_whenNoneFound_shouldThrowException() {
            // Arrange
            when(specialistRepo.findAll()).thenReturn(Collections.emptyList());

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () ->
                    adminService.getAllSpecialists()
            );
            assertEquals("No specialists found", exception.getMessage());
        }
    }


    @Nested
    @DisplayName("Specialist Status and Service Management Tests")
    class SpecialistManagementTests {

        @Test
        @DisplayName("acceptSpecialist should approve specialist when found")
        void acceptSpecialist_whenFound_shouldSetStatusToApproved() {
            // Arrange
            specialist.setSpecialistStatus(SpecialistStatus.NEW); // Set initial status
            when(specialistRepo.findById(1L)).thenReturn(Optional.of(specialist));

            // Act
            adminService.acceptSpecialist(1L);

            // Assert
            verify(specialistRepo).findById(1L);
            verify(specialistRepo).save(specialist);
            assertEquals(SpecialistStatus.APPROVED, specialist.getSpecialistStatus());
        }




        @Test
        @DisplayName("addingSpecialistToSubService should throw exception if specialist is not approved")
        void addingSpecialistToSubService_whenNotApproved_shouldThrowException() {
            // Arrange
            specialist.setSpecialistStatus(SpecialistStatus.NEW);
            when(specialistRepo.findById(1L)).thenReturn(Optional.of(specialist));

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () ->
                    adminService.addingSpecialistToSubService(1L, 10L)
            );
            assertEquals("Specialist is not approved", exception.getMessage());
        }
    }


    @Nested
    @DisplayName("Service Delegation Tests")
    class ServiceDelegationTests {

        @Test
        @DisplayName("createNewService should delegate to serviceService")
        void createNewService_shouldDelegate() {
            // Arrange
            ServiceRequest request = new ServiceRequest("Gardening", "Garden maintenance", 60.0f, null, true, null);
            ServiceResponse response = new ServiceResponse(1L, "Gardening", "Garden maintenance", 60.0f, null, true, null, null);
            when(serviceService.save(request)).thenReturn(Optional.of(response));

            // Act
            Optional<ServiceResponse> result = adminService.createNewService(request);

            // Assert
            assertTrue(result.isPresent());
            assertEquals("Gardening", result.get().name());
            verify(serviceService).save(request);
        }

        @Test
        @DisplayName("addSpeciliast should delegate to speciallistService")
        void addSpeciliast_shouldDelegate() {
            // Arrange
            when(speciallistService.save(specialistRequest)).thenReturn(Optional.of(specialistResponse));

            // Act
            Optional<SpecialistResponse> result = adminService.addSpeciliast(specialistRequest);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(specialistResponse.id(), result.get().id());
            verify(speciallistService).save(specialistRequest);
        }

        @Test
        @DisplayName("findAllServices should delegate to serviceService")
        void findAllServices_shouldDelegate() {
            // Arrange
            List<ServiceResponse> responses = List.of(new ServiceResponse(1L, "Plumbing", "Pipe repair", 80.0f, null, true, null, null));
            when(serviceService.findAll()).thenReturn(Optional.of(responses));

            // Act
            Optional<List<ServiceResponse>> result = adminService.findAllServices();

            // Assert
            assertTrue(result.isPresent());
            assertEquals(1, result.get().size());
            verify(serviceService).findAll();
        }
    }
}