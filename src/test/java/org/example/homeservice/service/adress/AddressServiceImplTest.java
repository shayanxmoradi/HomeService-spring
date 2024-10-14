package org.example.homeservice.service.adress;

import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.address.AddressMapper;
import org.example.homeservice.domain.Address;
import org.example.homeservice.repository.address.AddressRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @Mock
    private AddressRepo addressRepo;


    @Spy
    private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
//    @Mock
//    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;
    private AddressReqest addressReqest;
    private AddressResponse addressResponse;
    @BeforeEach
    void setUp() {

        address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");

        addressReqest = new AddressReqest("123 Main St", "City", "State", "12345",2l);
        addressResponse = new AddressResponse("123 Main St", "123 Main St", "City", "State", 2l);
    }

//    @Test
//    void testSave() {
//        when(addressMapper.toEntity(any(AddressReqest.class))).thenReturn(address);
//        when(addressMapper.toResponseDto(any(Address.class))).thenReturn(addressResponse);
//        when(addressRepo.save(any(Address.class))).thenReturn(address);
//
//        Optional<AddressResponse> result = addressService.save(addressReqest);
//
//        verify(addressRepo, times(1)).save(any(Address.class));
//        verify(addressMapper, times(1)).toEntity(any(AddressReqest.class));
//        verify(addressMapper, times(1)).toResponseDto(any(Address.class));
//
//        assertEquals(addressResponse, result);
//    }

    @Test
    void testFindById() {
        when(addressRepo.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toResponseDto(address)).thenReturn(addressResponse);

        Optional<AddressResponse> result = addressService.findById(1L);

        verify(addressRepo, times(1)).findById(1L);
        assertEquals(Optional.of(addressResponse), result);
    }

    @Test
    void testFindById_NotFound() {
        when(addressRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<AddressResponse> result = addressService.findById(1L);

        verify(addressRepo, times(1)).findById(1L);
        assertEquals(Optional.empty(), result);
    }
}