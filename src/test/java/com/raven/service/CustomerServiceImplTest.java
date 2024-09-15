package com.raven.service;

import com.raven.dto.CustomerDto;
import com.raven.exception.CustomerException;
import com.raven.mapper.CustomerMapper;
import com.raven.model.Customer;
import com.raven.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer existingCustomer;
    private CustomerDto existingCustomerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Given - Assume that this customer is created by migration (existing in DB)
        existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setFullName("John Doe");
        existingCustomer.setEmail("john.doe@google.com");
        existingCustomer.setPhone("+1234567890");
        existingCustomer.setActive(true);

        existingCustomerDto = new CustomerDto();
        existingCustomerDto.setId(1L);
        existingCustomerDto.setFullName("John Doe");
        existingCustomerDto.setEmail("john.doe@google.com");
        existingCustomerDto.setPhone("+1234567890");
        existingCustomerDto.setActive(true);
    }

    @Test
    void createCustomer_Success() {
        // Given - Customer that we want to create
        CustomerDto newCustomerDto = new CustomerDto();
        newCustomerDto.setFullName("New User");
        newCustomerDto.setEmail("new.user@google.com");
        newCustomerDto.setPhone("+1111111111");

        Customer newCustomer = new Customer();
        newCustomer.setId(4L);
        newCustomer.setFullName("New User");
        newCustomer.setEmail("new.user@google.com");
        newCustomer.setPhone("+1111111111");

        // When - Saving the new customer
        when(customerMapper.toCustomer(newCustomerDto)).thenReturn(newCustomer);
        when(customerRepository.save(newCustomer)).thenReturn(newCustomer);
        when(customerMapper.toDto(newCustomer)).thenReturn(newCustomerDto);

        CustomerDto createdCustomer = customerService.createCustomer(newCustomerDto);

        // Then - The customer should be saved and returned correctly
        assertNotNull(createdCustomer);
        assertEquals(newCustomerDto.getFullName(), createdCustomer.getFullName());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void getAllActiveCustomers_Success() {
        // Given - A list of existing active customers
        List<Customer> activeCustomers = Arrays.asList(existingCustomer);

        // When - Retrieving all active customers
        when(customerRepository.findAllByIsActiveTrue()).thenReturn(activeCustomers);
        when(customerMapper.toDto(existingCustomer)).thenReturn(existingCustomerDto);

        List<CustomerDto> customerDtoList = customerService.getAllActiveCustomers();

        // Then - The list of active customers should be returned
        assertNotNull(customerDtoList);
        assertFalse(customerDtoList.isEmpty());
        assertEquals(1, customerDtoList.size());
        assertEquals("John Doe", customerDtoList.get(0).getFullName());
        verify(customerRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void getActiveCustomerById_Success() {
        // Given - An existing active customer with ID 1
        when(customerRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerMapper.toDto(existingCustomer)).thenReturn(existingCustomerDto);

        // When - Retrieving the customer by ID
        Optional<CustomerDto> foundCustomer = customerService.getActiveCustomerById(1L);

        // Then - The customer should be found and returned
        assertTrue(foundCustomer.isPresent());
        assertEquals(existingCustomerDto.getFullName(), foundCustomer.get().getFullName());
        verify(customerRepository, times(1)).findByIdAndIsActiveTrue(1L);
    }

    @Test
    void updateCustomer_Success() {
        // Given - An existing customer and the updated details
        CustomerDto updatedCustomerDto = new CustomerDto();
        updatedCustomerDto.setFullName("John Doe Updated");
        updatedCustomerDto.setPhone("+2222222222");

        // When - Updating the customer details
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);
        when(customerMapper.toDto(existingCustomer)).thenReturn(updatedCustomerDto);

        CustomerDto updatedCustomer = customerService.updateCustomer(1L, updatedCustomerDto);

        // Then - The customer should be updated
        assertNotNull(updatedCustomer);
        assertEquals("John Doe Updated", updatedCustomer.getFullName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void updateCustomer_NotFound() {
        // Given - No customer with ID 99 exists
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Trying to update a non-existent customer
        CustomerDto customerDetails = new CustomerDto();
        customerDetails.setFullName("Non-existent User");

        // Then - A CustomerException should be thrown
        assertThrows(CustomerException.class, () -> customerService.updateCustomer(99L, customerDetails));
        verify(customerRepository, times(1)).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_Success() {
        // Given - An existing customer with ID 1
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        // When - Deleting the customer
        customerService.deleteCustomer(1L);

        // Then - The customer should be marked as inactive
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(existingCustomer);
        assertFalse(existingCustomer.isActive());
    }

    @Test
    void deleteCustomer_NotFound() {
        // Given - No customer with ID 99 exists
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Trying to delete a non-existent customer

        // Then - A CustomerException should be thrown
        assertThrows(CustomerException.class, () -> customerService.deleteCustomer(99L));
        verify(customerRepository, times(1)).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void findByEmail_Success() {
        // Given - An existing customer with email "john.doe@google.com"
        when(customerRepository.findByEmail("john.doe@google.com")).thenReturn(Optional.of(existingCustomer));
        when(customerMapper.toDto(existingCustomer)).thenReturn(existingCustomerDto);

        // When - Finding a customer by email
        Optional<CustomerDto> foundCustomer = customerService.findByEmail("john.doe@google.com");

        // Then - The customer should be found
        assertTrue(foundCustomer.isPresent());
        assertEquals("john.doe@google.com", foundCustomer.get().getEmail());
        verify(customerRepository, times(1)).findByEmail("john.doe@google.com");
    }

    @Test
    void findByEmail_NotFound() {
        // Given - No customer with the email "non.existent@google.com" exists
        when(customerRepository.findByEmail("non.existent@google.com")).thenReturn(Optional.empty());

        // When - Trying to find a non-existent customer by email
        Optional<CustomerDto> foundCustomer = customerService.findByEmail("non.existent@google.com");

        // Then - The customer should not be found
        assertFalse(foundCustomer.isPresent());
        verify(customerRepository, times(1)).findByEmail("non.existent@google.com");
    }
}