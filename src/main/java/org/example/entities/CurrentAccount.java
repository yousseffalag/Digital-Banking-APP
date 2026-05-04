package org.example.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("CA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount extends BankAccount{
    private Double overDraft;
}
