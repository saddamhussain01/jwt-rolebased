package com.easylearning.services;

import com.easylearning.dtos.SignupRequest;
import com.easylearning.dtos.UserDto;

public interface AuthService {
    UserDto creatingUser(SignupRequest signupRequest);
}
