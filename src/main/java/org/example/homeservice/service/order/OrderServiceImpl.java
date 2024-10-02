package org.example.homeservice.service.order;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.*;
import org.example.homeservice.dto.mapper.AddressMapper;
import org.example.homeservice.dto.mapper.OfferMapper;
import org.example.homeservice.dto.mapper.OrderMapper;
import org.example.homeservice.entity.Address;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.enums.OrderStatus;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends BaseEntityServiceImpl<Order, Long, OrderRepo, OrderRequest, OrderResponse> implements OrderService {
   private  CustomerService customerService;
   private final ServiceService serviceService;
   private final AddressService addressService;
   private final OrderMapper orderMapper;
   private final AddressMapper addressMapper;
   private final OfferService offerService;
   private final OfferMapper offerMapper;
    @Autowired
    public OrderServiceImpl(OrderRepo baseRepository, ServiceService serviceService, AddressService addressService, OrderMapper orderMapper, AddressMapper addressMapper, OfferService offerService, OfferMapper offerMapper) {
        super(baseRepository);
        this.serviceService = serviceService;
        this.addressService = addressService;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
        this.offerService = offerService;
        this.offerMapper = offerMapper;
    }
    @Autowired
    public void setCustomerService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }



    @Override
    public Optional<OrderResponse> save(OrderRequest orderRequest) {
        if (customerService.findById(orderRequest.customerId()).isEmpty()) {
            throw new ValidationException("Customer with this id not found");
        }
        Optional<ServiceResponse> foundService = serviceService.findById(orderRequest.serviceId());
        if (foundService.isEmpty()) {
            throw new ValidationException("Order cannot be null");
        } else if (foundService.get().category()==true) {
            throw new ValidationException("chosenService is not really service its just as category for other services");

        }
        Optional<AddressResponse> foundedAddress = addressService.findById(orderRequest.addressId());
        if (foundedAddress.isEmpty()) {
            throw new ValidationException("no address with this id found");
        }
        if (foundService.get().basePrice()> orderRequest.offeredPrice()) throw new ValidationException("base price is greater than offered price");

//todo saving in customer side
        //todo tekraari


      return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(orderMapper.toEntity(orderRequest))));
    }

    @Override
    protected OrderResponse toDto(Order entity) {
        return orderMapper.toResponse(entity);
    }

    @Override
    protected Order toEntity(OrderRequest dto) {
        return orderMapper.toEntity(dto);
    }

    @Override
    public List<OrderResponse> findWaitingForOfferAndSpecialist() {

        return orderMapper.toListOfResponse(baseRepository.findByStatusIn(Arrays.asList(OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS,OrderStatus.WAITING_FOR_SPECIALISTS)));
    }

    @Override
    public List<OrderResponse> findByCustomerId(Long customerId) {
      return   orderMapper.toListOfResponse( baseRepository.findByCustomerId(customerId));
    }

    @Transactional
    @Override
    public List<OrderResponse> findWaitingOrdersBySpecialist(Long specialistId) {
        return orderMapper.toListOfResponse(baseRepository.findWaitingOrdersBySpecialist(specialistId));
    }




    //    @Override
//    public Optional<OrderResponse> choseOrder(Long orderId,Long chosenOfferId) {
//        Optional<Order> foundedOrder = baseRepository.findById(orderId);
//        if (foundedOrder.isEmpty()) {
//            throw new ValidationException("no order with this id found");
//        }
//        Optional<OfferResponse> foundedResponse = offerService.findById(chosenOfferId);
//        if (foundedResponse.isEmpty()) {
//            throw new ValidationException("no offer with this id found");
//        }
//        boolean isChosableOrder = foundedOrder.get().getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS || foundedOrder.get().getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS;
//        if (isChosableOrder) {
//            foundedOrder.get().setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY);
//            foundedOrder.get().setChosenOffer(offerMapper.toEnity(foundedResponse.get()));
//          return   save(orderMapper.toDto(foundedOrder.get()));
//
//        }else throw new ValidationException("order is not in status wich you can chose it");
//
//    }

//
//    @Override
//    public Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId) {
//        Order foundedOrder=   baseRepository.findById(orderId).get();
//      //  Order foundedOrder = orderMapper.toEntity(findById(orderId).get());
//        //foundedOrder.setId(1l);
//        System.out.println("hereee");
//        //todo why addreess is null
//        //System.out.println(foundedOrder);
////        //todo check there
//////        if (foundedOrder.isEmpty()) {
//////            throw new ValidationException("no order with this id found");
//////        }
//        Optional<OfferResponse> foundedResponse = offerService.findById(chosenOfferId);
//        if (foundedResponse.isEmpty()) {
//            throw new ValidationException("no offer with this id found");
//        }
//     boolean isChosableOrder = foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS || foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS;
//        if (isChosableOrder) {
//            foundedOrder.setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY);
//            foundedOrder.setChosenOffer(offerMapper.toEnity(foundedResponse.get()));
//
//            return save(orderMapper.toDto(foundedOrder));
//            //return null;
//        } else throw new ValidationException("order is not in status wich you can chose it");
//    }


    @Override
    public Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId) {
        // Validate that the IDs are not null
        if (orderId == null || chosenOfferId == null) {
            throw new IllegalArgumentException("Order ID and chosen offer ID must not be null");
        }

        // Find the order by ID, throwing an exception if not found
        Order foundedOrder = baseRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("No order with this ID found"));

        // Find the offer by ID, throwing an exception if not found
        Optional<OfferResponse> foundedResponse = offerService.findById(chosenOfferId);
        if (foundedResponse.isEmpty()) {
            throw new ValidationException("No offer with this ID found");
        }

        // Check if the order is in a status where it can be chosen
        boolean isChosableOrder = foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS
                                  || foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS;

        if (isChosableOrder) {
            // Update the order status and set the chosen offer
            foundedOrder.setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY);
            foundedOrder.setChosenOffer(offerMapper.toEnity(foundedResponse.get()));

            // Save the updated order and return it
           // return save(orderMapper.toDto(foundedOrder));

            System.out.println("xxxx");
           // OrderRequest orderRequest = orderMapper.toDto(foundedOrder);
         //   System.out.println(orderRequest);

            return update(foundedOrder);
        } else {
            throw new ValidationException("Order is not in a status where you can choose it");
        }
    }


    public Optional<OrderResponse> update(Order dto) {
return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(dto)));
    }
}
