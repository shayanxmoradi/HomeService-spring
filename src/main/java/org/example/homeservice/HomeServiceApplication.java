package org.example.homeservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.homeservice.domain.*;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.customer.CustomerMapper;
import org.example.homeservice.dto.offer.OfferMapper;
import org.example.homeservice.dto.offer.OfferRequest;
import org.example.homeservice.dto.order.OrderMapper;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceMapper;
import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.service.admin.AdminService;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableCaching
public class HomeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(ServiceService serviceService) {
        return args -> {
            Service addServiceDto = new Service();
            addServiceDto.setId(164l);
            addServiceDto.setName("Add Servicex");
            addServiceDto.setDescription("Add Service Description");
            addServiceDto.setCategory(true);
            //serviceService.save(addServiceDto);

        };
    }
    @Bean
    CommandLineRunner delteService(ServiceService serviceService) {
        return args -> {
         //   serviceService.deleteById(2002l);
        };
    }

    @Bean
    CommandLineRunner createCustomer(CustomerService customerService, CustomerMapper customerMapper) {
        return args -> {
            Customer customer = new Customer();

            customer.setFirstName("Joxxhn");
            customer.setLastName("Doex");
            customer.setEmail("john.dxxdoedsss@example.com");
            customer.setPassword("12345678");

     //  customerService.save(customerMapper.toDto(customer));
        };
    }

    @Bean
    CommandLineRunner createSpealist(SpeciallistService speciallistService, SpecialistMapper specialistMapper) {
        return args -> {
//todo give Specialrequest or convert it? give image like this or give path to request and convert it
            {
//             SpecialistRequest specialist = new SpecialistRequest("shayan",
//                     "moradi",
//                     "shay@gmai.com",
//                     "1234ghuy",
//                     null,
//                     0.0,
//                     null
//                     );
//             speciallistService.save(specialist);
//            specialist.setId(1l);
            }
            Specialist specialist = new Specialist();
            specialist.setFirstName("John");
            specialist.setLastName("Doe");
            specialist.setEmail("jodhss2xs2n.do22e@example.com");
            specialist.setPassword("1234123s");
//specialist.setSpecialistStatus();//todo defalut status
//            specialist.setPersonalImage(speciallistService.processImage("/Users/shayan/Desktop/x.jpg"));

          //  speciallistService.save(specialistMapper.toDtoReq(specialist));

            //speciallistService.retriveImageOfSpecialist(802l,"/Users/shayan/Desktop/savedx.jpg");
        };
    }

    @Bean
    CommandLineRunner acceptSpecialist(AdminService adminService, SpecialistMapper specialistMapper) {
        return args -> {
//adminService.acceptSpecialist(1002L);

        };
    }

    @Bean
    CommandLineRunner adddingSpecialistToService(AdminService adminService, SpecialistMapper specialistMapper) {
        return args -> {
         //  adminService.addingSpecialistToSubService(1102L,1352l);
//todo FIIIIIIIIIIXXXXXX
        };
    }

    @Bean
    CommandLineRunner updatePassword(CustomerService customerService, SpecialistMapper specialistMapper) {
        return args -> {
            //  customerService.updatePassword(new UpdatePasswordRequst("john.doe@example.com","XXXXXX78","XXXXzz78"));
        };
    }


    @Bean
    CommandLineRunner updatePasswords(SpeciallistService speciallistService, SpecialistMapper specialistMapper) {
        return args -> {
            //  speciallistService.updatePassword(new UpdatePasswordRequst("jodhss2xs2n.do22e@example.com","1234123s","xxxx7654"));
        };
    }

    @Bean
    CommandLineRunner createService(ServiceService serviceService, ServiceMapper serviceMapper) {

        return args -> {
            Long parentServiceId = 1052l;
//todo if parent already not isCategory cant be parent
            ServiceRequest serviceRequest = new ServiceRequest(
                    "kitchen",
                    "fast and ez",
                    222.2f,
                    parentServiceId,
                    false,
                    null
            );
    //     serviceService.save(serviceRequest);
        };


    }

    @Bean
    CommandLineRunner admincreateService(AdminService adminService, ServiceMapper serviceMapper) {

        return args -> {
            Long parentServiceId = 1702l;
//todo if parent already not isCategory cant be parent
            ServiceRequest serviceRequest = new ServiceRequest(
                    "kitchen",
                    "asdf",
                    12.2f,
                    parentServiceId,
                    false,
                    null
            );
         //   adminService.createNewService(serviceRequest);
        };
    }

    @Bean
    CommandLineRunner findService(ServiceService serviceService, ServiceMapper serviceMapper) {
        return args -> {
            List<ServiceResponse> serviceResponses = serviceService.findAll().get();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(serviceResponses);
            //  System.out.println(json);
        };
    }

    @Bean
    CommandLineRunner findServiceByAdmin(AdminService adminService, ServiceMapper serviceMapper) {
        return args -> {
            List<ServiceResponse> serviceResponses = adminService.findAllServices().get();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(serviceResponses);
          //  System.out.println(json);
        };
    }

    @Bean
    CommandLineRunner createAddress(AddressService addressService, ServiceMapper serviceMapper) {
        return args ->
        {
//            Address address = new Address();
//            address.setCity("San Francisco");
//            address.setState("CA");
//            address.setZip("12345");
            AddressReqest addressReqest = new AddressReqest(
                    "emil moog",
                    "dortmund",
                    "NRW",
                    "12345",
                    952l);
            //  addressService.save(addressReqest);
        };
    }

    @Bean
    CommandLineRunner registerOrder(OrderService orderService, OrderMapper orderMapper) {
        return args -> {
            OrderRequest order = new OrderRequest(
                    952l,
                    1102l,
                    52l,
                    "its hould be claen",
                    LocalDateTime.now().plusDays(2),
                    33333.21
            );
          //  orderService.save(order);
        };
    }
    @Bean
    CommandLineRunner seeActiveOrders(OrderService orderService, OrderMapper orderMapper) {
        return args -> {
            System.out.println(orderService.findWaitingForOfferAndSpecialist());
        };
    }
    @Bean
    CommandLineRunner getrelatedOrderToSpecialist(OrderService orderService, OrderMapper orderMapper) {
        return args -> {
         //   System.out.println("here");
          //  orderService.findWaitingOrdersBySpecialist(1002l).forEach(a -> System.out.println(a.serviceId()));
        };
    }

    @Bean
    CommandLineRunner registerOffer(OfferService offerService, OfferMapper offerMapper) {
        return args -> {
            //todo change
            OfferRequest offer= new OfferRequest(LocalDateTime.now().plusDays(1),2000.2,
                    4402l, java.time.Duration.ofHours(2),1102l);

//                    LocalDate.now().plusDays(2), LocalTime.now().plusHours(2),
//                    4444.2,2552l ,
//                    1102l,
//                    5,1, LocalDate.now().plusDays(2),22,1002l);
           // offerService.save(offer);
        };
    }

    @Bean
    CommandLineRunner findOffers(OfferService offerService, OfferMapper offerMapper) {
        return args -> {
            System.out.println("findOffers");
//            System.out.println(offerService.findOfferByOrderId(2552l));
        };
    }
    @Bean
    CommandLineRunner choseOffer(OrderService orderService, OfferMapper offerMapper) {
        return args -> {
//      orderService.choseOrder(4452l,1902l);
        };
    }    @Bean
    CommandLineRunner startOrder(OrderService orderService, OfferMapper offerMapper) {
        return args -> {
//            Optional<OrderResponse> savingResponse = orderService.startOrder(4452l);
        };
    }


    @Bean
    CommandLineRunner spefilter(SpeciallistService speciallistService, OfferMapper offerMapper) {
        return args -> {
//speciallistService.filterSpecialists("","","","rate",true);
        };
    }
}