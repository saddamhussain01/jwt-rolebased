package com.easylearning.dtos;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;

    private String password;
}
