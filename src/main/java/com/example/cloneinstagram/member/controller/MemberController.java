package com.example.cloneinstagram.member.controller;

import com.example.cloneinstagram.member.dto.*;
import com.example.cloneinstagram.member.service.MemberService;
import com.example.cloneinstagram.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        return memberService.signUp(signUpRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<StatusResponseDto> logIn(@RequestBody LogInRequestDto logInRequestDto, HttpServletResponse httpServletResponse){
        return memberService.logIn(logInRequestDto, httpServletResponse);
    }

    @GetMapping("/")
    public Page<MemberResponseDto> getMemberList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberService.getUserList(userDetails.getUser(), pageable);
    }

    @PostMapping("/{nickName}")
    public ResponseEntity<StatusResponseDto> follow(@PathVariable String nickName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.follow(nickName, userDetails.getUser());
    }

    @GetMapping("/{nickName}")
    public MyFeedResponseDto memberInfo(@PathVariable String nickName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.memberInfo(nickName, userDetails.getUser());
    }
}
