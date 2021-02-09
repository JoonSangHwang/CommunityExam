package com.joonsang.example.CommunityExam.board;

import com.joonsang.example.CommunityExam.entity.Board;
import com.joonsang.example.CommunityExam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
