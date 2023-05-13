package com.example.cloneinstagram.board.dto;

import com.example.cloneinstagram.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private String imageUrl;
    private String contents;

    @Builder
    public BoardResponseDto(Board board) {
        this.imageUrl = board.getImageUrl();
        this.contents = board.getContents();
    }
}