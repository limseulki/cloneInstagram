package com.example.cloneinstagram.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.board.repository.BoardRepository;
import com.example.cloneinstagram.common.ResponseMsgDto;
import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.member.dto.*;
import com.example.cloneinstagram.member.entity.Follow;
import com.example.cloneinstagram.member.entity.Member;
import com.example.cloneinstagram.member.repository.FollowRepository;
import com.example.cloneinstagram.member.repository.MemberRepository;
import com.example.cloneinstagram.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.cloneinstagram.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final AmazonS3Client amazonS3Client;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public ResponseEntity<ResponseMsgDto<Void>> signUp(SignUpRequestDto signUpRequestDto){
        String  nickName = signUpRequestDto.getNickName();
        String email = signUpRequestDto.getEmail();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        if(!Pattern.matches("^[a-zA-Z0-9._]+$", nickName)){
            throw new CustomException(NOT_MATCH_NICKNAME);
        }
        Optional<Member> checkNickName = memberRepository.findByNickName(nickName);
        if (checkNickName.isPresent()) {
            throw new CustomException(EXIST_NICKNAME);
        }

        if(!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)){
            throw new CustomException(NOT_MATCH_EMAIL);
        }
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(EXIST_EMAIL);
        }

        if(!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$", password)){
            throw new CustomException(NOT_MATCH_PASSWORD);
        }
        if (password.length() >= 8 && password.length() <= 20) {
            throw new CustomException(INVALID_PASSWORD_LENGTH);
        }

        Member member = new Member(nickName, email, password);
        memberRepository.save(member);
        return ResponseEntity.ok(ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "회원가입 성공", null));
    }

    @Transactional
    public ResponseEntity<ResponseMsgDto<Void>> logIn(LogInRequestDto logInRequestDto, HttpServletResponse httpServletResponse){
        String email = logInRequestDto.getEmail();
        String password = logInRequestDto.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        String token = jwtUtil.createToken(member.getId(), member.getNickName(), member.getEmail());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "로그인 성공", null));
    }

    @Transactional(readOnly = true)
    public Page<MemberResponseDto> getFollowerUserList(Member member, Pageable pageable){
        return memberRepository.selectFollowerMember(pageable, member.getId());
    }

    @Transactional(readOnly = true)
    public Page<MemberResponseDto> getUnFollowerList(Member member, Pageable pageable){
        return memberRepository.selectUnFollowerMember(pageable, member.getId());
    }

    @Transactional
    public ResponseEntity<ResponseMsgDto<Void>> follow(String nickName, Member member) {
        Member followMember = memberRepository.findByNickName(nickName).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Follow follow = new Follow(member, followMember);

        Follow existFollow = followRepository.existFollow(member.getNickName(), followMember.getNickName());


        if (existFollow != null) {
            followRepository.delete(existFollow);
            return ResponseEntity.ok(ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "팔로우 취소", null));
        } else {
            followRepository.save(follow);
        }
        return ResponseEntity.ok(ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "팔로우 등록", null));

    }
//
//        Member currentMember = memberRepository.findByNickName(member.getNickName()).orElseThrow(
//                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
//
//        //매번 돌릴 때마다 member 주소 값이 바뀜
//        if(!currentMember.getFollowingList().contains(followMember)) {
//            currentMember.addFollowing(followMember);
//            followMember.addFollower(currentMember);
//            memberRepository.save(currentMember);
//            memberRepository.save(followMember);
//        } else {
//            currentMember.getFollowingList().remove(followMember);
//            followMember.getFollowerList().remove(currentMember);
//            memberRepository.save(currentMember);
//            memberRepository.save(followMember);
//            return ResponseEntity.ok(new StatusResponseDto(followMember.getNickName(), "팔로우 취소"));
//        }
//
//        return ResponseEntity.ok(new StatusResponseDto(followMember.getNickName(), "팔로우 성공"));


    //사진 업데이트 미적용
    @Transactional(readOnly = true)
    public MyFeedResponseDto memberInfo(String nickName, Member member){
        String logInNickName = member.getNickName();
        if(!StringUtils.pathEquals(nickName, logInNickName)){
            throw new CustomException(ErrorCode.CANNOT_ACCESS);
        }

        MyFeedResponseDto myFeedResponseDto = new MyFeedResponseDto(member);
        List<Board> boardList = boardRepository.findAllByMember(member);
        int followingCnt = followRepository.countAllFollowing(member.getNickName());
        int followerCnt = followRepository.countAllFollower(member.getNickName());

        myFeedResponseDto.setBoardList(boardList);
        myFeedResponseDto.setFollowingCnt(followingCnt);
        myFeedResponseDto.setFollowerCnt(followerCnt);

        return myFeedResponseDto;
    }


    @Transactional
    public ResponseEntity<ResponseMsgDto<Void>> updateInfo(String nickName, MultipartFile image, MyFeedRequestDto myFeedRequestDto, Member member) throws IOException {
        Member updateMember = memberRepository.findByNickName(nickName).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        String logInNickName = member.getNickName();
        if(!StringUtils.pathEquals(nickName, logInNickName)){
            throw new CustomException(ErrorCode.CANNOT_ACCESS);
        }

        updateMember.setNickName(myFeedRequestDto.getNickName());
        updateMember.setContents(myFeedRequestDto.getContents());

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        String imageUrl = null;

        // 새로 부여한 이미지명
        String newFileName = "image" + hour + minute + second + millis;
        String fileExtension = '.' + image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
        String imageName = S3_BUCKET_PREFIX + newFileName + fileExtension;

        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        InputStream inputStream = image.getInputStream();

        amazonS3Client.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        imageUrl = amazonS3Client.getUrl(bucketName, imageName).toString();

        updateMember.setImg(imageUrl);

        memberRepository.save(updateMember);

        return ResponseEntity.ok(ResponseMsgDto.setSuccess(HttpStatus.OK.value(), "업데이트 완료", null));
    }
}
