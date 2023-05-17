package com.example.cloneinstagram.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    CANNOT_FOUND_USERNAME(BAD_REQUEST, "사용자가 존재하지 않습니다."),
    AUTHOR_NOT_SAME_MOD(BAD_REQUEST, "작성자만 수정할 수 있습니다."),
    AUTHOR_NOT_SAME_DEL(BAD_REQUEST, "작성자만 삭제할 수 있습니다."),
    INVALIDATED_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다."),
    EXIST_EMAIL(BAD_REQUEST, "이미 존재하는 이메일입니다."),
    EXIST_NICKNAME(BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_FOUND_USER(BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    CANNOT_ACCESS(BAD_REQUEST, "접근 불가능한 멤버입니다."),
    NOT_MATCH_NICKNAME(BAD_REQUEST, "닉네임은 대소문자와 '.', '_'만 가능합니다."),
    NOT_MATCH_EMAIL(BAD_REQUEST, "이메일 형식이 맞지 않습니다."),
    NOT_MATCH_PASSWORD(BAD_REQUEST, "대소문자, 숫자, 특수문자를 모두 포함된 비밀번호만 가능합니다."),
    INVALID_PASSWORD_LENGTH(BAD_REQUEST, "비밀번호 길이를 8~20글자만 가능합니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    BOARD_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    HASHTAG_NOT_FOUND(NOT_FOUND, "해당 태그를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
