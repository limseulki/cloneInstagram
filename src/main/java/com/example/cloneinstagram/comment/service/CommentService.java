package com.example.cloneinstagram.comment.service;


import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.board.repository.BoardRepository;
import com.example.cloneinstagram.comment.dto.CommentRequestDto;
import com.example.cloneinstagram.comment.dto.CommentResponseDto;
import com.example.cloneinstagram.comment.entity.Comment;
import com.example.cloneinstagram.comment.repository.CommentRepository;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.member.entity.Member;
import com.example.cloneinstagram.member.repository.MemberRepository;
import com.example.cloneinstagram.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    Board board;
    Comment comment;



    // 댓글 작성
    @Transactional
    public ResponseMsgDto<?> createComment(CommentRequestDto commentRequestDto,
                                           Member member) {
        // 게시글 검증
        Board board = boardRepository.findById(commentRequestDto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));


        // 댓글 생성
        Comment comment = new Comment(commentRequestDto, board, member);
        commentRepository.save(comment);

        // 응답 생성
        return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "댓글 생성 완료", new CommentResponseDto(comment));
    }

    //댓글 삭제
    @Transactional
    public ResponseMsgDto<?> deleteComment(Long id, Member member) {
        matchAuthor(id, member);
        comment = existComment(id);
        commentRepository.deleteById(id);
        return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "댓글 삭제 완료", null);
    }


    private Comment existComment(Long id) {
        comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return comment;
    }


    private void matchAuthor(Long id, Member member) {
        comment = commentRepository.findByIdAndNickName(id, member.getNickName()).orElseThrow(
                () -> new CustomException(ErrorCode.AUTHOR_NOT_SAME_MOD)
        );
    }
}