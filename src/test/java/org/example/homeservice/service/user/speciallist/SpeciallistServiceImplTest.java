package org.example.homeservice.service.user.speciallist;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpeciallistServiceImplTest {

    @Mock
    private SpecialistRepo specialistRepo;
    @Mock
    private SpecialistMapper specialistMapper;
    @Mock
    private OrderService orderService;
    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private SpeciallistServiceImpl speciallistService;

    private Specialist specialist;
    private SpecialistResponse specialistResponse;
    private SpecialistRequest specialistRequest;

    @BeforeEach
    void setUp() {
        specialist = new Specialist();
        specialist.setId(1L);
        specialist.setEmail("test@example.com");
        specialist.setSpecialistStatus(SpecialistStatus.NEW);
        specialist.setIsActive(false);

        specialistRequest = new SpecialistRequest(null, "John", "Doe", "john.doe@example.com", "password", SpecialistStatus.NEW, 0.0, null);
        specialistResponse = new SpecialistResponse(1L, "John", "Doe", "john.doe@example.com", SpecialistStatus.APPROVED, 4.5, 10, null, 101L, true);
    }

    @Nested
    @DisplayName("Status and Activation Tests")
    class StatusTests {
        @Test
        @DisplayName("acceptSpecialist should set status to APPROVED")
        void acceptSpecialist_shouldSucceed() {
            when(specialistRepo.findById(1L)).thenReturn(Optional.of(specialist));
            when(specialistRepo.save(any(Specialist.class))).thenReturn(specialist);
            when(specialistMapper.toDto(any(Specialist.class))).thenReturn(specialistResponse);

            speciallistService.acceptSpecialist(1L);

            ArgumentCaptor<Specialist> specialistCaptor = ArgumentCaptor.forClass(Specialist.class);
            verify(specialistRepo).save(specialistCaptor.capture());
            assertEquals(SpecialistStatus.APPROVED, specialistCaptor.getValue().getSpecialistStatus());
        }

        @Test
        @DisplayName("activateSpecialist should set isActive to true")
        void activateSpecialist_shouldSucceed() {
            when(specialistRepo.findById(1L)).thenReturn(Optional.of(specialist));
            when(specialistMapper.toDto(any(Specialist.class))).thenReturn(specialistResponse);

            speciallistService.activateSpecialist(1L);

            assertTrue(specialist.getIsActive());
        }
    }

    @Nested
    @DisplayName("Data Retrieval Tests")
    class RetrievalTests {
        @Test
        @DisplayName("showReviews should delegate to ReviewService")
        void showReviews_shouldDelegate() {
            speciallistService.showReviews(1L);
            verify(reviewService).getRatingsBySpecialistId(1L);
        }

        @Test
        @DisplayName("getAvailableOrders should delegate to OrderService")
        void getAvailableOrders_shouldDelegate() {
            speciallistService.getAvilableOrders(1L);
            verify(orderService).findWaitingOrdersBySpecialist(1L);
        }

        @Test
        @DisplayName("filterSpecialists should call repository with a specification")
        void filterSpecialists_shouldCallRepoWithSpec() {
            speciallistService.filterSpecialists("John", null, null, null, "id", true, null, null, null, null);
            verify(specialistRepo).findAll(any(Specification.class), any(org.springframework.data.domain.Sort.class));
        }
    }
}