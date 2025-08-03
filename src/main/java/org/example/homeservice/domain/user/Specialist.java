package org.example.homeservice.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import org.example.homeservice.domain.enums.UserRole;
import org.example.homeservice.domain.service.Service;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Table(name = Specialist.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Specialist extends BaseUser {

    public static final String TABLE_NAME = "specialist";

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private SpecialistStatus specialistStatus = SpecialistStatus.PENDING;

    @Column(name = "total_rate")
    private Double rate = 0.0;
    @Column(name = "number_of_rate")
    private int numberOfRate = 0;

    @JoinColumn(nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    List<Service> workServices;


    // @Lob //todo whgat is this
    @Column(name = "image_data", length = 300000)
    private byte[] personalImage;

    // making sure no one can change the role
    @PrePersist
    @PreUpdate
    public void enforceSpecialistRole() {
        this.userRole = UserRole.SPECIALIST;
    }

    @Override
    public String toString() {
        return "Specialist{" +
               "specialistStatus=" + specialistStatus +
               ", rate=" + rate +
               ", workServices=" + workServices +
               '}';
    }
}
