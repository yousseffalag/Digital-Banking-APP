package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.Date;
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
    private String id;
    private Double balance;
    private String accuracy;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<Operation> operations;
}
