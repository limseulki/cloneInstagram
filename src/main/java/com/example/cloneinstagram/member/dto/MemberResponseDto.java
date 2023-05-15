package com.example.cloneinstagram.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {
    private Long userId;
    private String nickName;
    private String imgUrl;

    public MemberResponseDto(Long userId, String nickName, String img) {
        this.userId = userId;
        this.nickName = nickName;
        this.imgUrl = img;
    }
}
