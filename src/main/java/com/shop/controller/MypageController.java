package com.shop.controller;


import com.shop.dto.MemberInfoDto;
import com.shop.dto.MyInfoUpdateDto;
import com.shop.dto.PasswordUpdateDto;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final MemberService memberService;

    @GetMapping("/api/info")
    @ResponseBody
    public MemberInfoDto getMemberInfo() {
        return memberService.getCurrentMemberInfo();
    }

    @PutMapping("/api/email")
    @ResponseBody
    public ResponseEntity<String> updateEmail(@RequestBody Map<String, String> request) {
        try {
            memberService.updateEmail(request.get("email"));
            return ResponseEntity.ok("이메일이 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/api/password")
    @ResponseBody
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateDto passwordDto) {
        try {
            memberService.updatePassword(passwordDto);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/api/address")
    @ResponseBody
    public ResponseEntity<String> updateAddress(@RequestBody MyInfoUpdateDto addressDto) {
        try {
            memberService.updateAddress(addressDto);
            return ResponseEntity.ok("주소가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = {"", "/"})
    public String mypage(Model model) {
        MemberInfoDto memberInfo = memberService.getCurrentMemberInfo();
        model.addAttribute("member", memberInfo);
        return "mypage/mypage";
    }
}