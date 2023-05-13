package com.example.cloneinstagram.love.controller;

import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.love.service.BoardLoveService;
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
public class BoardLoveController {

    private final BoardLoveService boardLoveService;

    //피드 좋아요
    @Operation(summary = "피드 좋아요 API", description = "피드 좋아요")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "피드 좋아요 성공")})
    @PostMapping("/boards/{boardId}")
    public ResponseMsgDto boardLove(@PathVariable(name = "boardId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardLoveService.boardLove(id, userDetails.getUser());
    }
}
