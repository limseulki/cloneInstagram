package com.example.cloneinstagram.love.application.service;

import com.example.cloneinstagram.comment.entity.Comment;
import com.example.cloneinstagram.comment.repository.CommentRepository;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.love.adapter.out.persistence.BoardLoveOutputAdapter;
import com.example.cloneinstagram.love.adapter.out.persistence.CommentLoveOutputAdapter;
import com.example.cloneinstagram.love.application.port.in.CommentLoveInputPort;
import com.example.cloneinstagram.love.application.port.out.BoardLoveOutputPort;
import com.example.cloneinstagram.love.application.port.out.CommentLoveOutputPort;
import com.example.cloneinstagram.love.domain.CommentLove;
import com.example.cloneinstagram.love.adapter.out.persistence.CommentLoveRepository;
import com.example.cloneinstagram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLoveService implements CommentLoveInputPort {

    private final CommentRepository commentRepository;
    private final CommentLoveRepository commentLoveRepository;
    private final CommentLoveOutputPort commentLoveOutputPort;
    @Bean
    public CommentLoveOutputPort commentLoveOutputPort() {
        return new CommentLoveOutputAdapter();
    }

    private static final ThreadLocal<Member> threadLocalMember = new ThreadLocal<>();

    @Override
    @Transactional
    public ResponseMsgDto<Void> commentLove(Long id, Member member) {
        threadLocalMember.set(member);

        Member localMember = threadLocalMember.get();
        try {
            if (commentLoveRepository.findCommentLoveCheck(id, localMember.getId())) {
                commentLoveRepository.deleteByCommentIdAndMemberId(id, localMember.getId());
            } else {
                Comment comment = findCommentById(id);
                CommentLove commentLove = new CommentLove(comment, localMember);
                commentLoveRepository.save(commentLove);
            }
            return commentLoveOutputPort.setSuccessResponse();
        }catch(Exception e){
            commentLoveOutputPort.setFailureResponse(e.getMessage());
            throw e;
        }finally {
            clearMember();
        }
    }

    // 댓글 존재 확인 메서드
    public Comment findCommentById(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
    }

    public void clearMember() {
        threadLocalMember.remove();
    }
}
