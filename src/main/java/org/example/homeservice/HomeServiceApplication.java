package org.example.homeservice;

import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.UpdatePasswordRequst;
import org.example.homeservice.dto.mapper.CustomerMapper;
import org.example.homeservice.dto.mapper.ServiceMapper;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.entites.Customer;
import org.example.homeservice.entites.Service;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.BaseUserService;
import org.example.homeservice.service.user.CustomerService;
import org.example.homeservice.service.user.SpeciallistService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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
    CommandLineRunner createCustomer(CustomerService customerService, CustomerMapper customerMapper) {
        return args -> {

            Customer customer = new Customer();
            customer.setId(1l);
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setEmail("john.doe@example.com");
            customer.setPassword("1234123s");

            //customerService.save(customerMapper.toDtoReq(customer));
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
            specialist.setPersonalImage(speciallistService.processImage("/Users/shayan/Desktop/x.jpg"));

            //  speciallistService.save(specialistMapper.toDtoReq(specialist));

            //speciallistService.retriveImageOfSpecialist(802l,"/Users/shayan/Desktop/savedx.jpg");
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
            Long parentServiceId = 902l;
//todo if parent already not category cant be parent
            ServiceRequest serviceRequest = new ServiceRequest(
                    "neverxmore",
                    "asdf",
                    12.2f,
                    parentServiceId,
                    false,
                    null
            );

            serviceService.save(serviceRequest);
        };

    }
}