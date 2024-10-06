package org.example.homeservice.service.baseentity;

import org.example.homeservice.domain.BaseEntity;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseEntityServiceImplTest {
    @Mock
    private BaseEnitityRepo<BaseEntity<Long>, Long> baseRepository;

    @InjectMocks
    private BaseEntityServiceImpl<BaseEntity<Long>, Long, BaseEnitityRepo<BaseEntity<Long>, Long>, Object, Object> baseEntityService;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        Object dto = new Object(); // Mock or create a DTO object
        BaseEntity<Long> entity = mock(BaseEntity.class); // Mock entity object
        when(baseEntityService.toEntity(dto)).thenReturn(entity); // Mock toEntity conversion
        when(baseRepository.save(entity)).thenReturn(entity); // Mock repository save
        when(baseEntityService.toDto(entity)).thenReturn(new Object()); // Mock toDto conversion

        // Act
        Optional<Object> result = baseEntityService.save(dto);

        // Assert
        assertTrue(result.isPresent());
        verify(baseRepository, times(1)).save(entity);
    }
//
//    @Test
//    void testFindById() {
//        // Arrange
//        Long id = 1L;
//        BaseEntity<Long> entity = mock(BaseEntity.class);
//        when(baseRepository.findById(id)).thenReturn(Optional.of(entity));
//        when(baseEntityService.toDto(entity)).thenReturn(new Object());
//
//        // Act
//        Optional<Object> result = baseEntityService.findById(id);
//
//        // Assert
//        assertTrue(result.isPresent());
//        verify(baseRepository, times(1)).findById(id);
//    }

    @Test
    void testDeleteById() {

        Long id = 1L;

        boolean result = baseEntityService.deleteById(id);

        assertTrue(result);
        verify(baseRepository, times(1)).deleteById(id);
    }
}
