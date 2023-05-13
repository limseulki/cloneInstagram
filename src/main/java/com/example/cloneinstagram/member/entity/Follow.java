package com.example.cloneinstagram.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWING_ID", referencedColumnName = "id")
    private Member memberFollowing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "id")
    private Member memberFollower;

    public Follow(Member memberFollowing, Member memberFollower) {
        this.memberFollowing = memberFollowing;
        this.memberFollower = memberFollower;
    }
}
