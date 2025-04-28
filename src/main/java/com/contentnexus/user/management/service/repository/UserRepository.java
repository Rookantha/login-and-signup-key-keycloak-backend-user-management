package com.contentnexus.user.management.service.repository;

import com.contentnexus.user.management.service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);  // Find user by IAM ID
}
