package com.example.cloneinstagram.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {
    private Long userId;
    private String nickName;
    private String email;

    public MemberResponseDto(Long userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }
}
