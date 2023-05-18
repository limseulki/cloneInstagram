package com.example.cloneinstagram.love.application.port.out;

public interface CommentLoveRepositoryPort {
    void deleteByCommentIdAndMemberId(Long commentId, Long memberId);

    boolean findCommentLoveCheck(Long commentId, Long memberId);
}
