package com.raven.controller;

import com.raven.dto.CustomerDto;
import com.raven.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Tag(name = "Customer Controller", description = "CRUD Operations for Customer entity")
public class CustomerController {

    private final CustomerService customerService;

   @Operation(summary = "Create a new customer", description = "Creates a new customer and returns the created entity")
   @PostMapping
   public ResponseEntity<CustomerDto> createCustomer(@Validated @RequestBody CustomerDto customer) {
       CustomerDto newCustomer = customerService.createCustomer(customer);
       return ResponseEntity.ok(newCustomer);
   }

    @Operation(summary = "Get all active customers", description = "Returns a list of all active customers")
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Returns a customer by ID if the customer is active")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        Optional<CustomerDto> customerOptional = customerService.getActiveCustomerById(id);
        return customerOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update customer by ID", description = "Updates a customer's details, except for the email")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Validated
    @RequestBody CustomerDto customerDetails) {
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Soft delete a customer", description = "Marks a customer as inactive (soft delete) " +
            "without removing data from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
