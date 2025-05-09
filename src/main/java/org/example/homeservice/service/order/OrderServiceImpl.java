package org.example.homeservice.service.order;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.offer.OfferMapper;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.domain.Order;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.dto.order.OrderMapper;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.order.OrdeSpecificaiton;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.service.WalletService;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends BaseEntityServiceImpl<Order, Long, OrderRepo, OrderRequest, OrderResponse> implements OrderService {
    private CustomerService customerService;
    private final ServiceService serviceService;
    private final AddressService addressService;
    private final OrderMapper orderMapper;
    private final OfferService offerService;
    private final OfferMapper offerMapper;
    private SpeciallistService speciallistService;
    private final SpecialistMapper specialistMapper;
    private final WalletService walletService;

    @Autowired
    public OrderServiceImpl(OrderRepo baseRepository, ServiceService serviceService, AddressService addressService, OrderMapper orderMapper, OfferService offerService, OfferMapper offerMapper, SpecialistMapper specialistMapper, @Lazy WalletService walletService) {
        super(baseRepository);
        this.serviceService = serviceService;
        this.addressService = addressService;
        this.orderMapper = orderMapper;
        this.offerService = offerService;
        this.offerMapper = offerMapper;
        this.specialistMapper = specialistMapper;
        this.walletService = walletService;
    }


    @Autowired
    public void setSpeciallistService(@Lazy SpeciallistService speciallistService) {
        this.speciallistService = speciallistService;
    }

    @Autowired
    public void setCustomerService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public Optional<OrderResponse> save(OrderRequest orderRequest) {
        boolean isDuplicateOrder= isDuplicatedOrder(orderRequest);
        if (isDuplicateOrder) {
            throw new ValidationException("this order already exists");
        }
        if (customerService.findById(orderRequest.customerId()).isEmpty()) {
            throw new ValidationException("Customer with this id not found");
        }
        Optional<ServiceResponse> foundService = serviceService.findById(orderRequest.serviceId());
        if (foundService.isEmpty()) {
            throw new ValidationException("no servicee with this id : " + orderRequest.serviceId() + " found .");
        } else if (foundService.get().category() ) {
            throw new ValidationException("chosenService is not really service its just as category for other services");

        }
        Optional<AddressResponse> foundedAddress = addressService.findById(orderRequest.addressId());
        if (foundedAddress.isEmpty()) {
            throw new ValidationException("no address with this id found");
        }
        if (foundService.get().basePrice() > orderRequest.offeredPrice())
            throw new ValidationException("base price is greater than offered price");


        return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(orderMapper.toEntity(orderRequest))));
    }

    private boolean isDuplicatedOrder(OrderRequest orderRequest) {
        Long serviceId = orderRequest.serviceId();
        Long customerId = orderRequest.customerId();
        LocalDateTime serviceTime = orderRequest.serviceTime();
        return baseRepository.findOrderByCustomerIdAndChoosenServiceIdAndServiceTime(customerId, serviceId, serviceTime).isPresent();
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

        return orderMapper.toListOfResponse(baseRepository.findByStatusIn(Arrays.asList(OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS, OrderStatus.WAITING_FOR_SPECIALISTS)));
    }

    @Override
    public List<OrderResponse> findByCustomerId(Long customerId) {
        return orderMapper.toListOfResponse(baseRepository.findByCustomerId(customerId));
    }

    @Override
    public List<OrderResponse> findByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus) {
        return orderMapper.toListOfResponse(baseRepository.findByCustomerIdAndStatus(customerId,orderStatus));
    }

    @Override
    public List<OrderResponse> findBySpecialistId(Long specialistId) {
        return orderMapper.toListOfResponse(baseRepository.findByChosenSpecialistId(specialistId));
    }

    @Override
    public List<OrderResponse> findBySpecialistIdAndStatus(Long specialistId, OrderStatus orderStatus) {
        return orderMapper.toListOfResponse(baseRepository.findByChosenSpecialistIdAndStatus(specialistId,orderStatus));
    }

    @Transactional
    @Override
    public List<OrderResponse> findWaitingOrdersBySpecialist(Long specialistId) {
        return orderMapper.toListOfResponse(baseRepository.findWaitingOrdersBySpecialist(specialistId));
    }


    @Override
    public Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId) {
        if (orderId == null || chosenOfferId == null) {
            throw new IllegalArgumentException("Order ID and chosen offer ID must not be null");
        }

        Order foundedOrder = baseRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("No order with this ID found"));

        Optional<OfferResponse> foundedResponse = offerService.findById(chosenOfferId);

        if (foundedResponse.isEmpty()) {
            throw new ValidationException("No offer with this ID found");
        }

        boolean isChosableOrder = foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS
                                  || foundedOrder.getStatus() == OrderStatus.WAITING_FOR_SPECIALISTS;

        if (isChosableOrder) {
            foundedOrder.setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY);
//            System.out.println(offerMapper.toEnity(foundedResponse.get()));
            foundedOrder.setChosenOffer(offerMapper.toEnity(foundedResponse.get()));
            Long specialistId = foundedResponse.get().specialistId();
            SpecialistResponse specialistResponse = speciallistService.findById(specialistId).get();
            foundedOrder.setChosenSpecialist(specialistMapper.toEntity(specialistResponse));
            foundedOrder.setAcceptedPrice(foundedResponse.get().suggestedPrice());

            System.out.println("output order :");
            System.out.println(foundedOrder.getChoosenService().getId());

//return null;
            return update(foundedOrder);
        } else {
            throw new ValidationException("Order is not in a status where you can choose it");
        }
    }

    public List<OrderResponse> getOrdersByCriteria(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status, Long serviceId, Long subserviceId) {
        Specification<Order> spec = Specification.where(OrdeSpecificaiton.betweenDates(startDate, endDate))
                .and(OrdeSpecificaiton.hasStatus(status))
                .and(OrdeSpecificaiton.hasService(serviceId))
                .and(OrdeSpecificaiton.hasSubservice(subserviceId));

        return orderMapper.toListOfResponse(baseRepository.findAll(spec));
    }

    @Transactional
    @Override
    public Optional<OrderResponse> startOrder(Long orderId) {

        OrderStatus status = OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY;
        Order foundedOrder = checkOrderStatus(orderId, status);

        LocalDateTime offeredTimeToStart = foundedOrder.getChosenOffer().getOfferedTimeToStart();
        if (!isBeforeCurrentTime(offeredTimeToStart)) {
            throw new ValidationException("you are not allowed to start order before offered start time by specialist");
        }
        foundedOrder.setOrderStartedAt(LocalDateTime.now());
        foundedOrder.setStatus(OrderStatus.BEGAN);
        //return update(foundedOrder);
        return Optional.ofNullable(orderMapper.toResponse(foundedOrder));
    }


    @Transactional
    @Override
    public Optional<OrderResponse> endOrder(Long orderId) {
        OrderStatus status = OrderStatus.BEGAN;
        Order foundedOrder = checkOrderStatus(orderId, status);
        LocalDateTime orderStartedAt = foundedOrder.getOrderStartedAt();
        Duration estimatedDuration = foundedOrder.getChosenOffer().getEstimatedDuration();
        LocalDateTime estimatedTimeToEnd = orderStartedAt.plus(estimatedDuration);
        if (!isBeforeCurrentTime(estimatedTimeToEnd)) {
            throw new ValidationException("you are not allowed to end order before estimated duration");
        }
        foundedOrder.setStatus(OrderStatus.DONE);
        return Optional.ofNullable(orderMapper.toResponse(foundedOrder));

//        return update(foundedOrder);
    }

    @Override
    public void deleteByServiceId(Long serviceId) {
        baseRepository.deleteByServiceId(serviceId);
    }

    @Override
    public void updateOrdersWithNullService(Long serviceId) {
        baseRepository.updateOrdersWithNullService(serviceId);
    }

    @Transactional
    @Override
    public Optional<OrderResponse> onlinePayment(Long orderId,OrderStatus orderStatus) {
        try {
            OrderStatus status = OrderStatus.DONE;
            Order foundedOrder = checkOrderStatus(orderId, status);

            foundedOrder.setStatus(orderStatus);
//todo wtf


            //add 70% money to specilist wallet
            SpecialistResponse specialistResponse = speciallistService.findById(foundedOrder.getChosenSpecialist().getId()).orElseThrow(() -> new ValidationException("No specialist with this ID found"));
            Long walletId = specialistResponse.walletId();
            double specilistProfit = foundedOrder.getAcceptedPrice() * 0.7;
            walletService.addMoneyToWallet(walletId, specilistProfit);


            return Optional.ofNullable(orderMapper.toResponse(foundedOrder));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    @Override
    public Optional<OrderResponse> setOnlinePaied(Long orderId) {
        OrderStatus status = OrderStatus.DONE;
        Order foundedOrder = checkOrderStatus(orderId, status);
        System.out.println(foundedOrder);

        foundedOrder.setStatus(OrderStatus.PAID);
        return Optional.ofNullable(orderMapper.toResponse(foundedOrder));

    }

    @Override
    public List<Order> findByAdrressId(Long adrressId) {
        return baseRepository.findOrderByAddressId(adrressId);
    }


    public Optional<OrderResponse> update(Order dto) {
        return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(dto)));
    }

    public boolean isBeforeCurrentTime(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return dateTime.isBefore(currentDateTime);
    }

    private Order checkOrderStatus(Long orderId, OrderStatus status) {
        Order foundedOrder = baseRepository.findById(orderId).orElseThrow(() -> new ValidationException("No order with this ID found"));
        if (foundedOrder.getStatus() != status) {
            throw new ValidationException("Order should be in status :" + status);
        }
        return foundedOrder;
    }

}
