package com.raven.dto;

import lombok.Data;

@Data
public class CustomerDto {

    private Long id;

    private Long created;

    private Long updated;

    private String fullName;

    private String email;

    private String phone;

    private boolean isActive = true;
}
