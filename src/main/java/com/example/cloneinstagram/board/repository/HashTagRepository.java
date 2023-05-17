package com.example.cloneinstagram.board.repository;

import com.example.cloneinstagram.board.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    HashTag findByHashTag(String hashTag);
}
