package com.easylearning.dtos;

import com.easylearning.models.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private String gender;

    private Set<Role> roles;
}
