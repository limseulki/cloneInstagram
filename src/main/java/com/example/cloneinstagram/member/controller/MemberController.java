package com.example.cloneinstagram.member.controller;

import com.example.cloneinstagram.member.dto.LogInRequestDto;
import com.example.cloneinstagram.member.dto.MemberStatusResponseDto;
import com.example.cloneinstagram.member.dto.SignUpRequestDto;
import com.example.cloneinstagram.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberStatusResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        return memberService.signUp(signUpRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberStatusResponseDto> logIn(@RequestBody LogInRequestDto logInRequestDto, HttpServletResponse httpServletResponse){
        return memberService.logIn(logInRequestDto, httpServletResponse);
    }
}
