package com.example.cloneinstagram.love.application.service;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.board.repository.BoardRepository;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.love.adapter.out.persistence.BoardLoveOutputAdapter;
import com.example.cloneinstagram.love.application.port.in.BoardLoveInputPort;
import com.example.cloneinstagram.love.application.port.out.BoardLoveOutputPort;
import com.example.cloneinstagram.love.domain.BoardLove;
import com.example.cloneinstagram.love.adapter.out.persistence.BoardLoveRepository;
import com.example.cloneinstagram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLoveService implements BoardLoveInputPort {

    private final BoardLoveRepository boardLoveRepository;
    private final BoardRepository boardRepository;

    private final BoardLoveOutputPort boardLoveOutputPort;

    @Bean
    public BoardLoveOutputPort boardLoveOutputPort() {
        return new BoardLoveOutputAdapter();
    }

    private static final ThreadLocal<Member> threadLocalMember = new ThreadLocal<>();

    @Override
    @Transactional
    public ResponseMsgDto<Void> boardLove(Long id, Member member) {
        threadLocalMember.set(member);

        Member localMember = threadLocalMember.get();
        try {
            if (boardLoveRepository.findBoardLoveCheck(id, localMember.getId())) {
                boardLoveRepository.deleteByBoardIdAndMemberId(id, localMember.getId());
            } else {
                Board board = findBoardById(id);
                BoardLove boardLove = new BoardLove(board, localMember);
                boardLoveRepository.save(boardLove);
            }
            return boardLoveOutputPort.setSuccessResponse(); // 성공 응답을 BoardLoveOutputPort를 통해 반환
        } catch (Exception e) {
            boardLoveOutputPort.setFailureResponse(e.getMessage());
            throw e;
        }finally {
            clearMember();
        }
    }

    public Board findBoardById(Long id){
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );
    }

    public void clearMember() {
        threadLocalMember.remove();
    }
}
