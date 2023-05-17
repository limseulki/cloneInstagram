package com.example.cloneinstagram.board.entity;

import com.example.cloneinstagram.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Tag_Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HashTag_id")
    @JsonIgnore
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Board_id")
    @JsonIgnore
    private Board board;

    public Tag_Board(HashTag hashTag, Board board){
        this.hashTag = hashTag;
        this.board = board;
    }
}
