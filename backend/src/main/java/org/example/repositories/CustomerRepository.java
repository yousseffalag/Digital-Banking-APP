package org.example.repositories;

import org.example.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer , Long> {
    @org.springframework.data.jpa.repository.Query("select c from Customer c where c.name like :kw")
    java.util.List<Customer> searchCustomer(@org.springframework.data.repository.query.Param("kw") String kw);
}
