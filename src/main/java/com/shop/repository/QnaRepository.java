package com.shop.repository;

import com.shop.constant.QnaStatus;
import com.shop.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    @Query("SELECT q FROM Qna q WHERE q.isDeleted = false")
    List<Qna> findAllActive();

    List<Qna> findByStatusAndIsDeletedFalse(QnaStatus status);

    List<Qna> findByNameAndIsDeletedFalse(String name);
}