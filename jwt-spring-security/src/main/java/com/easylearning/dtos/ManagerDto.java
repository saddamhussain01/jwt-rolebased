package com.easylearning.dtos;

import lombok.Data;

@Data
public class ManagerDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private String gender;

    private String role;
}
