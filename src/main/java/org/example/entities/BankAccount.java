package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Bank_Accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4)
@Getter
@Setter
@NoArgsConstructor
public abstract class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double balance;
    private String accuracy;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<Operation> operations;
}
