package com.example.cloneinstagram.board.controller;

import com.example.cloneinstagram.board.dto.BoardRequestDto;
import com.example.cloneinstagram.board.service.BoardService;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "boardController", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @Operation(summary = "게시글 작성 API", description = "게시글 작성")
    @PostMapping(value = "/", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsgDto<?> createBoard(@RequestPart(value = "image") MultipartFile image,
                                         @Valid @RequestPart(value = "board") BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.createBoard(image, boardRequestDto, userDetails);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정 API", description = "게시글 수정")
    @PutMapping(value = "/{postId}")
    public ResponseMsgDto<?> updatePost(@PathVariable(name = "postId") Long id, @RequestBody BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updatePost(id, boardRequestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제 API", description = "게시글 삭제 후 삭제된 아이디 반환")
    @DeleteMapping("/{postId}")
    public ResponseMsgDto<?> deletePost(@PathVariable(name = "postId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deletePost(id, userDetails.getUser());
    }

    // 전체 피드 조회
    @Operation(summary = "전체 피드 조회 API", description = "전체 피드 조회")
    @GetMapping("/")
    public ResponseMsgDto getMainFeed(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getMainFeed(userDetails.getUser());
    }
}
