package com.shop.service;

import com.shop.constant.QnaStatus;

import com.shop.dto.QnaRequestDto;
import com.shop.dto.QnaResponseDto;
import com.shop.entity.Member;
import com.shop.entity.Qna;

import com.shop.exception.CustomException;
import com.shop.repository.MemberRepository;
import com.shop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createQna(QnaRequestDto dto, String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 로그인한 사용자의 Member 정보 조회
//        Member member = memberRepository.findByEmail(username)
//                .orElseThrow(() -> new EntityNotFoundException("현재 정보를 찾을 수 없습니다"));
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("현재 정보를 찾을 수 없습니다");
        }


        Qna qna = new Qna();
        qna.setName(dto.getName());
        qna.setEmail(dto.getEmail());
        qna.setTitle(dto.getTitle());
        qna.setContent(dto.getContent());
        qna.setStatus(QnaStatus.PENDING);
        qna.setCreatedBy(username);
        qna.setMember(member);  // Member 정보 설정

        qnaRepository.save(qna);
    }

    @Transactional
    public void updateAnswer(Long id, String answer) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new CustomException("존재하지 않는 QnA입니다."));

        if (!isAdmin()) {
            throw new CustomException("관리자만 답변을 등록할 수 있습니다.");
        }

        qna.setAnswer(answer);
        qna.setStatus(QnaStatus.ANSWERED);
        qna.setAnsweredByAdmin(true);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        qna.setModifiedBy(authentication.getName());

        qnaRepository.save(qna);
    }

    public List<QnaResponseDto> getAllQnas() {
        return qnaRepository.findAllActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteQna(Long id) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new CustomException("존재하지 않는 QnA입니다."));

        if (!isAdmin()) {
            throw new CustomException("관리자만 QnA를 삭제할 수 있습니다.");
        }

        qna.setDeleted(true);
        qna.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        qnaRepository.save(qna);
    }

    private QnaResponseDto convertToDto(Qna qna) {
        return new QnaResponseDto(
                qna.getId(),
                qna.getTitle(),
                qna.getName(),
                qna.getContent(),
                qna.getAnswer(),
                qna.getStatus(),
                qna.getCreatedAt(),
                qna.getUpdatedAt(),
                qna.getCreatedBy(),
                qna.getModifiedBy(),
                qna.isAnsweredByAdmin()
        );
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}