package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

// PasswordUpdateDto.java
@Getter @Setter
public class PasswordUpdateDto {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
