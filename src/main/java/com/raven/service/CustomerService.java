package com.raven.service;

import com.raven.dto.CustomerDto;
import com.raven.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customer);

    List<CustomerDto> getAllActiveCustomers();

    Optional<CustomerDto> getActiveCustomerById(Long id);

    CustomerDto updateCustomer(Long id, CustomerDto customerDetails);

    void deleteCustomer(Long id);

    Optional<CustomerDto> findByEmail(String email);


}
