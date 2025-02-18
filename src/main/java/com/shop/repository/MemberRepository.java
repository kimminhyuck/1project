package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);


    @Modifying
    @Query("UPDATE Member m SET m.email = :newEmail WHERE m.email = :currentEmail")
    void updateMemberEmail(@Param("currentEmail") String currentEmail, @Param("newEmail") String newEmail);

    // 주소 업데이트를 위한 쿼리 메서드
    @Modifying
    @Query("UPDATE Member m SET m.address = :address, m.updateTime = CURRENT_TIMESTAMP WHERE m.email = :email")
    void updateMemberAddress(@Param("email") String email, @Param("address") String address);

    @Modifying
    @Query("UPDATE Member m SET m.password = :password, m.updateTime = CURRENT_TIMESTAMP WHERE m.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

}