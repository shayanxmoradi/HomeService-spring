package org.example.homeservice.service.user.speciallist;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.example.homeservice.exception.FileNotFoundException;
import org.example.homeservice.exception.ImageTooLargeException;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.dto.review.SpecialistRateRespone;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.repository.service.ServiceRepo;

import org.example.homeservice.repository.user.SpecialistSpecification;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.review.ReviewService;
import org.example.homeservice.service.user.BaseUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class SpeciallistServiceImpl extends BaseUserServiceImpl<Specialist, SpecialistRepo, SpecialistRequest, SpecialistResponse> implements SpeciallistService {
    private final ServiceRepo serviceRepo;
    private final SpecialistMapper specialistMapper;
    private final OrderService orderService;
    private final ReviewService reviewService;


    @Autowired
    public SpeciallistServiceImpl(@Qualifier("specialistRepo")SpecialistRepo baseRepo, ServiceRepo serviceRepo, SpecialistMapper specialistMapper, OrderService orderService,@Lazy ReviewService reviewService) {
        super(baseRepo);
        this.serviceRepo = serviceRepo;
        this.specialistMapper = specialistMapper;

        this.orderService = orderService;
        this.reviewService = reviewService;
    }


    @Override
    public Optional<SpecialistResponse> acceptSpecialist(Long specialistId) {
        Specialist specialist = baseRepository.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist not found"));

        specialist.setSpecialistStatus(SpecialistStatus.APPROVED);
       return Optional.ofNullable(specialistMapper.toDto(baseRepository.save(specialist)));
    }

    @Override
    public List<Specialist> filterSpecialists(String name, String lastName, String email, String serviceName, String sortBy, boolean ascending, LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long numberOfOffers) {
        Specification<Specialist> spec = Specification.where(SpecialistSpecification.filterByName(name))
                .and(SpecialistSpecification.filterByLastName(lastName))
                .and(SpecialistSpecification.filterByEmail(email))
                .and(SpecialistSpecification.filterByServiceName(serviceName))
                .and(SpecialistSpecification.betweenDates(startDate,endDate))
                .and(SpecialistSpecification.filterByNumberOfOrders(numberOfOrders))
                .and(SpecialistSpecification.filterByNumberOfNumber(numberOfOffers));
                ;

        if (ascending) {

            return baseRepository.findAll(spec, Sort.by(sortBy).ascending());
        } else {
            return baseRepository.findAll(spec, Sort.by(sortBy).descending());
        }
    }
@Transactional
    @Override
    public Integer submitRating(Long specialsitId, Integer rate) {
        if (rate>5) {
            throw new ValidationException("Rate must be under 5");
        }
    Double oldRate = findById(specialsitId).get().rate();

    return 0;
    }

    @Override
    public Optional<Specialist> findByIdX(Long specialistId) {
        return baseRepository.findById(specialistId);
    }

    @Override
    public double showRating(Long specialistId) {
        return findById(specialistId).orElseThrow(()-> new ValidationException("no specilist with this id.")).rate();
    }

    @Override
    public List<SpecialistRateRespone> showReviews(Long specialistId) {
//        return orderService.getRatingsBySpecialistId(specialistId);
        return reviewService.getRatingsBySpecialistId(specialistId);
    }

    @Override
    public Optional<SpecialistResponse> activateSpecialist(Long userId) {
        Specialist specialist = findId(userId).get();
specialist.setIsActive(true);
        return Optional.ofNullable(specialistMapper.toDto(specialist));
    }

    @Override
    public Optional<SpecialistResponse> findByEmail(String email) {
        return baseRepository.findByEmail(email)
                .map(specialistMapper::toDto);
    }

    Optional<Specialist> findId(long id){
        return Optional.ofNullable(baseRepository.findById(id).orElseThrow(() -> new ValidationException("Customer not found")));
    }

    @Override
    public Optional<SpecialistResponse> findById(Long id) {
        return baseRepository.findById(id)
                .map(specialistMapper::toDto);
    }



    @Override
    public Optional<SpecialistResponse> save(SpecialistRequest request) {
        if (baseRepository.findByEmail(request.email()).isPresent()) {
            throw new ValidationException("specialist with this email already exists");
        }
       // if (request.personalImage()==null) throw new ValidationException("Personal image is required");

        Specialist customer = SpecialistMapper.INSTANCE.toEntity(request);
        Specialist savedSpelist = baseRepository.save(customer);
        return Optional.of(SpecialistMapper.INSTANCE.toDto(savedSpelist));
    }



    @Override
    public Optional<SpecialistResponse> login(String email, String password) {
        return Optional.ofNullable(toDto(baseRepository.findByEmailAndPassword(email, password).get()));
    }

    @Override
    public byte[] processImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                throw new FileNotFoundException("Image file not found at the  path.", imagePath);
            }

            if (imageFile.length() > 300 * 1024) {
                throw new ImageTooLargeException("Image exceeds 300KB, cannot store it.");
            }

            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                BufferedImage bufferedImage = ImageIO.read(fileInputStream);

                if (bufferedImage == null) {
                    throw new ValidationException("Invalid image format. Only JPG is allowed.");
                }

                // Verify JPEG
                String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(imageFile)).next().getFormatName();
                if (!"JPEG".equalsIgnoreCase(formatName) && !"JPG".equalsIgnoreCase(formatName)) {
                    throw new ValidationException("Invalid image format. Only JPG is allowed.");
                }
            }

            byte[] imageData = new byte[(int) imageFile.length()];
            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                fileInputStream.read(imageData);
            }

            System.out.println("Image processed successfully!");
            return imageData;


        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage() + ": \n" + e.getFilePath());
        } catch (ImageTooLargeException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("An IO error occurred while processing the image.");
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public   void retriveImageOfSpecialist(Long specialistId, String savingPath) {
        try {

            byte[] imageData = findById(specialistId).get().personalImage();

            FileOutputStream fileOutputStream = new FileOutputStream(savingPath);
            fileOutputStream.write(imageData);
            fileOutputStream.close();

            System.out.println("Image retrieved and saved to file successfully!");


        } catch (IOException e) {
            System.err.println("An IO error occurred while processing the image.");
        }
    }

    @Override
    protected Specialist toEntity(SpecialistRequest dto) {
        return specialistMapper.toEntity(dto);
    }

    @Override
    protected SpecialistResponse toDto(Specialist entity) {
        return specialistMapper.toDto(entity);
    }

    @Override
    public List<OrderResponse> getAvilableOrders(Long specialistId) {
        return orderService.findWaitingOrdersBySpecialist(specialistId  );


    }
//    @Override
//    public void updatePassword(UpdatePasswordRequst updatePasswordRequst) {
//
//        Specialist specialist = baseRepository.findByEmail(updatePasswordRequst.email())
//                .orElseThrow(() -> new ValidationException("user with this email not found"));
//
//        if (!specialist.getPassword().equals(updatePasswordRequst.oldPassword())) {
//            throw new ValidationException("Incorrect password");
//        }
//
//        specialist.setPassword(updatePasswordRequst.newPassword());
//        baseRepository.save(specialist);
//    }
}
