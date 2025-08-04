package org.example.homeservice.service.baseentity;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.BaseEntity;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseEntityServiceImplTest {


    static class TestEntity extends BaseEntity<Long> {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    record TestEntityRequestDto(String name) {}

    record TestEntityResponseDto(Long id, String name) {}

    interface TestEntityRepo extends BaseEnitityRepo<TestEntity, Long> {}


    static class TestEntityServiceImpl extends BaseEntityServiceImpl<TestEntity, Long, TestEntityRepo, TestEntityRequestDto, TestEntityResponseDto> {

        public TestEntityServiceImpl(TestEntityRepo baseRepository) {
            super(baseRepository);
        }

        @Override
        protected TestEntity toEntity(TestEntityRequestDto dto) {
            TestEntity entity = new TestEntity();
            entity.setName(dto.name());
            return entity;
        }

        @Override
        protected TestEntityResponseDto toDto(TestEntity entity) {
            return new TestEntityResponseDto(entity.getId(), entity.getName());
        }
    }

    @Mock
    private TestEntityRepo mockRepo;

    @InjectMocks
    private TestEntityServiceImpl service;

    private TestEntity testEntity;
    private TestEntityRequestDto testRequestDto;
    private TestEntityResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        testRequestDto = new TestEntityRequestDto("Test Name");

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("Test Name");

        testResponseDto = new TestEntityResponseDto(1L, "Test Name");
    }

    @Nested
    @DisplayName("Save and Update Operations")
    class SaveUpdateTests {
        @Test
        @DisplayName("save should correctly convert DTO, save entity, and return response DTO")
        void save_shouldWorkCorrectly() {
            // Arrange
            when(mockRepo.save(any(TestEntity.class))).thenReturn(testEntity);

            // Act
            Optional<TestEntityResponseDto> result = service.save(testRequestDto);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testResponseDto, result.get());
            verify(mockRepo).save(any(TestEntity.class));
        }

        @Test
        @DisplayName("save should throw IllegalArgumentException for null DTO")
        void save_whenDtoIsNull_shouldThrowException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> service.save(null));
        }

        @Test
        @DisplayName("update should function like save")
        void update_shouldWorkCorrectly() {
            // Arrange
            when(mockRepo.save(any(TestEntity.class))).thenReturn(testEntity);

            // Act
            Optional<TestEntityResponseDto> result = service.update(testRequestDto);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testResponseDto, result.get());
            verify(mockRepo).save(any(TestEntity.class));
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindTests {
        @Test
        @DisplayName("findById should return DTO when entity exists")
        void findById_whenExists_shouldReturnDto() {
            // Arrange
            when(mockRepo.findById(1L)).thenReturn(Optional.of(testEntity));

            // Act
            Optional<TestEntityResponseDto> result = service.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testResponseDto, result.get());
        }

        @Test
        @DisplayName("findById should throw ValidationException when entity does not exist")
        void findById_whenNotExists_shouldThrowException() {
            // Arrange
            when(mockRepo.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> service.findById(99L));
            assertTrue(exception.getMessage().contains("with  : 99 not found!"));
        }

        @Test
        @DisplayName("findAll should return a list of DTOs")
        void findAll_shouldReturnListOfDtos() {
            // Arrange
            when(mockRepo.findAll()).thenReturn(List.of(testEntity));

            // Act
            Optional<List<TestEntityResponseDto>> result = service.findAll();

            // Assert
            assertTrue(result.isPresent());
            assertEquals(1, result.get().size());
            assertEquals(testResponseDto, result.get().get(0));
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteTests {
        @Test
        @DisplayName("deleteById should return true when entity exists")
        void deleteById_whenExists_shouldReturnTrue() {
            // Arrange
            when(mockRepo.findById(1L)).thenReturn(Optional.of(testEntity));
            // No need to mock deleteById as it's a void method

            // Act
            boolean result = service.deleteById(1L);

            // Assert
            assertTrue(result);
            verify(mockRepo).deleteById(1L);
        }

        @Test
        @DisplayName("deleteById should return false when entity does not exist")
        void deleteById_whenNotExists_shouldReturnFalse() {
            // Arrange
            when(mockRepo.findById(99L)).thenReturn(Optional.empty());

            // Act
            boolean result = service.deleteById(99L);

            // Assert
            assertFalse(result);
            verify(mockRepo, never()).deleteById(99L);
        }

        @Test
        @DisplayName("delete by entity should delegate to repository")
        void delete_shouldDelegateToRepo() {
            // Act
            boolean result = service.delete(testEntity);

            // Assert
            assertTrue(result);
            verify(mockRepo).delete(testEntity);
        }
    }
}