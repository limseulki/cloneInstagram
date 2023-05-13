package com.example.cloneinstagram.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String nickName;

    private String email;

    private String password;
}
