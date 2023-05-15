package com.example.cloneinstagram.love.controller;

import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.love.service.CommentLoveService;
import com.example.cloneinstagram.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentLoveController {

    private final CommentLoveService commentLoveService;

    //댓글 좋아요
    @Operation(summary = "댓글 좋아요 API", description = "댓글 좋아요")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "댓글 좋아요 성공")})
    @PostMapping("/comments/{commentId}")
    public ResponseMsgDto commentLove(@PathVariable(name = "commentId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentLoveService.commentLove(id, userDetails.getUser());
    }
}
