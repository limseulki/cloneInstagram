package com.example.cloneinstagram.comment.repository;


import com.example.cloneinstagram.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndNickName(Long id, String nickName);
    List<Comment> findAllByBoardIdOrderByCreatedAtAsc(Long boardId);
}