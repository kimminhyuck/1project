package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaRequestDto {
    private String name;
    private String email;
    private String title;
    private String content;
}