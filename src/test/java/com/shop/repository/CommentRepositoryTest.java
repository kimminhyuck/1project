package com.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.CommentStar;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.QComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품평 테스트")
    public void createCommentTest(){
        Comment comment = new Comment();
        comment.setRid(2L);
        comment.setCommentStar(CommentStar.FOUR);
        comment.setContent("이게 설마 되겠어?");
        comment.setRegTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        Comment saveComment = commentRepository.save(comment);
        System.out.println(saveComment.getRid());

    }

    public void createCommentList() {
        for(int i=1; i<=10; i++){
            Comment comment = new Comment();
            Item item = new Item();
            comment.getItem().setId(57L);
            comment.setRid(1L + i);
            comment.setContent("이게 이제 된다." + i);
            comment.setRegTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            Comment saveComment = commentRepository.save(comment);
            System.out.println("Saved Comment ID:" + saveComment.getItem().getId());
        }
    }

    @Test
    @DisplayName("상품평 조회 테스트")
    public void findByCommenterTest(){
        this.createCommentList();
        List<Comment> commentList = commentRepository.findByCreatedBy("작성자5");
        for(Comment comment : commentList){
            System.out.println(comment.toString());
        }
    }

    @Test
    @DisplayName("작성자, 상품평 or 테스트")
    public void findByCommenterOrContentTest(){
        this.createCommentList();
        List<Comment> commentList = commentRepository.findByCreatedByOrContent("작성자7", "이게 이제 된다.");
        for(Comment comment : commentList){
            System.out.println(comment.toString());

        }
    }

    @Test
    @DisplayName("상품평 변호 LessThan 테스트")
    public void findByRegTimeLessThanTest(){
        this.createCommentList();
        LocalDateTime witchDate = LocalDateTime.of(2024,11,29,5,14);
        List<Comment> commentList = commentRepository.findByRegTimeLessThan(witchDate);
        for (Comment comment : commentList){
            System.out.println(comment.toString());
        }
    }

    @Test
    @DisplayName("상품평 변호 LessThan 테스트")
    public void findByRegTimeLessThanOrderByRegTimeDescTest(){
        this.createCommentList();
        LocalDateTime witchDate = LocalDateTime.of(2024,11,29,18,14);
        List<Comment> commentList = commentRepository.findByRegTimeLessThan(witchDate);
        for (Comment comment : commentList){
            System.out.println(comment.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회테스트1")
    public void queryDslTest(){
        this.createCommentList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QComment qComment = QComment.comment;
        JPAQuery<Comment> query = queryFactory.selectFrom(qComment)
                .where(qComment.commentStar.eq(CommentStar.FIVE))
                .where(qComment.content.like("%" + "이게 이제 된다." + "%"))
                .orderBy(qComment.rid.desc());

        List<Comment> commentList = query.fetch();

        for(Comment comment : commentList){
            System.out.println(comment.toString());
        }
    }
}