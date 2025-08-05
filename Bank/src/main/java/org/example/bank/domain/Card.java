package org.example.bank.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, unique = true)
    @CreditCardNumber
//    @Length(min = 16, max = 16, message = "should be 16 digits")
    private String cardNumber;
    //    @NotBlank(message = "cant be null")
    private String cardHolderName;
    @Future(message = "should be in futture")
    @NotNull(message = "cant be null")
    private LocalDate expirationDate;
    @NotBlank
    @Length(min = 3, max = 3, message = "should be 3Ò digits")
    private String cvv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(cardNumber, card.cardNumber) && Objects.equals(expirationDate.getYear(), card.expirationDate.getYear())
               && Objects.equals(expirationDate.getMonth(), card.expirationDate.getMonth())
               && Objects.equals(cvv, card.cvv);
    }
    //@Override
//public boolean equals(Object o) {
//    if (this == o) return true;
//    if (!(o instanceof Card card)) return false;
//    if (cardNumber == null || card.cardNumber == null) return false;
//    if (cvv == null || card.cvv == null) return false;
//    if (expirationDate == null || card.expirationDate == null) return false;
//
//    return cardNumber.equals(card.cardNumber)
//           && expirationDate.getYear() == card.expirationDate.getYear()
//           && expirationDate.getMonth() == card.expirationDate.getMonth()
//           && expirationDate.getDayOfMonth() == card.expirationDate.getDayOfMonth()
//           && cvv.equals(card.cvv)
//            && cardHolderName.equals(card.cardHolderName);
//}
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, expirationDate, cvv);
    }
}