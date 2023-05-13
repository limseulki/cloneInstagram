package com.example.cloneinstagram.member.dto;

import com.example.cloneinstagram.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyFeedResponseDto {
    private Long memberId;
    private String nickName;
    private String img;
    private int followerCnt;
    private int followingCnt;

    public MyFeedResponseDto(Member member){
        this.memberId = member.getId();
        this.nickName = member.getNickName();
    }
}
