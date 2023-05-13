package com.example.cloneinstagram.board.dto;

import com.example.cloneinstagram.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String imageUrl;
    private String contents;
    private LocalDateTime createdAt;

    @Builder
    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.imageUrl = board.getImageUrl();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
    }
}