package org.example.homeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Table(name = Service.TABLE_NAME)
@Entity
@AllArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Builder
public class Service extends BaseEntity<Long> {
    public static final String TABLE_NAME = "service";
    private static final String PARENT_SERVICE_ID = "parent_service_id";
    private static final String SERVICE_NAME = "service_name";
    private static final String DESCRIPTION = "description";
    private static final String BASE_PRICE = "base_price";


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PARENT_SERVICE_ID)
    private Service parentService;

    @OneToMany(mappedBy = "parentService",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Service> subServices= new ArrayList<>();

    @Column(name = SERVICE_NAME,unique = true)
    @NotBlank(message = "Name cannot be blank")

    private String name;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = BASE_PRICE)
    @Positive
    private Float basePrice;

    //todo should be many to many?
    @JoinColumn(nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})

    private List<Specialist> avilableSpecialists = new ArrayList<>();

    @Column
    private Boolean category;



    public void addSubService(Service subService) {
        subServices.add(subService);
        subService.setParent(this);
    }

    public void removeSubService(Service subService) {
        subServices.remove(subService);
        subService.setParent(null);
    }

    public void setParent(Service parent) {
        this.parentService = parent;
    }

    public Service(String name) {
        this.name = name;
    }


    public void setBasePrice(Float basePrice) {
        if (category) {
            throw new IllegalStateException("Categories cannot have a base price.");
        }
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "Service{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", base_price=" + basePrice +
               '}';
    }
}
