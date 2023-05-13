package com.example.cloneinstagram.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull
    private Long boardId;
    @NotNull
    private String contents;
}