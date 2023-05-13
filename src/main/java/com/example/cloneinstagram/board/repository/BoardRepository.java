package com.example.cloneinstagram.board.repository;

import com.example.cloneinstagram.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndNickName(Long id, String nickName);
}
