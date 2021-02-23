package com.joonsang.example.CommunityExam.repository;

import com.joonsang.example.CommunityExam.entity.Board;
import com.joonsang.example.CommunityExam.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByAccount(Account account);
}
