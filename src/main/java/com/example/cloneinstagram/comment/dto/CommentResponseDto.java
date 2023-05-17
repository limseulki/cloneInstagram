package com.example.cloneinstagram.comment.dto;

import com.example.cloneinstagram.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private Long userId;
    private String nickName;
    private String contents;
    private String createdAt;
    private boolean commentLove;

    public CommentResponseDto(Comment comment, boolean commentLove) {
        this.commentId = comment.getId();
        this.userId = comment.getMember().getId();
        this.nickName = comment.getMember().getNickName();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.commentLove = commentLove;
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getMember().getId();
        this.nickName = comment.getMember().getNickName();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
    }
}