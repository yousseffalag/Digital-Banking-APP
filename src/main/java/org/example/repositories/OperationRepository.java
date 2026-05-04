package org.example.repositories;

import org.example.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation , Long> {

    List<Operation> findByBankAccountId(UUID accountId);
    Page<Operation> findByBankAccountId(UUID accountId , Pageable pageable);

}
