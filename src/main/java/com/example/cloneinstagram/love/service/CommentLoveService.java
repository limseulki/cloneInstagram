package com.example.cloneinstagram.love.service;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.comment.entity.Comment;
import com.example.cloneinstagram.comment.repository.CommentRepository;
import com.example.cloneinstagram.commonDto.ResponseMsgDto;
import com.example.cloneinstagram.love.entity.BoardLove;
import com.example.cloneinstagram.love.entity.CommentLove;
import com.example.cloneinstagram.love.repository.BoardLoveRepository;
import com.example.cloneinstagram.love.repository.CommentLoveRepository;
import com.example.cloneinstagram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLoveService {

    private final CommentRepository commentRepository;
    private final CommentLoveRepository commentLoveRepository;

    @Transactional
    public ResponseMsgDto commentLove(Long id, Member member) {
        try {
            if (commentLoveRepository.findByCommentIdAndMemberId(id, member.getId())) {
                commentLoveRepository.deleteByCommentIdAndMemberId(id, member.getId());
                return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "좋아요 취소 성공", null);
            } else {
                Comment comment = commentRepository.findById(id);
                CommentLove commentLove = new CommentLove(comment, member);
                BoardLoveRepository.save(commentLove);
                return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "좋아요 등록 성공", null);
            }
        }catch(Exception e){
            return ResponseMsgDto.setFail(HttpStatus.BAD_REQUEST.value(), "실패");
        }
        return null;
    }
}
