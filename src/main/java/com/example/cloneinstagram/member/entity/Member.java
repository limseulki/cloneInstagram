package com.example.cloneinstagram.member.entity;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column
    @Lob
    private String img;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "memberFollowing", cascade = CascadeType.REMOVE)
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "memberFollower", cascade = CascadeType.REMOVE)
    private List<Follow> followerList = new ArrayList<>();

    public Member(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

//    public void addFollowing(Member following) {
//        this.followingList.add(following);
//
//        if(!following.getFollowingList().contains(this)) {
//            following.getFollowerList().add(this);
//        }
//        if(!following.getMemberFollower().getFollowerList().contains(this)) {
//            following.getMemberFollower().getFollowerList().add(this);
//        }
//    }
//    public void addFollower(Member follower) {
//        this.followerList.add(follower);
//
//        if(follower.getFollowingList().contains(this)) {
//            follower.getFollowingList().add(this);
//        }
//        if(!follower.getMemberFollowing().getFollowingList().contains(this)) {
//            follower.getMemberFollowing().getFollowingList().add(this);
//        }
//    }
}
