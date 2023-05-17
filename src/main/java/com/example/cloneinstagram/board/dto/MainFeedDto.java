package com.example.cloneinstagram.board.dto;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainFeedDto {
    private Long boardId;
    private String imageUrl;
    private String contents;
    private String nickName;
    private String createdAt;
    private List<CommentResponseDto> commentList;

    public MainFeedDto(Board board) {
        this.boardId = board.getId();
        this.imageUrl = board.getImageUrl();
        this.contents = board.getContents();
        this.nickName = board.getNickName();
        this.createdAt = board.getCreatedAt();
        this.commentList = board.getCommentList()
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());;
    }
}
