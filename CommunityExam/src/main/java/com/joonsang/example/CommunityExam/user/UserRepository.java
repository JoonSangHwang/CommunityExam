package com.joonsang.example.CommunityExam.user;

import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("select m from User m where m.email= :email")
    Optional<User> findEmail(String email);
}
