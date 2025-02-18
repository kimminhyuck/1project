package com.shop.service;

import com.shop.dto.MemberInfoDto;
import com.shop.dto.MyInfoUpdateDto;
import com.shop.dto.PasswordUpdateDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 이메일 중복 체크
    public boolean validateDuplicateMember(String email) {
        Member findMember = memberRepository.findByEmail(email);
        return findMember == null;  // 중복되지 않으면 true, 중복되면 false 반환
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    // 현재 로그인한 회원의 정보를 반환하는 메서드
    public Member getCurrentLoggedInMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("로그인된 사용자가 없습니다.");
        }

        String email = authentication.getName(); // 인증된 사용자의 이메일
        return memberRepository.findByEmail(email);
    }

    public MemberInfoDto getCurrentMemberInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email);

        if(member != null) {
            String zipcode = null;
            String address = member.getAddress();

            if (address != null) {
                if (address.startsWith("[")) {
                    int endIndex = address.indexOf("]");
                    if (endIndex > 0) {
                        zipcode = address.substring(1, endIndex);
                        address = address.substring(endIndex + 2);
                    }
                }
            }

            return MemberInfoDto.builder()
                    .name(member.getName())
                    .email(member.getEmail())
                    .zipcode(zipcode)
                    .address(address)
                    .role(member.getRole())
                    .availableMileage(member.getAvailableMileage())
                    .build();
        }
        return null;
    }

    @Transactional
    public void updateEmail(String newEmail) {
        Member member = getCurrentLoggedInMember();
        String currentEmail = member.getEmail();

        // 현재 사용자의 이메일과 동일한 경우는 업데이트할 필요가 없음
        if (currentEmail.equals(newEmail)) {
            return;
        }

        // 다른 사용자가 사용중인 이메일인지 확인
        Member existingMember = memberRepository.findByEmail(newEmail);
        if (existingMember != null) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 이메일 업데이트
        memberRepository.updateMemberEmail(currentEmail, newEmail);
    }

    @Transactional
    public void updatePassword(PasswordUpdateDto passwordDto) {
        Member member = getCurrentLoggedInMember();

        // 현재 비밀번호 일치 확인
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호와 확인 비밀번호 일치 확인
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호로 업데이트
        String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        memberRepository.updatePassword(member.getEmail(), encodedPassword);
    }

    @Transactional
    public void updateAddress(MyInfoUpdateDto addressDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // JPQL을 사용하여 address 필드만 업데이트
        if(addressDto.getZipcode() != null && addressDto.getAddress() != null) {
            String formattedAddress = String.format("[%s] %s", addressDto.getZipcode(), addressDto.getAddress());
            memberRepository.updateMemberAddress(email, formattedAddress);
        }
    }
}