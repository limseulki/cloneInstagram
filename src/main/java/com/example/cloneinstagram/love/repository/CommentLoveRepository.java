package com.example.cloneinstagram.love.repository;

import com.example.cloneinstagram.love.entity.CommentLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLoveRepository extends JpaRepository<CommentLove, Long> {
}
