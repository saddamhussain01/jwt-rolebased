package com.easylearning.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String mobileNumber;

    private String gender;

    /* each user can have multiple roles (like "admin", "user", etc.),
       you might use @ElementCollection to store these roles.

       fetch = FetchType.EAGER : means that whenever you load a User from the database,
       you also want to load their roles right away, instead of waiting until you actually need them.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;


}