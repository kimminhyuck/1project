package com.shop.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.shop.constant.CommentStar;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.QComment;
import com.shop.repository.CommentRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

//    // 댓글 작성
//    @Transactional
//    public Comment createComment(String content, Item item, String createdBy, CommentStar commentStar) {
//        // 댓글 생성
//        Comment comment = new Comment(content, item, commentStar);
//        return commentRepository.save(comment);  // 댓글 저장
//    }
    //댓글 작성
    @Transactional
    public Comment createComment(String content, Item item, String createdBy, CommentStar commentStar) {
        // 댓글 생성
        Comment comment = new Comment(content, item, commentStar);
        commentRepository.save(comment);  // 댓글 저장
        updateAverageStar(item);
        return comment;
    }


//    public List<Comment> getCommentsByItem(Long itemId) {
//        return commentRepository.findByItemId(itemId);
//    }
    public List<Comment> getCommentsByItem(Long itemId) {
        return commentRepository.findByItemId(itemId);
    }

//    // 댓글 수정
//    @Transactional
//    public Comment updateComment(Comment comment){
////    public Comment updateComment(Long rid, String content, CommentStar commentStar) {
//        comment.setContent(comment.getContent());
//        comment.setCommentStar(comment.getCommentStar());
////        Comment comment = commentRepository.findById(rid).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));
////        comment.setContent(content);
////        comment.setCommentStar(commentStar);
//        return commentRepository.save(comment);
//    }
    // 댓글 수정
    @Transactional
    public Comment updateComment(Comment comment){
    //    public Comment updateComment(Long rid, String content, CommentStar commentStar) {
        comment.setContent(comment.getContent());
        comment.setCommentStar(comment.getCommentStar());
    //        Comment comment = commentRepository.findById(rid).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));
    //        comment.setContent(content);
    //        comment.setCommentStar(commentStar);
        commentRepository.save(comment);
        updateAverageStar(comment.getItem());  // 평균 별점 계산 및 Item에 저장 후 반환
        return comment;
    }

//    // 댓글 삭제
//    @Transactional
//    public void deleteComment(Long rid) {
//        commentRepository.deleteById(rid);
//    }
    // 댓글 삭제
    @Transactional
    public void deleteComment(Long rid) {
        Comment comment = commentRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        // 2. 해당 댓글의 아이템 정보 가져오기
        Item item = comment.getItem();
        commentRepository.deleteById(rid);
        updateAverageStar(item);
    }


    @Transactional(readOnly = true)
    public Comment getComment(Long rid) {
        return commentRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    public Double getAverageStar(Long itemId){
        List<Comment> comments = commentRepository.findByItemId(itemId); //해당 아이템의 모든 댓글 조회
        if (comments.isEmpty()) {
            return 0.0; // 댓글이 없을 경우 평균 별점은 0.0
        }

        double totalStar = 0.0; // 총 별점 합계
        for (Comment comment : comments) {
            totalStar += comment.getCommentStar().toInt(); // CommentStar를 숫자로 변환하여 합산
        }

        return totalStar / comments.size(); // 평균 별점 계산
    }
    // 평균 별점 업데이트 메서드
    private void updateAverageStar(Item item) {
        // getAverageStar 메서드를 사용하여 평균 별점 계산
        Double averageStar = getAverageStar(item.getId());

        item.setAverageStar(averageStar);  // Item의 평균 별점 업데이트
        itemRepository.save(item);  // Item 저장
    }

}
