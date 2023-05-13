package com.example.cloneinstagram.member.repository;

import com.example.cloneinstagram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickName(String nickName);
    Optional<Member> findByEmail(String email);
}
