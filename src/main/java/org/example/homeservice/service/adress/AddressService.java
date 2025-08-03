package org.example.homeservice.service.adress;

import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.domain.user.Address;
import org.example.homeservice.service.baseentity.BaseEntityService;

public interface AddressService extends BaseEntityService<Address,Long, AddressReqest, AddressResponse> {
}
