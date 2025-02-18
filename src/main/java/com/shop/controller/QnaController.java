package com.shop.controller;

import com.shop.dto.QnaRequestDto;
import com.shop.dto.QnaResponseDto;
import com.shop.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class    QnaController {

    private final QnaService qnaService;

    @GetMapping("/admin")
    public String qnaList(Model model) {
        List<QnaResponseDto> qnaList = qnaService.getAllQnas();
        model.addAttribute("qnaList", qnaList);
        return "admin/qna";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<QnaResponseDto> getAllQnas() {
        return qnaService.getAllQnas();
    }

    @PostMapping("/create")
    @ResponseBody
    public void createQna(@RequestBody QnaRequestDto qnaRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 현재 사용자의 이름 가져오기

        qnaService.createQna(qnaRequestDto, username); // username 추가
    }

    @PutMapping("/{id}/answer")
    @ResponseBody
    public void updateAnswer(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String answer = payload.get("answer");
        qnaService.updateAnswer(id, answer);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseBody
    public String deleteQna(@PathVariable Long id) {
        try {
            qnaService.deleteQna(id);
            return "문의가 성공적으로 삭제되었습니다.";
        } catch (Exception e) {
            return "삭제 중 오류가 발생했습니다.";
        }
    }
}
