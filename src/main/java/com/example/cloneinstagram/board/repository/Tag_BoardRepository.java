//package com.example.cloneinstagram.board.repository;
//
//import com.example.cloneinstagram.board.entity.Board;
//import com.example.cloneinstagram.board.entity.Tag_Board;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface Tag_BoardRepository extends JpaRepository<Tag_Board, Long> {
//    @Query("select tb.board from Tag_Board tb where tb.hashTag.id = :hashTagId")
//    List<Board> selectBoardByTag(Long hashTagId);
//}
