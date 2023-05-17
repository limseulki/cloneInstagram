package com.example.cloneinstagram.board.dto;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardRequestDto {
    private String contents;

    private List<String> hashtags;

    public void setHashtags(List<String> hashtags) {
        // 맨 앞에 # 문자가 붙은 문자열만 리스트에 담도록 필터링
        if(hashtags != null) {
            this.hashtags = hashtags.stream()
                    .filter(tag -> tag.startsWith("#"))
                    .collect(Collectors.toList());
        } else {
            this.hashtags = null;
        }
    }
}
