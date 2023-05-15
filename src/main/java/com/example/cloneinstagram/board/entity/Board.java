package com.example.cloneinstagram.board.entity;

import com.example.cloneinstagram.board.dto.BoardRequestDto;
import com.example.cloneinstagram.common.Timestamped;
import com.example.cloneinstagram.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @Column
    private String contents;

    @Column(nullable = false)
    private String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Builder
    public Board(String imageUrl, String contents, Member member) {
        this.imageUrl = imageUrl;
        this.contents = contents;
        this.member = member;
        this.nickName = member.getNickName();
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.contents = boardRequestDto.getContents();
    }
}
