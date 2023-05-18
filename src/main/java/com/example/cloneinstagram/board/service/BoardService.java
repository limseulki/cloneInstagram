package com.example.cloneinstagram.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.cloneinstagram.board.dto.BoardRequestDto;
import com.example.cloneinstagram.board.dto.BoardResponseDto;
import com.example.cloneinstagram.board.dto.MainFeedDto;
import com.example.cloneinstagram.board.entity.Board;
import com.example.cloneinstagram.board.entity.HashTag;
import com.example.cloneinstagram.board.entity.Tag_Board;
import com.example.cloneinstagram.board.repository.BoardRepository;
import com.example.cloneinstagram.board.repository.HashTagRepository;
import com.example.cloneinstagram.board.repository.Tag_BoardRepository;
import com.example.cloneinstagram.comment.dto.CommentResponseDto;
import com.example.cloneinstagram.comment.entity.Comment;
import com.example.cloneinstagram.comment.repository.CommentRepository;
import com.example.cloneinstagram.exception.CustomException;
import com.example.cloneinstagram.exception.ErrorCode;
import com.example.cloneinstagram.love.adapter.out.persistence.BoardLoveRepository;
import com.example.cloneinstagram.love.adapter.out.persistence.CommentLoveRepository;
import com.example.cloneinstagram.member.entity.Follow;
import com.example.cloneinstagram.member.entity.Member;
import com.example.cloneinstagram.member.repository.FollowRepository;
import com.example.cloneinstagram.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final AmazonS3Client amazonS3Client;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final Tag_BoardRepository tag_boardRepository;
    private final HashTagRepository hashTagRepository;
    private final BoardLoveRepository boardLoveRepository;
    private final CommentLoveRepository commentLoveRepository;
    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    Board board;
    boolean boardLove;
    boolean commentLove;

    // 게시글 작성
    @Transactional
    public ResponseEntity<BoardResponseDto> createBoard(MultipartFile image, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) throws IOException {
        // 파일명 새로 부여를 위한 현재 시간 알아내기
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);


        // 새로 부여한 이미지명
        String newFileName = "image" + hour + minute + second + millis;
        String fileExtension = '.' + image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
        String imageName = S3_BUCKET_PREFIX + newFileName + fileExtension;

        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        InputStream inputStream = image.getInputStream();

        amazonS3Client.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imageUrl = amazonS3Client.getUrl(bucketName, imageName).toString();

        board = Board.builder()
                .imageName(imageName)
                .imageUrl(imageUrl)
                .contents(boardRequestDto.getContents())
                .member(userDetails.getUser())
                .build();

        // JPA에서 관계를 맺고 있는 엔티티의 영속성 처리 문제를 위해 먼저 board를 1차 캐시에 save를 보냄
        boardRepository.save(board);

        if(boardRequestDto.getHashtags() != null) {
            boardRequestDto.setHashtags(boardRequestDto.getHashtags());

            for (String hashTag : boardRequestDto.getHashtags()) {
                String hashTagString = hashTag.substring(1);
                HashTag existHashTag = hashTagRepository.findByHashTag(hashTagString);
                if (existHashTag != null) {
                    Tag_Board tag_board = new Tag_Board(existHashTag, board);
                    tag_boardRepository.save(tag_board);
                } else {
                    HashTag hashTagTable = new HashTag(hashTagString);
                    hashTagRepository.save(hashTagTable);
                    Tag_Board tag_board = new Tag_Board(hashTagTable, board);
                    tag_boardRepository.save(tag_board);
                }
            }
        }
        return ResponseEntity.ok(new BoardResponseDto(board));
    }

    // 게시글 수정
    @Transactional
    public ResponseEntity<BoardResponseDto> updatePost(Long id, BoardRequestDto boardRequestDto, Member member) {
        matchAuthor(id, member);
        board = existBoard(id);
        board.update(boardRequestDto);
        return ResponseEntity.ok(new BoardResponseDto(board));
    }

    // 게시글 삭제
    @Transactional
    public ResponseEntity<Void> deletePost(Long id, Member member) {
        matchAuthor(id, member);
        board = existBoard(id);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, board.getImageName()));
        boardRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }

    // 전체 피드 조회
    public ResponseEntity<Page<MainFeedDto>> getMainFeed(Member member, Pageable pageable) {
        Page<MainFeedDto> mainFeedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        List<MainFeedDto> mainFeedList = new ArrayList<>();

        for (Board board : boardRepository.findAllByMemberId(member.getId())) {
            boardLove = boardLoveRepository.findBoardLoveCheck(board.getId(), member.getId());
            mainFeedList.add(new MainFeedDto(board, getCommentList(board.getId(), member), boardLove));
        }

        // 팔로워 게시물 조회
        for (Follow follow : followRepository.findAllByMemberFollowing(member)) {
            Long followerId = follow.getMemberFollower().getId();
            for (Board board : boardRepository.findAllByMemberId(followerId)) {
                boardLove = boardLoveRepository.findBoardLoveCheck(board.getId(), member.getId());
                mainFeedList.add(new MainFeedDto(board, getCommentList(board.getId(), member), boardLove));
           }
          
        }
         //mainFeedList.addAll(boardRepository.selectFollowingBoard(member));


        // 작성일 기준 내림차순 정렬
        mainFeedList.sort(Comparator.comparing(MainFeedDto::getCreatedAt).reversed());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<MainFeedDto> pagedMainFeedList;

        if (mainFeedList.size() < startItem) {
            pagedMainFeedList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, mainFeedList.size());
            pagedMainFeedList = mainFeedList.subList(startItem, toIndex);
        }

        mainFeedPage = new PageImpl<>(pagedMainFeedList, pageable, mainFeedList.size());

        return ResponseEntity.ok(mainFeedPage);
    }

    // 태그 검색
    public List<MainFeedDto> searchByTag(String hashTag, Member member){
        List<MainFeedDto> searchFeedByTag = new ArrayList<>();

        HashTag hashTagTable = hashTagRepository.findByHashTag(hashTag);

        if(hashTagTable == null){
            throw new CustomException(ErrorCode.HASHTAG_NOT_FOUND);
        }
        List<Board> searchBoardByTag = tag_boardRepository.selectBoardByTag(hashTagTable.getId());
        for(Board board : searchBoardByTag){
            boardLove = boardLoveRepository.findBoardLoveCheck(board.getId(), member.getId());
            searchFeedByTag.add(new MainFeedDto(board, getCommentList(board.getId(), member), boardLove));
        }

        searchFeedByTag.sort(Comparator.comparing(MainFeedDto::getCreatedAt).reversed());
        return searchFeedByTag;
    }

    // 게시글에 달린 댓글 가져오기
    private List<CommentResponseDto> getCommentList(Long boardId, Member member) {
        // 게시글에 달린 댓글 찾아서 작성일 기준 오름차순 정렬
        List<Comment> commentList = commentRepository.findAllByBoardIdOrderByCreatedAtAsc(boardId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList) {
            commentLove = commentLoveRepository.findCommentLoveCheck(comment.getId(), member.getId());
            commentResponseDtoList.add(new CommentResponseDto(comment, commentLove));
        }
        return commentResponseDtoList;
    }

    // 게시글 DB 존재 여부 확인
    private Board existBoard(Long id) {
        board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return board;
    }

    // 작성자 일치 여부 확인
    private void matchAuthor(Long id, Member member) {
        board = boardRepository.findByIdAndNickName(id, member.getNickName()).orElseThrow(
                () -> new CustomException(ErrorCode.AUTHOR_NOT_SAME_MOD)
        );
    }
}