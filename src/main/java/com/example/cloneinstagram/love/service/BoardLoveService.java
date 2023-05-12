package com.example.cloneinstagram.love.service;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.board.repository.BoardRepository;
import com.example.cloneinstagram.commonDto.ResponseMsgDto;
import com.example.cloneinstagram.love.dto.BoardLoveResponseDto;
import com.example.cloneinstagram.love.entity.BoardLove;
import com.example.cloneinstagram.love.repository.BoardLoveRepository;
import com.example.cloneinstagram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLoveService {

    private final BoardLoveRepository boardLoveRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseMsgDto boardLove(Long id, Member member) {
        try {
            if (boardLoveRepository.findByBoardIdAndMemberId(id, member.getId())) {
                boardLoveRepository.deleteByBoardIdAndMemberId(id, member.getId());
                return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "좋아요 취소 성공", null);
            } else {
                Board board = boardRepository.findById(id);
                BoardLove boardLove = new BoardLove(board, member);
                BoardLoveRepository.save(boardLove);
                return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "좋아요 등록 성공", null);
            }
        }catch(Exception e){
            return ResponseMsgDto.setFail(HttpStatus.BAD_REQUEST.value(), "실패");
        }
        return null;
    }
}
