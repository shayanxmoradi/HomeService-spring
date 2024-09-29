package org.example.homeservice.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.sql.Time;
@Table(name = BaseUser.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseUser extends BaseEntity<Long> {
    public static final String TABLE_NAME = "base_user";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";


    @Column(nullable = false,name = FIRST_NAME)
    @Length(min = 2, max = 50)
    private String firstName;

    @Column(nullable = false,name = LAST_NAME)
    @Length(min = 2, max = 50)
    private String lastName;

    @Column(nullable = false,name = EMAIL,unique = true)
    @Email
    private String email;

    @Column
    private Date registrationDate;

    @Column
    private Time registrationTime;

    @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
    @Column(nullable = false,name = PASSWORD)
    private String password;

    @OneToOne
    Wallet wallet;
}
