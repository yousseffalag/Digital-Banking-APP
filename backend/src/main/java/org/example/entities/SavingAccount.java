package org.example.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("SA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate;
}
