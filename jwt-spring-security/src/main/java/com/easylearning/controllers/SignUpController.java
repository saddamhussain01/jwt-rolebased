package com.easylearning.controllers;

import com.easylearning.dtos.SignupRequest;
import com.easylearning.dtos.UserDto;
import com.easylearning.models.User;
import com.easylearning.repositories.UserRepository;
import com.easylearning.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;


    /*
     we need to tell the spring boot application.
     This API will allow without any authentication like jwt token.
     Because of first time user is registering.
     For telling to spring boot we are creating bean in configuration package.
     */
    
    //http://localhost:8080/register
    @PostMapping("/register")
    public ResponseEntity<?>  createUser(@RequestBody SignupRequest signupRequest){

        /* when user signup we are creating user in database because
        user details need to store in database
        */

        // Check if the created user already exists
        User existingUser = userRepository.findFirstByEmail(signupRequest.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>("User already exists.", HttpStatus.BAD_REQUEST);
        }

        UserDto createdUser = authService.creatingUser(signupRequest);

        if (createdUser == null) {
            return new ResponseEntity<>("User could not be created. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
