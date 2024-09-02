package com.easylearning.services;

import com.easylearning.dtos.SignupRequest;
import com.easylearning.dtos.UserDto;
import com.easylearning.models.Role;
import com.easylearning.models.User;
import com.easylearning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto creatingUser(SignupRequest signupRequest) {

        //Taking user details and creating user in database & transfering the user details with userid

        User createUser = new User();

        createUser.setFirstName(signupRequest.getFirstName());
        createUser.setLastName(signupRequest.getLastName());
        createUser.setEmail(signupRequest.getEmail());
        //we are not storing plain text password, by using bycrypt function we are storing encoded password
        createUser.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        createUser.setMobileNumber(signupRequest.getMobileNumber());
        createUser.setGender(signupRequest.getGender());
        // Create a Set containing the user's role(s)
        Set<Role> roles = new HashSet<>();

//        roles.add(Role.USER);

        if (signupRequest.isManager()){
            roles.add(Role.MANAGER);
        }else {
            roles.add(Role.USER); // Add the desired role
        }
        createUser.setRoles(roles);

        userRepository.save(createUser);


        //sending created user details with user id using dto
        UserDto sendDetails = new UserDto();
        sendDetails.setId(createUser.getId());
        sendDetails.setFirstName(createUser.getFirstName());
        sendDetails.setLastName(createUser.getLastName());
        sendDetails.setEmail(createUser.getEmail());
        sendDetails.setMobileNumber(createUser.getMobileNumber());
        sendDetails.setGender(createUser.getGender());
        sendDetails.setRoles(createUser.getRoles());

        return sendDetails;
    }
}
