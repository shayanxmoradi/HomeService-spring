package org.example.homeservice.service.adress;

import org.example.homeservice.dto.AddressReqest;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.domain.Address;
import org.example.homeservice.service.baseentity.BaseEntityService;

public interface AddressService extends BaseEntityService<Address,Long, AddressReqest, AddressResponse> {
}
