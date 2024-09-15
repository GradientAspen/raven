package com.raven.service;

import com.raven.dto.CustomerDto;
import com.raven.exception.CustomerException;
import com.raven.mapper.CustomerMapper;
import com.raven.model.Customer;
import com.raven.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.toCustomer(customerDto);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {

        List<Customer> allByIsActiveTrue = customerRepository.findAllByIsActiveTrue();
        return allByIsActiveTrue.stream().map(customerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDto> getActiveCustomerById(Long id) {
        Optional<Customer> byIdAndIsActiveTrue = customerRepository.findByIdAndIsActiveTrue(id);
        return byIdAndIsActiveTrue.map(customerMapper::toDto);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDetails) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setFullName(customerDetails.getFullName());
            customer.setPhone(customerDetails.getPhone());
            customer.setUpdated(System.currentTimeMillis());
            Customer save = customerRepository.save(customer);
            return customerMapper.toDto(save);
        } else {
            throw new CustomerException("Customer not found");
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
            throw new CustomerException("Customer not found");
        }

    }

    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        Optional<Customer> byEmail = customerRepository.findByEmail(email);
        return byEmail.map(customerMapper::toDto);
    }

}
