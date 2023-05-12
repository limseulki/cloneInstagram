package com.example.cloneinstagram.member.service;

import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.member.dto.LogInRequestDto;
import com.example.cloneinstagram.member.dto.MemberStatusResponseDto;
import com.example.cloneinstagram.member.dto.SignUpRequestDto;
import com.example.cloneinstagram.member.entity.Member;
import com.example.cloneinstagram.member.repository.MemberRepository;
import com.example.cloneinstagram.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.cloneinstagram.exception.ErrorCode.EXIST_EMAIL;
import static com.example.cloneinstagram.exception.ErrorCode.EXIST_NICKNAME;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<MemberStatusResponseDto> signUp(SignUpRequestDto signUpRequestDto){
        String  nickName = signUpRequestDto.getNickName();
        String email = signUpRequestDto.getEmail();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        Optional<Member> checkNickName = memberRepository.findByNickName(nickName);
        if (checkNickName.isPresent()) {
            throw new CustomException(EXIST_NICKNAME);
        }

        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(EXIST_EMAIL);
        }

        Member member = new Member(nickName, email, password);
        memberRepository.save(member);
        return ResponseEntity.ok(new MemberStatusResponseDto(member.getNickName(), "회원가입 성공"));
    }

    @Transactional
    public ResponseEntity<MemberStatusResponseDto> logIn(LogInRequestDto logInRequestDto, HttpServletResponse httpServletResponse){
        String email = logInRequestDto.getEmail();
        String password = logInRequestDto.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        String token = jwtUtil.createToken(member.getId(), member.getNickName(), member.getEmail());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(new MemberStatusResponseDto(member.getNickName(), "로그인 성공"));
    }
}
