package com.example.cloneinstagram.board.dto;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainFeedDto {
    private Long boardId;
    private String imageUrl;
    private String contents;
    private String nickName;
    private String memberImage;
    private String createdAt;
    private boolean boardLove;
    private List<CommentResponseDto> commentList;


    public MainFeedDto(Board board, List<CommentResponseDto> commentList, boolean boardLove) {

        this.boardId = board.getId();
        this.imageUrl = board.getImageUrl();
        this.contents = board.getContents();
        this.nickName = board.getNickName();
        this.memberImage = board.getMember().getImg();
        this.createdAt = board.getCreatedAt();
        this.commentList = commentList;
        this.boardLove = boardLove;
        

    }
}
