package com.example.cloneinstagram.member.repository;

import com.example.cloneinstagram.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("select f from Follow f where f.memberFollowing.nickName = :followingNickName and f.memberFollower.nickName = :followerNickName")
    Follow existFollow(@Param("followingNickName") String followingNickName, @Param("followerNickName") String followerNickName);

    @Query("select count(f) from Follow f where f.memberFollowing.nickName = :followingNickName")
    int countAllFollowing(@Param("followingNickName") String followingNickName);

    @Query("select count(f) from Follow f where f.memberFollower.nickName = :followerNickName")
    int countAllFollower(@Param("followerNickName") String followerNickName);
}
