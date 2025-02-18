package com.shop.dto;

import com.shop.constant.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberFormDto {


    private String zipcode;

    private String addressDetail;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=4, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;//편의상 수정함

    private String confirmPassword; // 비밀번호 확인용 필드 추가

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    private Role role = Role.USER; // 기본값 USER

}