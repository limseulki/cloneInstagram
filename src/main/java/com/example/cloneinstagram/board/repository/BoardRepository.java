package com.example.cloneinstagram.board.repository;

import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndNickName(Long id, String nickName);

    List<Board> findAllByMember(Member member);

    List<Board> findAllByMemberId(Long id);

    @Query("select f.memberFollower from Board b inner join Follow f on b.id = f.memberFollowing.id")
    List<Board> selectFollowingBoard();
}
