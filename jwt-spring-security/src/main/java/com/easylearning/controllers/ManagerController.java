package com.easylearning.controllers;

import com.easylearning.exceptions.UserNotFoundException;
import com.easylearning.exceptions.UsersNotFoundException;
import com.easylearning.models.User;
import com.easylearning.services.managerService.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers()  {

        List<User>   users = null;
        try {
               users = managerService.fetchAllUsers();
        } catch (UsersNotFoundException e) {
            return new ResponseEntity("No users found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId)  {

        Optional<User> user = null;
        try {
            user = managerService.getUserById(userId);
        } catch (UserNotFoundException e) {
            return new ResponseEntity("User id " + userId + " is not found ", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(user,HttpStatus.OK);

    }

    @DeleteMapping("/deleteUserById/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {

        if (managerService.deleteUser(userId)) {
            return new ResponseEntity("User Id " + userId + " is deleted successfully ", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity("User id " + userId + " is not found ", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateUserById/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody User updatedUser) {
        try {
            managerService.updateUser(userId, updatedUser);
            return new ResponseEntity<>("User with id " + userId + " updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }
    }

}
