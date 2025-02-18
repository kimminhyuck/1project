package com.shop.controller;


import com.shop.dto.FaqRequestDto;
import com.shop.dto.FaqResponseDto;
import com.shop.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs") // 수정된 경로
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // FAQ 생성
    @PostMapping
    public ResponseEntity<String> createFaq(@RequestBody FaqRequestDto faqRequestDto) {
        faqService.createFaq(faqRequestDto);
        return ResponseEntity.ok("FAQ가 성공적으로 생성되었습니다.");
    }

    // 모든 FAQ 조회
    @GetMapping
    public ResponseEntity<List<FaqResponseDto>> getAllFaqs() {
        return ResponseEntity.ok(faqService.getAllFaqs());
    }
}
