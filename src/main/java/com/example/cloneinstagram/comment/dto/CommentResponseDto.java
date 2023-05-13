package com.example.cloneinstagram.comment.dto;

import com.example.cloneinstagram.comment.entity.Comment;
import lombok.AllArgsConstructor;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private String nickName;
    private String contents;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getMember().getId();
        this.nickName = comment.getMember().getNickName();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
    }
}