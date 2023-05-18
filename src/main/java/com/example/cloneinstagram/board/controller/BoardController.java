package com.example.cloneinstagram.board.controller;

import com.example.cloneinstagram.board.dto.BoardRequestDto;
import com.example.cloneinstagram.board.dto.BoardResponseDto;
import com.example.cloneinstagram.board.dto.MainFeedDto;
import com.example.cloneinstagram.board.service.BoardService;
import com.example.cloneinstagram.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Tag(name = "boardController", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @Operation(summary = "게시글 작성 API", description = "게시글 작성")
    @PostMapping(value = "/", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponseDto> createBoard(@RequestPart(value = "image") MultipartFile image,
                                                        @Valid @RequestPart(value = "board") BoardRequestDto boardRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.createBoard(image, boardRequestDto, userDetails);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정 API", description = "게시글 수정")
    @PutMapping(value = "/{boardId}")
    public ResponseEntity<BoardResponseDto> updatePost(@PathVariable(name = "boardId") Long id, @RequestBody BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updatePost(id, boardRequestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제 API", description = "게시글 삭제 후 삭제된 아이디 반환")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "boardId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deletePost(id, userDetails.getUser());
    }

    // 전체 피드 조회
    @Operation(summary = "전체 피드 조회 API", description = "전체 피드 조회")
    @GetMapping("/")
    public ResponseEntity<Page<MainFeedDto>> getMainFeed(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam(defaultValue = "20") int pageSize,
                                                         @RequestParam(defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return boardService.getMainFeed(userDetails.getUser(), pageable);
    }

//    // 태그 검색
//    @GetMapping("/{hashTags}")
//    public List<MainFeedDto> searchByTag(@PathVariable String hashTags, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return boardService.searchByTag(hashTags, userDetails.getUser());
//    }
}
