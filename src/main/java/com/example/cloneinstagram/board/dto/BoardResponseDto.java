package com.example.cloneinstagram.board.dto;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String imageUrl;
    private String contents;
    private LocalDateTime createdAt;
    @OrderBy("createdAt DESC")
    private List<CommentResponseDto> commentResponseDtoList;

    @Builder
    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.imageUrl = board.getImageUrl();
        this.contents = board.getContents();
        this.createdAt = LocalDateTime.parse(board.getCreatedAt());
        this.commentResponseDtoList = board.getCommentList()
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}