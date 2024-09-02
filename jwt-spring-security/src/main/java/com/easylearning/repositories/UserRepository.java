package com.easylearning.repositories;

import com.easylearning.models.Role;
import com.easylearning.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findFirstByEmail(String email);

    List<User> findByRoles(Role role);
}
