package com.easylearning.dtos;

import lombok.Data;

@Data
public class SignupRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String mobileNumber;

    private String gender;

    private boolean manager;



}
