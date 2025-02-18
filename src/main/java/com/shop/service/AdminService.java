package com.shop.service;

import com.shop.entity.Member;
import com.shop.constant.Role;
import com.shop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    // 회원 목록 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 정보 수정
    public Member updateMember(Long memberId, Map<String, String> updateFields) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 변경된 필드만 업데이트
        if (updateFields.containsKey("role")) {
            member.setRole(Role.valueOf(updateFields.get("role")));
        }
        if (updateFields.containsKey("name")) {
            member.setName(updateFields.get("name"));
        }
        if (updateFields.containsKey("email")) {
            member.setEmail(updateFields.get("email"));
        }
        if (updateFields.containsKey("postalCode")) {
            member.setZipCode(updateFields.get("postalCode"));
        }
        if (updateFields.containsKey("address")) {
            member.setAddress(updateFields.get("address"));
        }
        if (updateFields.containsKey("detailAddress")) {
            member.setDetailAddress(updateFields.get("detailAddress"));
        }

        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        try {
            // 1. 주문 관련 데이터 삭제
            em.createNativeQuery("DELETE FROM order_item WHERE order_id IN (SELECT order_id FROM orders WHERE member_id = ?)")
                    .setParameter(1, memberId)
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM orders WHERE member_id = ?")
                    .setParameter(1, memberId)
                    .executeUpdate();

            // 2. 마일리지 히스토리 삭제
            em.createNativeQuery("DELETE FROM mileage_history WHERE member_id = ?")
                    .setParameter(1, memberId)
                    .executeUpdate();

            // 3. 마일리지 요약 정보 삭제
            em.createNativeQuery("DELETE FROM mileage_summary WHERE member_id = ?")
                    .setParameter(1, memberId)
                    .executeUpdate();

            // 4. 회원 삭제
            memberRepository.deleteById(memberId);
        } catch (Exception e) {
            throw new RuntimeException("회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        return member;
    }
}