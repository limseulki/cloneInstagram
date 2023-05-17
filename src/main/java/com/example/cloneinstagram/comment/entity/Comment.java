package com.example.cloneinstagram.comment.entity;


import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.comment.dto.CommentRequestDto;
import com.example.cloneinstagram.common.Timestamped;
import com.example.cloneinstagram.love.entity.CommentLove;
import com.example.cloneinstagram.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLove> commentLoveList = new ArrayList<>();

    public Comment(CommentRequestDto commentRequestDto, Board board, Member member) {
        this.contents = commentRequestDto.getContents();
        this.member = member;
        this.board = board;
        this.nickName = member.getNickName();
    }


}