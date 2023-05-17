package com.example.cloneinstagram.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long boardId;
    @NotBlank
    private String contents;
}