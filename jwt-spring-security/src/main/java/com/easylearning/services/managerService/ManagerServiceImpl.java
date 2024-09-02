package com.easylearning.services.managerService;

import com.easylearning.exceptions.UserNotFoundException;
import com.easylearning.exceptions.UsersNotFoundException;
import com.easylearning.models.Role;
import com.easylearning.models.User;
import com.easylearning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl {


    @Autowired
    private UserRepository userRepository;


    public List<User> fetchAllUsers() throws UsersNotFoundException {

        List<User> users = userRepository.findByRoles(Role.USER);

        if (users.isEmpty()) {
            throw new UsersNotFoundException("users not found");
        }
        return users;
    }

    public Optional<User> getUserById(long userId) throws UserNotFoundException {
        List<User> users = userRepository.findByRoles(Role.USER);
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        if (userIds.contains(userId)) {
            return userRepository.findById(userId);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public boolean deleteUser(long userId) {
        List<User> users = userRepository.findByRoles(Role.USER);
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        if (userIds.contains(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUser(long userId, User updatedUser) throws UserNotFoundException {

        List<User> users = userRepository.findByRoles(Role.USER);
        Optional<User> existingUserOptional = users.stream()
                                               .filter(user -> user.getId() == userId)
                                               .findFirst();
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            // Update user properties
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());


            // Update any other fields as needed
            userRepository.save(existingUser);
            return true;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }



}
