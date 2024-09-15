package com.raven.service;

import com.raven.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    List<Customer> getAllActiveCustomers();

    Optional<Customer> getActiveCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customerDetails);

    void deleteCustomer(Long id);

    Optional<Customer> findByEmail(String email);

    List<Customer> findCustomers(Specification<Customer> spec);
}
