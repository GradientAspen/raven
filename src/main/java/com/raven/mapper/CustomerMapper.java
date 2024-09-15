package com.raven.mapper;

import com.raven.config.MapperConfig;
import com.raven.dto.CustomerDto;
import com.raven.model.Customer;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);

    Customer toCustomer(CustomerDto customerDto);
}
