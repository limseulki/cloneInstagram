package com.example.cloneinstagram.love.application.port.out;

public interface BoardLoveRepositoryPort {
    void deleteByBoardIdAndMemberId(Long boardId, Long memberId);

    boolean findBoardLoveCheck(Long boardId, Long memberId);
}
