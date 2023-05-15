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

    @Query("select new com.example.cloneinstagram.member.dto.MemberResponseDto(f.memberFollower.id, f.memberFollower.nickName, f.memberFollower.img) from Follow f where f.memberFollowing.id = :memberId")
    Page<MemberResponseDto> selectFollowerMember(Pageable pageable, @Param("memberId") Long memberId);

    @Query("select new com.example.cloneinstagram.member.dto.MemberResponseDto(m.id, m.nickName, m.img) from Member m where not exists (select f from Follow f where f.memberFollowing.id = :memberId AND f.memberFollower.id = m.id)")
    Page<MemberResponseDto> selectUnFollowerMember(Pageable pageable, @Param("memberId") Long memberId);
}
