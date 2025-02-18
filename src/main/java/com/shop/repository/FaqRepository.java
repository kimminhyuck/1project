package com.shop.repository;


import com.shop.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    // JpaRepository를 상속받아 Faq 엔티티를 Long 타입 ID로 관리
    // 기본적인 CRUD 메서드(저장, 조회, 수정, 삭제) 사용 가능

    @Query("SELECT f FROM Faq f WHERE f.isDeleted = false")
        // JPQL 쿼리를 직접 작성:
        // Faq 엔티티에서 isDeleted가 false인(삭제되지 않은) FAQ만 선택하는 쿼리
    List<Faq> findAllActive();

    // 메서드 이름으로 쿼리 생성:
    // FAQ의 question 필드 내용 중 keyword를 포함(부분 일치)하면서
    // isDeleted가 false인(삭제되지 않은) FAQ 목록을 가져오는 메서드
    List<Faq> findByQuestionContainingAndIsDeletedFalse(String keyword);
}
