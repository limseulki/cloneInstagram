package com.example.cloneinstagram.member.repository;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.member.dto.MemberResponseDto;
import com.example.cloneinstagram.member.dto.MyFeedResponseDto;
import com.example.cloneinstagram.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickName(String nickName);
    Optional<Member> findByEmail(String email);

    @Query("SELECT new com.example.cloneinstagram.member.dto.MemberResponseDto(f.memberFollower.id, f.memberFollower.nickName, f.memberFollower.img) " +
            "FROM Follow f " +
            "WHERE f.memberFollowing.id = :memberId")
    List<MemberResponseDto> selectFollowerMember(
            @Param("memberId") Long memberId
    );

    @Query("SELECT new com.example.cloneinstagram.member.dto.MemberResponseDto(m.id, m.nickName, m.img) " +
            "FROM Member m " +
            "WHERE NOT EXISTS (" +
            "SELECT f " +
            "FROM Follow f " +
            "WHERE f.memberFollowing.id = :memberId " +
            "AND f.memberFollower.id = m.id)")
    List<MemberResponseDto> selectUnFollowerMember(
            @Param("memberId") Long memberId
    );
}
