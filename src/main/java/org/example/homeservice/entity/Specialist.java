package org.example.homeservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.homeservice.entity.enums.SpecialistStatus;

import java.util.List;

@Table(name = Specialist.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Specialist extends BaseUser {
    public static final String TABLE_NAME = "specialist";

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private SpecialistStatus specialistStatus=SpecialistStatus.PENDING;

    @Column
    private Double rate=0.0;

    @OneToMany
    List<Service > workServices;


    // @Lob //todo whgat is this
    @Column(name = "image_data",length = 300000)
    private byte[] personalImage;

    @Override
    public String toString() {
        return "Specialist{" +
               "specialistStatus=" + specialistStatus +
               ", rate=" + rate +
               ", workServices=" + workServices +
               '}';
    }
}
