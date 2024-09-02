package com.easylearning.controllers;

import com.easylearning.dtos.AuthenticationRequest;
import com.easylearning.dtos.AuthenticationResponse;
import com.easylearning.repositories.UserRepository;
import com.easylearning.services.jwtService.UserDetailsServiceImpl;
import com.easylearning.services.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authentication")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse httpServletResponse) throws IOException {

        try {
            /*
            authenticationManager.authenticate() verifying :
            If the provided credentials are correct, the authentication process continues; otherwise,
            it throws an exception to indicate that the credentials are invalid.

            UsernamePasswordAuthenticationToken () :
            This creates an object representing the user's email and password.
            It's like handing over your login information to the authentication manager.
             */

            // Attempt authentication
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        }catch (BadCredentialsException e) {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password.");
        } catch (DisabledException e) {
            // Account disabled
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND,"User account is disabled. Please contact support.");
           return null;
        }

        // If authentication succeeded, load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        // Generate JWT token and return it
        final String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        AuthenticationResponse response = new AuthenticationResponse(jwtToken);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
