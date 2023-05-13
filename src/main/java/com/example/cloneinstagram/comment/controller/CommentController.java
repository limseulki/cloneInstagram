package com.example.cloneinstagram.comment.controller;


import com.example.cloneinstagram.comment.dto.CommentRequestDto;
import com.example.cloneinstagram.comment.service.CommentService;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    // 게시글 작성

    @PostMapping(value = "/")
    public ResponseMsgDto<?> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseMsgDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }

}