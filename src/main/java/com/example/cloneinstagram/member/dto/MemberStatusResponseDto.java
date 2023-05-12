package com.example.cloneinstagram.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberStatusResponseDto {
    private String username;
    private String message;
}
