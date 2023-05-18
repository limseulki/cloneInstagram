package com.example.cloneinstagram.love.application.port.in;

import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.member.entity.Member;

public interface BoardLoveInputPort {
    ResponseMsgDto<Void> boardLove(Long id, Member member);
}
