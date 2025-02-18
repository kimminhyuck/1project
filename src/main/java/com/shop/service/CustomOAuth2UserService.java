//package com.shop.service;
//
//import com.shop.entity.Member;
//import com.shop.constant.Role;
//import com.shop.dto.MemberFormDto;
//import org.springframework.beans.factory.annotation.Autowired;  // 이 부분 추가
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//import java.util.Map;
//
//@Service
//@Transactional
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String email = null;
//        String name = null;
//
//        if ("google".equals(registrationId)) {
//            email = oAuth2User.getAttribute("email");
//            name = oAuth2User.getAttribute("name");
//        } else if ("kakao".equals(registrationId)) {
//            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
//            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//            email = (String) kakaoAccount.get("email");
//            name = (String) profile.get("nickname");
//        } else if ("naver".equals(registrationId)) {
//            Map<String, Object> response = oAuth2User.getAttribute("response");
//            email = (String) response.get("email");
//            name = (String) response.get("name");
//        }
//
//        try {
//            Member findMember = memberService.findByEmail(email);
//            if(findMember == null) {
//                MemberFormDto memberFormDto = new MemberFormDto();
//                memberFormDto.setEmail(email);
//                memberFormDto.setName(name);
//                memberFormDto.setPassword(UUID.randomUUID().toString());
//
//                Member member = Member.createMember(memberFormDto, passwordEncoder);
//                member.setRole(Role.USER);
//                memberService.saveMember(member);
//            }
//        } catch (Exception e) {
//            throw new OAuth2AuthenticationException("Authentication failed: " + e.getMessage());
//        }
//
//        return oAuth2User;
//    }
//}
//

package com.shop.service;

import com.shop.entity.Member;
import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        // OAuth2 공급자별로 사용자 정보 추출
        if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = oAuth2User.getAttribute("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        }

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("OAuth2 공급자로부터 이메일 정보를 가져올 수 없습니다.");
        }

        try {
            Member findMember = memberService.findByEmail(email);
            if (findMember == null) {
                // 신규 회원 가입 처리
                MemberFormDto memberFormDto = new MemberFormDto();
                memberFormDto.setEmail(email);
                memberFormDto.setName(name);

                // 기본 주소 정보 설정
                memberFormDto.setAddress("[00000] 소셜로그인 기본주소");

                // 기본 비밀번호 설정
                String defaultPassword = UUID.randomUUID().toString();
                memberFormDto.setPassword(defaultPassword);

                Member member = Member.createMember(memberFormDto, passwordEncoder);
                member.setRole(Role.USER);
                memberService.saveMember(member);

                // 로그로 기본 비밀번호 출력 (개발 단계에서만 사용)
                System.out.println("소셜 로그인 회원가입 완료 - Email: " + email + ", 기본 비밀번호: " + defaultPassword);
            }
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("OAuth2 인증 과정에서 오류가 발생했습니다: " + e.getMessage());
        }

        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("email", email);

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}