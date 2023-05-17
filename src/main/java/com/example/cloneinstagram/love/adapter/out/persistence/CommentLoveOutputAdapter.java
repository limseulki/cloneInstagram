package com.example.cloneinstagram.love.adapter.out.persistence;

import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.love.application.port.out.CommentLoveOutputPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CommentLoveOutputAdapter implements CommentLoveOutputPort {
    @Override
    public ResponseMsgDto<Void> setSuccessResponse() {
        // 성공 응답 생성
        return ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "좋아요 등록/취소 성공", null);
    }

    @Override
    public ResponseMsgDto<Void> setFailureResponse(String message) {
        // 실패 응답 처리
        // 예를 들어, 로깅 또는 예외 처리 등을 수행할 수 있습니다.
        // 이 경우에는 ResponseMsgDto를 반환하지 않습니다.
        return ResponseMsgDto.setFail(HttpStatus.BAD_REQUEST.value(), "실패");
    }
}
