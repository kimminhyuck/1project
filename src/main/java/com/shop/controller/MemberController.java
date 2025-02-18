package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult,
                            Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        // 비밀번호 확인 체크
        if (!memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            model.addAttribute("memberFormDto", new MemberFormDto());
            model.addAttribute("message", "회원 가입을 축하합니다.");
            return "member/memberForm";
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", "에러가 발생하였습니다. 다시 시도해주세요.");
            return "member/memberForm";
        }
    }

    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmailDuplication(@RequestParam String email) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", memberService.validateDuplicateMember(email));
        return result;
    }

    @GetMapping(value = "/login")
    public String loginMember(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberForm";
    }

    @GetMapping("/oauth2/success")
    public String oauth2LoginSuccess(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        return "redirect:/";
    }


}