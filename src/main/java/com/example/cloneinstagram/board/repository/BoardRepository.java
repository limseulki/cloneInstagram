package com.example.cloneinstagram.board.repository;

import com.example.cloneinstagram.board.dto.MainFeedDto;
import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndNickName(Long id, String nickName);

    List<Board> findAllByMember(Member member);

    List<Board> findAllByMemberId(Long id);

//    @Query("SELECT new com.example.cloneinstagram.board.dto.MainFeedDto(b) FROM Board b " +
//            "JOIN b.member m " +
//            "JOIN m.followerList f " +
//            "WHERE f.memberFollowing = :member")
//    List<MainFeedDto> selectFollowingBoard(@Param("member") Member member);
}
