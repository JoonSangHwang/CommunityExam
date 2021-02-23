package com.joonsang.example.CommunityExam.repository;

import com.joonsang.example.CommunityExam.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

    @Query("select m from Account m where m.email= :email")
    Optional<Account> findEmail(String email);
}
