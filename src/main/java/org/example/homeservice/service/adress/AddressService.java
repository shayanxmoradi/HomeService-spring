package org.example.homeservice.service.adress;

import org.example.homeservice.dto.AddressReqest;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.entity.Address;
import org.example.homeservice.entity.Order;
import org.example.homeservice.service.baseentity.BaseEntityService;

public interface AddressService extends BaseEntityService<Address,Long, AddressReqest, AddressResponse> {
}
