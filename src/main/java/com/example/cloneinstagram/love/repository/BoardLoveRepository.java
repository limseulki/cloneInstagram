package com.example.cloneinstagram.love.repository;

import com.example.cloneinstagram.love.entity.BoardLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLoveRepository extends JpaRepository<BoardLove, Long> {
    boolean findByBoardIdAndMemberId(Long boardId, Long memberId);
    void deleteByBoardIdAndMemberId(Long boardId, Long memberId);
}
