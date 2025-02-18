package com.shop.service;

import com.shop.dto.FaqRequestDto;
import com.shop.dto.FaqResponseDto;
import com.shop.entity.Faq;
import com.shop.exception.CustomException;
import com.shop.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {
    private final FaqRepository faqRepository;
    // FAQ 데이터를 DB와 주고받기 위한 레포지토리(FaqRepository)를 사용
    // final 키워드로 선언되어 있으므로 반드시 생성자나 DI를 통해 초기화 필요

    @Transactional
    // 이 메서드는 FAQ를 생성하는 동작으로, DB에 쓰기 작업이 필요하므로 @Transactional을 붙여줌
    // 실제 DB 반영은 트랜잭션 종료 시점에 이뤄진다.
    public void createFaq(FaqRequestDto dto) {
        // createFaq 메서드는 클라이언트로부터 받은 FAQ 생성 요청 DTO를 바탕으로 DB에 새 FAQ를 만든다.

        // FAQ 생성 전, 질문 내용이 비어있는지 확인
        if (dto.getQuestion() == null || dto.getQuestion().trim().isEmpty()) {
            // 질문이 없거나 공백만 있다면 CustomException 발생
            throw new CustomException("질문은 필수입니다.");
        }

        // FAQ 생성 전, 답변 내용이 비어있는지 확인
        if (dto.getAnswer() == null || dto.getAnswer().trim().isEmpty()) {
            // 답변이 없거나 공백만 있다면 CustomException 발생
            throw new CustomException("답변은 필수입니다.");
        }

        // FAQ 엔티티 객체 생성
        Faq faq = new Faq();
        // 엔티티에 질문과 답변 설정
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());

        // 레포지토리 이용해 DB에 FAQ 저장
        // 트랜잭션 종료 시점에 DB에 반영됨
        faqRepository.save(faq);
    }

    public List<FaqResponseDto> getAllFaqs() {
        // getAllFaqs 메서드는 DB에 존재하는 FAQ 목록을 DTO 형태로 반환한다.
        // FAQ 목록은 레포지토리의 findAll()을 통해 가져온다.

        return faqRepository.findAll().stream()
                // 가져온 FAQ 목록을 스트림으로 변환
                .filter(faq -> !faq.isDeleted())
                // 삭제되지 않은 FAQ만 필터링
                .map(this::convertToDto)
                // FAQ 엔티티를 DTO로 변환
                .collect(Collectors.toList());
        // 변환된 DTO들을 리스트로 모아 반환
    }

    private FaqResponseDto convertToDto(Faq faq) {
        // convertToDto 메서드는 FAQ 엔티티를 클라이언트에 응답하기 위한 DTO로 변환한다.

        FaqResponseDto dto = new FaqResponseDto();
        // DTO에 엔티티의 ID, 질문, 답변을 설정
        dto.setId(faq.getId());
        dto.setQuestion(faq.getQuestion());
        dto.setAnswer(faq.getAnswer());

        // 변환된 DTO 반환
        return dto;
    }
}
