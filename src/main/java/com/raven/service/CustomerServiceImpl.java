package com.raven.service;

import com.raven.model.Customer;
import com.raven.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllActiveCustomers() {
        return customerRepository.findAllByActiveTrue();
    }

    @Override
    public Optional<Customer> getActiveCustomerById(Long id) {
        return customerRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setFullName(customerDetails.getFullName());
            customer.setPhone(customerDetails.getPhone());
            customer.setUpdated(System.currentTimeMillis());
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setActive(false);
            customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }

    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findCustomers(Specification<Customer> spec) {
        return customerRepository.findAll(spec);
    }
}
