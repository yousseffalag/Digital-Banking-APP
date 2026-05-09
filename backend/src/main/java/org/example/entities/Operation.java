package org.example.entities;


import jakarta.persistence.*;
import lombok.*;
import org.example.enums.OperationType;

import java.util.Date;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    @JoinColumn(name = "bank_account")
    private BankAccount bankAccount;
    private String description;
    private String createdBy;
}
