package org.example.homeservice.service.user.speciallist;

import jakarta.validation.ValidationException;
import org.example.homeservice.Exception.FileNotFoundException;
import org.example.homeservice.Exception.ImageTooLargeException;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.repository.service.ServiceRepo;

import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.user.BaseUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class SpeciallistServiceImpl extends BaseUserServiceImpl<Specialist, SpecialistRepo, SpecialistRequest, SpecialistResponse> implements SpeciallistService {
    private final ServiceRepo serviceRepo;
    private final SpecialistMapper specialistMapper;
    private final OrderService orderService;


    @Autowired
    public SpeciallistServiceImpl(@Qualifier("specialistRepo")SpecialistRepo baseRepo, ServiceRepo serviceRepo, SpecialistMapper specialistMapper, OrderService orderService) {
        super(baseRepo);
        this.serviceRepo = serviceRepo;
        this.specialistMapper = specialistMapper;

        this.orderService = orderService;
    }


    @Override
    public Optional<SpecialistResponse> acceptSpecialist(Long specialistId) {
        Specialist specialist = baseRepository.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist not found"));

        specialist.setSpecialistStatus(SpecialistStatus.APPROVED);
       return Optional.ofNullable(specialistMapper.toDto(baseRepository.save(specialist)));
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
