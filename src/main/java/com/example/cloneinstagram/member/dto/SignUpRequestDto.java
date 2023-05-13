package com.example.cloneinstagram.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @Pattern(regexp = "^[a-zA-Z0-9._]$", message = "유효한 닉네임을 입력해주세요.")
    private String nickName;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소를 입력해주세요.")
    @NotNull(message = "E-mail을 입력해주세요.")
    private String email;

    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자에서 20자 사이로만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z\\p{Punct}0-9]*$", message = "비밀번호는 대문자,소문자,숫자,특수문자만 가능합니다.")
    private String password;
}
