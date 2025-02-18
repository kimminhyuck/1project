package com.shop.dto;

import com.shop.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


// MyInfoUpdateDto.java
@Getter @Setter
public class MyInfoUpdateDto {
    private String address;
    private String zipcode;
}


