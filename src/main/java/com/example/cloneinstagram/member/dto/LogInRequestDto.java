package com.example.cloneinstagram.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogInRequestDto {
    private String email;
    private String password;
}
