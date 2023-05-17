package com.example.cloneinstagram.love.application.port.out;

import com.example.cloneinstagram.common.ResponseMsgDto;

public interface BoardLoveOutputPort {
    ResponseMsgDto<Void> setSuccessResponse();
    ResponseMsgDto<Void> setFailureResponse(String message);

}
