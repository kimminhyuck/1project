package com.shop.dto;

import com.shop.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// MemberInfoDto.java
@Getter @Setter
@Builder
public class MemberInfoDto {
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private Role role;
    private Integer availableMileage;
}
