package org.example.entities;


import jakarta.persistence.*;
import lombok.*;
import org.example.enums.OperationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    @JoinColumn(name = "bank_account")
    private BankAccount bankAccount;
    private String description;
}
